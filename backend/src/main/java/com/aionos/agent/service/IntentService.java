package com.aionos.agent.service;

import com.aionos.agent.enums.IntentType;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class IntentService {

    public List<IntentType> detectIntents(String message) {
        if (message == null || message.trim().isEmpty()) {
            return Collections.singletonList(IntentType.GENERAL_INQUIRY);
        }

        String lower = message.toLowerCase();
        Set<IntentType> detected = new LinkedHashSet<>();

        // Legal Threat or Formal Complaint (Mandatory high-priority detection)
        if (containsAny(lower, "legal", "lawyer", "court", "sue", "formal complaint", "consumer forum", "take action")) {
            detected.add(IntentType.LEGAL_ESCALATION);
            detected.add(IntentType.COMPLAINT);
        }

        // Refund Intent
        if (containsAny(lower, "refund", "cash back", "money back", "return payment", "reimbursement", "reimburse")) {
            detected.add(IntentType.REFUND);
        }

        // Upgrade Intent
        if (containsAny(lower, "upgrade", "business class", "first class", "cabin upgrade", "higher class")) {
            detected.add(IntentType.UPGRADE);
        }

        // Hotel Intent
        if (containsAny(lower, "hotel", "accommodation", "stay", "room", "night stay", "overnight")) {
            detected.add(IntentType.HOTEL);
        }

        // Fare Difference Intent
        if (containsAny(lower, "fare difference", "higher fare", "different flight", "price difference", "2000", "2,000", "1500", "1,500", "difference")) {
            detected.add(IntentType.FARE_DIFFERENCE);
        }

        // Rebooking / Alternate Flight Intent
        if (containsAny(lower, "rebook", "alternate flight", "another flight", "next flight", "move onto", "change flight")) {
            detected.add(IntentType.REBOOKING);
        }

        // Cancellation Intent
        if (containsAny(lower, "cancellation", "cancel", "cancelled", "canceled", "grounded")) {
            detected.add(IntentType.CANCELLATION);
        }

        // Delay Intent
        if (containsAny(lower, "delay", "delayed", "late", "waiting", "postponed", "hours delay")) {
            detected.add(IntentType.DELAY);
        }

        // Meal Voucher Intent
        if (containsAny(lower, "meal", "food", "voucher", "snack", "refreshment", "eat")) {
            detected.add(IntentType.MEAL_VOUCHER);
        }

        // Lounge Access Intent
        if (containsAny(lower, "lounge", "lounge access", "airport lounge", "waiting room")) {
            detected.add(IntentType.LOUNGE_ACCESS);
        }

        // Compensation Intent
        if (containsAny(lower, "compensation", "compensate", "trouble", "for the trouble", "claim")) {
            detected.add(IntentType.COMPENSATION);
        }

        // Complaint / Frustration Intent
        if (containsAny(lower, "furious", "angry", "terrible", "unacceptable", "frustrated", "ruined", "annoyed", "bad service", "worst")) {
            detected.add(IntentType.COMPLAINT);
        }

        // Flight / Booking Status Intent
        if (containsAny(lower, "status", "scheduled", "time", "departure", "pnr", "booking")) {
            detected.add(IntentType.FLIGHT_STATUS);
        }

        if (detected.isEmpty()) {
            detected.add(IntentType.GENERAL_INQUIRY);
        }

        return new ArrayList<>(detected);
    }

    private boolean containsAny(String input, String... keywords) {
        for (String kw : keywords) {
            if (input.contains(kw)) {
                return true;
            }
        }
        return false;
    }
}
