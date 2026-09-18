package com.aionos.agent;

import com.aionos.agent.dto.AgentMessageRequest;
import com.aionos.agent.dto.AgentResponse;
import com.aionos.agent.enums.ActionStatus;
import com.aionos.agent.enums.ActionType;
import com.aionos.agent.service.AgentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AgentServiceTest {

    @Autowired
    private AgentService agentService;

    @Test
    @DisplayName("Test 9: Missing PNR information -> Asks only necessary question")
    void testMissingPnrAsksNecessaryQuestion() {
        AgentMessageRequest req = AgentMessageRequest.builder()
                .message("Hello, my flight is delayed, what can I do?")
                .build();

        AgentResponse response = agentService.processMessage(req);
        assertNotNull(response);
        assertNull(response.getPnr());
        assertTrue(response.getAgentMessage().contains("Booking Reference (PNR)"),
                "Agent should specifically ask for the missing PNR");
        assertEquals(0, response.getExecutedActions().size());
    }

    @Test
    @DisplayName("Full Flow: Scenario 1 - Priya Nair (Gold, Cancelled, Refund + Upgrade)")
    void testScenario1PriyaNair() {
        AgentMessageRequest req = AgentMessageRequest.builder()
                .pnr("SK4821X")
                .message("My flight SK-204 was cancelled! I am furious and want a full cash refund and a free upgrade to business class on my return flight.")
                .build();

        AgentResponse response = agentService.processMessage(req);
        assertNotNull(response);
        assertEquals("Priya Nair", response.getCustomerName());
        assertEquals("GOLD", response.getLoyaltyTier());
        assertEquals("CANCELLED", response.getFlightStatus());

        // Verify refund action executed
        boolean refundExecuted = response.getExecutedActions().stream()
                .anyMatch(a -> a.getActionType() == ActionType.INITIATE_REFUND && a.getStatus() == ActionStatus.SUCCESS);
        assertTrue(refundExecuted, "Refund must be executed for cancelled flight");

        // Verify upgrade escalated
        assertTrue(response.isEscalated(), "Upgrade request must cause escalation");
        boolean upgradeEscalated = response.getExecutedActions().stream()
                .anyMatch(a -> a.getActionType() == ActionType.ESCALATE_TO_HUMAN);
        assertTrue(upgradeEscalated, "Upgrade must be escalated to human");
    }

    @Test
    @DisplayName("Full Flow: Scenario 2 - Arvind Kulkarni (Silver, 4h Delay, Hotel Request)")
    void testScenario2ArvindKulkarni() {
        AgentMessageRequest req = AgentMessageRequest.builder()
                .pnr("TR1190B")
                .message("Flight SK-118 is delayed 4 hours and I'm missing my meeting. Please arrange hotel accommodation.")
                .build();

        AgentResponse response = agentService.processMessage(req);
        assertNotNull(response);
        assertEquals("Arvind Kulkarni", response.getCustomerName());
        assertEquals("SILVER", response.getLoyaltyTier());

        // Hotel should be blocked by policy
        boolean hotelBlocked = response.getExecutedActions().stream()
                .anyMatch(a -> a.getStatus() == ActionStatus.BLOCKED_BY_POLICY);
        assertTrue(hotelBlocked, "Hotel must be blocked for 4h delay");

        // Meal voucher and lounge access should be issued
        boolean voucherIssued = response.getExecutedActions().stream()
                .anyMatch(a -> a.getActionType() == ActionType.ISSUE_MEAL_VOUCHER && a.getStatus() == ActionStatus.SUCCESS);
        boolean loungeIssued = response.getExecutedActions().stream()
                .anyMatch(a -> a.getActionType() == ActionType.ISSUE_LOUNGE_ACCESS && a.getStatus() == ActionStatus.SUCCESS);
        assertTrue(voucherIssued, "Meal voucher should be issued");
        assertTrue(loungeIssued, "Lounge access should be issued");
    }

    @Test
    @DisplayName("Full Flow: Scenario 3 - Meher Kaur (Platinum, 6h Delay, Full Night Hotel + ₹2,000 Difference)")
    void testScenario3MeherKaur() {
        AgentMessageRequest req = AgentMessageRequest.builder()
                .pnr("WL7742")
                .message("Flight SK-305 is delayed 6 hours. I want a full night's hotel stay and to move to a higher fare flight with ₹2,000 difference.")
                .build();

        AgentResponse response = agentService.processMessage(req);
        assertNotNull(response);
        assertEquals("Meher Kaur", response.getCustomerName());
        assertEquals("PLATINUM", response.getLoyaltyTier());

        // ₹2,000 fare difference must be escalated (exceeds ₹1,500 limit)
        assertTrue(response.isEscalated(), "Must be escalated due to ₹2,000 fare difference / full night hotel");
        assertTrue(response.getExecutedActions().stream()
                .anyMatch(a -> a.getActionType() == ActionType.ARRANGE_HOTEL && a.getStatus() == ActionStatus.SUCCESS),
                "The allowed delayed-hours hotel entitlement must be arranged even when the overnight extension is escalated");
    }
}
