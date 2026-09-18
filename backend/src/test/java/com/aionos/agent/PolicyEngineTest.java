package com.aionos.agent;

import com.aionos.agent.dto.PolicyDecision;
import com.aionos.agent.entity.Booking;
import com.aionos.agent.entity.Customer;
import com.aionos.agent.entity.Flight;
import com.aionos.agent.enums.ActionType;
import com.aionos.agent.enums.EscalationReason;
import com.aionos.agent.enums.FlightStatus;
import com.aionos.agent.enums.LoyaltyTier;
import com.aionos.agent.service.PolicyEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PolicyEngineTest {

    private PolicyEngine policyEngine;

    private Customer priyaGold;
    private Customer arvindSilver;
    private Customer meherPlatinum;

    private Flight cancelledFlight;
    private Flight delayed4hFlight;
    private Flight delayed6hFlight;

    private Booking bookingPriya;

    @BeforeEach
    void setUp() {
        policyEngine = new PolicyEngine();

        priyaGold = Customer.builder()
                .name("Priya Nair")
                .loyaltyTier(LoyaltyTier.GOLD)
                .bookingReference("SK4821X")
                .build();

        arvindSilver = Customer.builder()
                .name("Arvind Kulkarni")
                .loyaltyTier(LoyaltyTier.SILVER)
                .bookingReference("TR1190B")
                .build();

        meherPlatinum = Customer.builder()
                .name("Meher Kaur")
                .loyaltyTier(LoyaltyTier.PLATINUM)
                .bookingReference("WL7742")
                .build();

        cancelledFlight = Flight.builder()
                .flightNumber("SK-204")
                .status(FlightStatus.CANCELLED)
                .cancellationReason("operational reasons")
                .build();

        delayed4hFlight = Flight.builder()
                .flightNumber("SK-118")
                .status(FlightStatus.DELAYED)
                .delayHours(4)
                .build();

        delayed6hFlight = Flight.builder()
                .flightNumber("SK-305")
                .status(FlightStatus.DELAYED)
                .delayHours(6)
                .build();

        bookingPriya = Booking.builder()
                .pnr("SK4821X")
                .customer(priyaGold)
                .flight(cancelledFlight)
                .originalPaymentMethod("CREDIT_CARD")
                .fareAmount(7500.0)
                .currency("INR")
                .build();
    }

    @Test
    @DisplayName("Test 1: Airline cancellation -> Correct refund decision")
    void testAirlineCancellationRefundDecision() {
        PolicyDecision decision = policyEngine.evaluateRefund(bookingPriya, "ORIGINAL");
        assertTrue(decision.isAllowed(), "Refund should be allowed for airline-caused cancellation");
        assertEquals(ActionType.INITIATE_REFUND, decision.getRecommendedAction());
        assertEquals("POL_REFUND_PROCESS", decision.getRuleCode());
        assertTrue(decision.getReason().contains("original payment method"));
        assertTrue(decision.getReason().contains("7 business days"));
    }

    @Test
    @DisplayName("Test 2: Unauthorized upgrade request -> Escalation")
    void testUnauthorizedUpgradeEscalation() {
        PolicyDecision decision = policyEngine.evaluateUpgrade(priyaGold, bookingPriya);
        assertFalse(decision.isAllowed(), "Agent cannot approve cabin upgrade");
        assertEquals(ActionType.ESCALATE_TO_HUMAN, decision.getRecommendedAction());
        assertEquals(EscalationReason.UNAUTHORIZED_UPGRADE, decision.getEscalationReason());
        assertTrue(decision.getReason().contains("outside agent authority"));
    }

    @Test
    @DisplayName("Test 3: 4-Hour Delay -> Meal voucher and lounge access")
    void testDelay4HoursCompensation() {
        PolicyDecision decision = policyEngine.evaluateDelay(delayed4hFlight);
        assertTrue(decision.isAllowed());
        assertEquals("POL_DELAY_3TO5", decision.getRuleCode());
        assertEquals(ActionType.ISSUE_LOUNGE_ACCESS, decision.getRecommendedAction());
        assertTrue(decision.getDetails().contains("Meal Voucher + Airport Lounge Access"));
    }

    @Test
    @DisplayName("Test 4: Hotel request for 4-hour delay -> Policy denial (requires > 5 hours)")
    void testHotelRequestFor4HourDelayDenial() {
        PolicyDecision decision = policyEngine.evaluateHotelRequest(delayed4hFlight, false);
        assertFalse(decision.isAllowed(), "Hotel should be denied for 4-hour delay");
        assertEquals("POL_HOTEL_DISQUALIFIED", decision.getRuleCode());
        assertTrue(decision.getReason().contains("delays of 5 hours or less"));
    }

    @Test
    @DisplayName("Test 5: 6-Hour Delay -> Hotel accommodation covering delayed hours only")
    void testDelay6HoursHotelDelayedHoursOnly() {
        PolicyDecision decision = policyEngine.evaluateHotelRequest(delayed6hFlight, false);
        assertTrue(decision.isAllowed(), "Hotel for delayed hours portion should be allowed for > 5h delay");
        assertEquals(ActionType.ARRANGE_HOTEL, decision.getRecommendedAction());
        assertEquals("POL_DELAY_GT5", decision.getRuleCode());

        // But full night requested -> escalation
        PolicyDecision fullNightDecision = policyEngine.evaluateHotelRequest(delayed6hFlight, true);
        assertFalse(fullNightDecision.isAllowed(), "Full night hotel stay exceeds agent authority");
        assertEquals(ActionType.ESCALATE_TO_HUMAN, fullNightDecision.getRecommendedAction());
        assertEquals(EscalationReason.EXCEEDS_COMPENSATION_POLICY, fullNightDecision.getEscalationReason());
    }

    @Test
    @DisplayName("Test 6: ₹2,000 fare difference -> Exceeds ₹1,500 limit -> Supervisor escalation")
    void testFareDifferenceExceedsLimit() {
        // Within limit (₹1,500)
        PolicyDecision allowedDecision = policyEngine.evaluateFareDifference(1500.0);
        assertTrue(allowedDecision.isAllowed(), "₹1,500 fare difference is within agent limit");

        // Exceeds limit (₹2,000 from Scenario 3)
        PolicyDecision exceededDecision = policyEngine.evaluateFareDifference(2000.0);
        assertFalse(exceededDecision.isAllowed(), "₹2,000 fare difference exceeds ₹1,500 limit");
        assertEquals(ActionType.ESCALATE_TO_HUMAN, exceededDecision.getRecommendedAction());
        assertEquals(EscalationReason.FARE_DIFFERENCE_EXCEEDS_LIMIT, exceededDecision.getEscalationReason());
    }

    @Test
    @DisplayName("Test 7: Legal threat / formal complaint -> Immediate escalation")
    void testLegalThreatImmediateEscalation() {
        PolicyDecision decision = policyEngine.evaluateLegalOrFormalComplaint("I am going to file a formal complaint and take legal action");
        assertFalse(decision.isAllowed());
        assertEquals(ActionType.ESCALATE_TO_HUMAN, decision.getRecommendedAction());
        assertEquals(EscalationReason.LEGAL_THREAT_OR_FORMAL_COMPLAINT, decision.getEscalationReason());
    }

    @Test
    @DisplayName("Test 8: Refund to different payment method -> Prohibited & escalated")
    void testRefundDifferentPaymentMethodProhibited() {
        PolicyDecision decision = policyEngine.evaluateRefund(bookingPriya, "CASH");
        assertFalse(decision.isAllowed(), "Refunding to different payment method is prohibited");
        assertEquals(ActionType.ESCALATE_TO_HUMAN, decision.getRecommendedAction());
        assertEquals(EscalationReason.DIFFERENT_REFUND_METHOD_REQUESTED, decision.getEscalationReason());
    }

    @Test
    @DisplayName("Test 10: Policy cannot be overridden by AI -> Deterministic rules prevail")
    void testPolicyCannotBeOverridden() {
        // Even if customer is Platinum (Meher Kaur), upgrade to Business Class is prohibited
        PolicyDecision upgradeMeher = policyEngine.evaluateUpgrade(meherPlatinum, bookingPriya);
        assertFalse(upgradeMeher.isAllowed(), "Platinum loyalty tier does not grant free cabin upgrades beyond policy");
        assertEquals(EscalationReason.UNAUTHORIZED_UPGRADE, upgradeMeher.getEscalationReason());
    }
}
