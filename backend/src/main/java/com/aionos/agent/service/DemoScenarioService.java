package com.aionos.agent.service;

import com.aionos.agent.config.DataInitializer;
import com.aionos.agent.dto.AgentMessageRequest;
import com.aionos.agent.dto.AgentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DemoScenarioService {

    private static final Logger log = LoggerFactory.getLogger(DemoScenarioService.class);

    private final AgentService agentService;
    private final DataInitializer dataInitializer;

    public DemoScenarioService(AgentService agentService, DataInitializer dataInitializer) {
        this.agentService = agentService;
        this.dataInitializer = dataInitializer;
    }

    public AgentResponse runScenario(int scenarioNumber) {
        log.info("Executing Assignment Demo Scenario {}", scenarioNumber);

        switch (scenarioNumber) {
            case 1:
                return agentService.processMessage(AgentMessageRequest.builder()
                        .pnr("SK4821X")
                        .message("My flight SK-204 from Delhi to Goa was cancelled! I am furious about this. I want a full cash refund for my booking and a free upgrade to business class on my return flight for all the trouble!")
                        .build());

            case 2:
                return agentService.processMessage(AgentMessageRequest.builder()
                        .pnr("TR1190B")
                        .message("My flight SK-118 to Bengaluru is delayed by 4 hours. I'm very frustrated because I am missing an important connecting meeting. Since it's been such a long delay, I ask for hotel accommodation.")
                        .build());

            case 3:
                return agentService.processMessage(AgentMessageRequest.builder()
                        .pnr("WL7742")
                        .message("My flight SK-305 is delayed 6 hours. I would like a full night's hotel stay rather than coverage for just the delayed hours. Also, I would like to be moved onto a different, higher-fare flight instead of waiting — the fare difference for that flight is ₹2,000.")
                        .build());

            default:
                throw new IllegalArgumentException("Unknown scenario number: " + scenarioNumber + ". Valid numbers are 1, 2, or 3.");
        }
    }

    public void resetDemo() {
        log.info("Resetting demo environment to initial state...");
        dataInitializer.seedDatabase();
    }
}