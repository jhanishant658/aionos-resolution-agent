package com.aionos.agent.service;

import com.aionos.agent.dto.PolicyDecision;
import com.aionos.agent.entity.Booking;
import com.aionos.agent.entity.Customer;
import com.aionos.agent.entity.Flight;
import com.aionos.agent.enums.ActionType;
import com.aionos.agent.enums.EscalationReason;
import com.aionos.agent.enums.FlightStatus;
import com.aionos.agent.enums.LoyaltyTier;
import org.springframework.stereotype.Service;

@Service
public class PolicyEngine {

    /**
     * Evaluates cancellation policy for a given flight and booking.
     * Service Rule: "If a flight is cancelled by the airline, the customer is entitled to
     * a free rebooking on the next available flight within 24 hours, or a full refund, customer's choice."
     */
    public PolicyDecision evaluateCancellation(Flight flight, Booking booking) {
        if (flight.getStatus() == FlightStatus.CANCELLED) {
            return PolicyDecision.builder()
                    .ruleCode("POL_CANCEL_REBOOK")
                    .ruleName("Cancellation Rebooking & Refund Policy")
                    .allowed(true)
                    .recommendedAction(ActionType.INITIATE_REFUND)
                    .reason("Flight " + flight.getFlightNumber() + " was cancelled due to " +
                            (flight.getCancellationReason() != null ? flight.getCancellationReason() : "airline operational reasons") +
                            ". Customer is eligible for a full refund or free rebooking within 24 hours.")
                    .policyReference("Service Rules: Cancellation Rebooking Rule")
                    .details("Eligible: Full refund to original payment method OR free rebooking within 24h")
                    .build();
        }

        return PolicyDecision.builder()
                .ruleCode("POL_CANCEL_REBOOK")
                .ruleName("Cancellation Policy")
                .allowed(false)
                .recommendedAction(ActionType.CHECK_FLIGHT_STATUS)
                .reason("Flight " + flight.getFlightNumber() + " is not cancelled (Current Status: " + flight.getStatus() + ").")
                .policyReference("Service Rules: Cancellation Rebooking Rule")
                .build();
    }

    /**
     * Evaluates delay compensation entitlement based on duration:
     * - Delay under 3 hours: ₹500 meal voucher
     * - Delay more than 3 hours (up to 5): meal voucher + lounge access
     * - Delay more than 5 hours: meal voucher + hotel accommodation (delayed hours only)
     */
    public PolicyDecision evaluateDelay(Flight flight) {
        if (flight.getStatus() != FlightStatus.DELAYED || flight.getDelayHours() == null || flight.getDelayHours() <= 0) {
            return PolicyDecision.builder()
                    .ruleCode("POL_DELAY_NONE")
                    .ruleName("Delay Compensation Policy")
                    .allowed(false)
                    .recommendedAction(ActionType.CHECK_FLIGHT_STATUS)
                    .reason("Flight is on schedule or unaffected.")
                    .policyReference("Service Rules: Delay Compensation Rule")
                    .build();
        }

        int hours = flight.getDelayHours();

        if (hours < 3) {
            return PolicyDecision.builder()
                    .ruleCode("POL_DELAY_LT3")
                    .ruleName("Delay Compensation (<3 Hours)")
                    .allowed(true)
                    .recommendedAction(ActionType.ISSUE_MEAL_VOUCHER)
                    .reason("Flight delayed " + hours + " hours. Entitled to ₹500 meal voucher.")
                    .policyReference("Service Rules: Delay Compensation Rule (Delay under 3 hours: ₹500 meal voucher)")
                    .details("Benefits: ₹500 Meal Voucher")
                    .build();
        } else if (hours <= 5) {
            return PolicyDecision.builder()
                    .ruleCode("POL_DELAY_3TO5")
                    .ruleName("Delay Compensation (3-5 Hours)")
                    .allowed(true)
                    .recommendedAction(ActionType.ISSUE_LOUNGE_ACCESS)
                    .reason("Flight delayed " + hours + " hours. Entitled to ₹500 meal voucher + lounge access pass.")
                    .policyReference("Service Rules: Delay Compensation Rule (Delay > 3 hours: meal voucher + lounge access)")
                    .details("Benefits: ₹500 Meal Voucher + Airport Lounge Access")
                    .build();
        } else {
            return PolicyDecision.builder()
                    .ruleCode("POL_DELAY_GT5")
                    .ruleName("Delay Compensation (>5 Hours)")
                    .allowed(true)
                    .recommendedAction(ActionType.ARRANGE_HOTEL)
                    .reason("Flight delayed " + hours + " hours. Entitled to meal voucher + hotel accommodation covering delayed-hours portion only.")
                    .policyReference("Service Rules: Delay Compensation Rule (Delay > 5 hours: meal voucher + hotel accommodation)")
                    .details("Benefits: ₹500 Meal Voucher + Day-use Hotel Room for delayed hours")
                    .build();
        }
    }

    /**
     * Evaluates hotel accommodation request.
     * Rule: Only granted if delay > 5 hours, covering ONLY delayed hours (not full night).
     */
    public PolicyDecision evaluateHotelRequest(Flight flight, boolean isFullNightRequested) {
        int delayHours = flight.getDelayHours() != null ? flight.getDelayHours() : 0;

        // Condition 1: Delay must be greater than 5 hours
        if (delayHours <= 5) {
            return PolicyDecision.builder()
                    .ruleCode("POL_HOTEL_DISQUALIFIED")
                    .ruleName("Hotel Accommodation Policy")
                    .allowed(false)
                    .recommendedAction(ActionType.CHECK_POLICY)
                    .reason("Hotel accommodation is strictly prohibited for delays of 5 hours or less. Current delay is " +
                            delayHours + " hours (Flight " + flight.getFlightNumber() + "). Eligible for meal voucher and lounge access only.")
                    .policyReference("Service Rules: Delay Compensation Rule & Prohibited Actions: Approving compensation beyond policy")
                    .details("Disqualified: Delay " + delayHours + "h <= 5h threshold")
                    .build();
        }

        // Condition 2: If delay > 5 hours, but customer requests full night's stay instead of delayed hours
        if (isFullNightRequested) {
            return PolicyDecision.builder()
                    .ruleCode("POL_HOTEL_FULL_NIGHT_PROHIBITED")
                    .ruleName("Hotel Accommodation Policy - Full Night Stay")
                    .allowed(false)
                    .recommendedAction(ActionType.ESCALATE_TO_HUMAN)
                    .reason("Policy specifies hotel accommodation covers only the delayed-hours portion (day-use until rescheduled departure), not a full night's stay. Granting a full night requires human supervisor escalation.")
                    .policyReference("Service Rules: Delay Compensation Rule (>5h covers only delayed hours, not full night)")
                    .escalationReason(EscalationReason.EXCEEDS_COMPENSATION_POLICY)
                    .details("Escalated: Full night stay requested; agent authorized only for delayed-hours coverage")
                    .build();
        }

        // Delay > 5 hours and standard delayed-hours coverage
        return PolicyDecision.builder()
                .ruleCode("POL_DELAY_GT5")
                .ruleName("Hotel Accommodation Policy - Delayed Hours")
                .allowed(true)
                .recommendedAction(ActionType.ARRANGE_HOTEL)
                .reason("Delay of " + delayHours + " hours exceeds 5 hours. Customer qualifies for hotel accommodation covering the delayed-hours duration.")
                .policyReference("Service Rules: Delay Compensation Rule (>5h)")
                .details("Approved: Hotel room for delayed hours portion")
                .build();
    }

    /**
     * Evaluates refund request:
     * - Airline caused: Full refund in 7 business days to ORIGINAL payment method only.
     * - Different payment method requested: Prohibited, must escalate immediately.
     */
    public PolicyDecision evaluateRefund(Booking booking, String requestedPaymentMethod) {
        Flight flight = booking.getFlight();
        if (flight.getStatus() != FlightStatus.CANCELLED) {
            return PolicyDecision.builder()
                    .ruleCode("POL_REFUND_NOT_CANCELLED")
                    .ruleName("Refund Processing Rule")
                    .allowed(false)
                    .recommendedAction(ActionType.CHECK_POLICY)
                    .reason("Standard refund not applicable because flight is not cancelled by airline.")
                    .policyReference("Service Rules: Refund Processing Rule")
                    .build();
        }

        // Check if customer asked for a different refund payment method
        if (requestedPaymentMethod != null &&
                !requestedPaymentMethod.equalsIgnoreCase(booking.getOriginalPaymentMethod()) &&
                !requestedPaymentMethod.equalsIgnoreCase("ORIGINAL")) {
            return PolicyDecision.builder()
                    .ruleCode("POL_REFUND_DIFFERENT_METHOD")
                    .ruleName("Refund Payment Method Restriction")
                    .allowed(false)
                    .recommendedAction(ActionType.ESCALATE_TO_HUMAN)
                    .reason("Processing refunds to a different payment method than the original is prohibited and must be escalated to a human agent.")
                    .policyReference("Prohibited Actions: Processing refunds to a different payment method than the original")
                    .escalationReason(EscalationReason.DIFFERENT_REFUND_METHOD_REQUESTED)
                    .build();
        }

        return PolicyDecision.builder()
                .ruleCode("POL_REFUND_PROCESS")
                .ruleName("Refund Processing Rule")
                .allowed(true)
                .recommendedAction(ActionType.INITIATE_REFUND)
                .reason("Full refund of original fare processed to original payment method (" +
                        booking.getOriginalPaymentMethod() + ") within 7 business days.")
                .policyReference("Service Rules: Refund Processing Rule")
                .details("Full refund: " + booking.getFareAmount() + " " + booking.getCurrency() + " to " + booking.getOriginalPaymentMethod() + " within 7 business days")
                .build();
    }

    /**
     * Evaluates upgrade request:
     * Loyalty Tier Rule: "Gold and Platinum tier customers get priority rebooking (first access to next-available seats),
     * but no additional compensation beyond the standard policy."
     * Prohibited: "Approving any compensation beyond the stated policy amounts"
     */
    public PolicyDecision evaluateUpgrade(Customer customer, Booking booking) {
        return PolicyDecision.builder()
                .ruleCode("POL_UPGRADE_PROHIBITED")
                .ruleName("Cabin Upgrade Policy")
                .allowed(false)
                .recommendedAction(ActionType.ESCALATE_TO_HUMAN)
                .reason("Complimentary upgrades to business class are outside agent authority. Even for " +
                        customer.getLoyaltyTier() + " tier members, loyalty benefits provide priority rebooking only, not additional compensation or free class upgrades.")
                .policyReference("Service Rules: Loyalty Tier Rule & Prohibited Actions: Approving compensation beyond policy")
                .escalationReason(EscalationReason.UNAUTHORIZED_UPGRADE)
                .details("Escalated: Free Business Class upgrade requested by " + customer.getLoyaltyTier() + " member")
                .build();
    }

    /**
     * Evaluates fare difference waiver:
     * Rule: "Agents cannot waive fare differences above ₹1,500 without supervisor approval."
     */
    public PolicyDecision evaluateFareDifference(double fareDifference) {
        if (fareDifference <= 1500.0) {
            return PolicyDecision.builder()
                    .ruleCode("POL_FARE_DIFF_ALLOWED")
                    .ruleName("Fare Difference Waiver Policy (Within Agent Limit)")
                    .allowed(true)
                    .recommendedAction(ActionType.REBOOK_FLIGHT)
                    .reason("Fare difference of ₹" + fareDifference + " is within agent waiver limit of ₹1,500.")
                    .policyReference("Service Rules: Fare Difference Rule")
                    .details("Approved: Fare difference waiver of ₹" + fareDifference)
                    .build();
        } else {
            return PolicyDecision.builder()
                    .ruleCode("POL_FARE_DIFF_EXCEEDED")
                    .ruleName("Fare Difference Waiver Policy (Exceeds Limit)")
                    .allowed(false)
                    .recommendedAction(ActionType.ESCALATE_TO_HUMAN)
                    .reason("Fare difference of ₹" + (int)fareDifference + " exceeds the agent's maximum waiver authority limit of ₹1,500. Requires supervisor approval.")
                    .policyReference("Service Rules: Fare Difference Rule & Prohibited Actions: Waiving a fare difference above ₹1,500")
                    .escalationReason(EscalationReason.FARE_DIFFERENCE_EXCEEDS_LIMIT)
                    .details("Escalated: Difference ₹" + (int)fareDifference + " > Agent limit ₹1,500")
                    .build();
        }
    }

    /**
     * Evaluates legal threats or formal complaints:
     * Prohibited: "Handling threats of legal action or formal complaints — must be escalated immediately"
     */
    public PolicyDecision evaluateLegalOrFormalComplaint(String message) {
        return PolicyDecision.builder()
                .ruleCode("POL_LEGAL_ESCALATION")
                .ruleName("Legal Action / Formal Complaint Escalation")
                .allowed(false)
                .recommendedAction(ActionType.ESCALATE_TO_HUMAN)
                .reason("Customer mentioned legal action or formal complaint. Policy mandates immediate escalation to specialist support team.")
                .policyReference("Prohibited Actions: Handling threats of legal action or formal complaints — must be escalated immediately")
                .escalationReason(EscalationReason.LEGAL_THREAT_OR_FORMAL_COMPLAINT)
                .details("Escalated: Legal or formal complaint trigger detected")
                .build();
    }
}
