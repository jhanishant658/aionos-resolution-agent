package com.aionos.agent.service;

import com.aionos.agent.dto.ActionResult;
import com.aionos.agent.dto.PolicyDecision;
import com.aionos.agent.entity.Booking;
import com.aionos.agent.entity.Customer;
import com.aionos.agent.enums.ActionStatus;
import com.aionos.agent.enums.ActionType;
import com.aionos.agent.enums.FlightStatus;
import com.aionos.agent.enums.IntentType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoAIService implements AIService {

    @Override
    public String generateResponse(Customer customer, Booking booking, List<IntentType> intents,
                            List<PolicyDecision> decisions, List<ActionResult> actions,
                            boolean escalated, String escalationReason, String userMessage) {

        String customerName = customer.getName();
        String tier = customer.getLoyaltyTier().name();
        String flightNum = booking.getFlight().getFlightNumber();
        String route = booking.getFlight().getRouteFrom() + " to " + booking.getFlight().getRouteTo();
        String lowerMsg = userMessage.toLowerCase();

        // 1. Legal threat / formal complaint immediate response
        if (intents.contains(IntentType.LEGAL_ESCALATION)) {
            return "I hear you, " + customerName + ", and I am truly sorry that this experience has caused such profound frustration. " +
                   "Because you mentioned formal/legal escalation, our policy requires me to route this directly to our specialist support team. " +
                   "A senior resolution specialist has been assigned to your case (PNR: " + booking.getPnr() + ") and will contact you directly within 2 hours.";
        }

        // 2. Scenario 1 Pattern: Priya Nair (Gold) - Cancelled flight, refund + business class upgrade
        if (booking.getFlight().getStatus() == FlightStatus.CANCELLED &&
                (intents.contains(IntentType.UPGRADE) || intents.contains(IntentType.REFUND) || lowerMsg.contains("furious"))) {

            StringBuilder sb = new StringBuilder();
            sb.append("Dear ").append(customerName).append(", I completely understand your frustration regarding the cancellation of flight ")
              .append(flightNum).append(" (").append(route).append("). As a valued ").append(tier)
              .append(" member, we sincerely apologize for the operational disruption.\n\n");

            sb.append("Here is how we have handled your request under our service rules:\n");
            sb.append("1. Full Refund: Approved. Since the cancellation was airline-caused, a full refund of ₹")
              .append(booking.getFareAmount() != null ? String.format("%.0f", booking.getFareAmount()) : "7,500")
              .append(" has been initiated to your original payment method (")
              .append(booking.getOriginalPaymentMethod()).append("). It will be credited within 7 business days.\n");

            if (intents.contains(IntentType.UPGRADE)) {
                sb.append("2. Business Class Upgrade: Escalated. Under our Loyalty Tier Policy, ")
                  .append(tier).append(" members receive priority rebooking, but agent guidelines strictly prohibit approving complimentary cabin class upgrades beyond policy. I have forwarded your upgrade request to a duty supervisor for special review.");
            } else {
                sb.append("2. Rebooking: If you prefer to travel, you are entitled to free priority rebooking on the next available flight within 24 hours.");
            }

            return sb.toString();
        }

        // 3. Scenario 2 Pattern: Arvind Kulkarni (Silver) - 4h delay, missing meeting, hotel request
        if (booking.getFlight().getStatus() == FlightStatus.DELAYED &&
                booking.getFlight().getDelayHours() != null && booking.getFlight().getDelayHours() == 4 &&
                (intents.contains(IntentType.HOTEL) || lowerMsg.contains("hotel") || lowerMsg.contains("meeting"))) {

            return "Dear " + customerName + ", I sincerely apologize for the delay and completely understand your concern about missing your connecting meeting. " +
                   "Flight " + flightNum + " (" + route + ") is delayed by 4 hours, with a revised departure time of 11:10.\n\n" +
                   "Here are your entitlements under our Delay Compensation Rule:\n" +
                   "1. Refreshments & Comfort: Your 4-hour delay qualifies for a complimentary ₹500 digital meal voucher and airport lounge access. Both have been activated for your booking.\n" +
                   "2. Hotel Accommodation: Under airline policy, hotel accommodation is provided only when a delay exceeds 5 hours. Because the current delay is 4 hours, hotel accommodation cannot be granted.\n\n" +
                   "You are welcome to proceed directly to the airport lounge to relax and work comfortably before your boarding at 11:10.";
        }

        // 4. Scenario 3 Pattern: Meher Kaur (Platinum) - 6h delay, full night hotel + ₹2,000 fare difference
        if (booking.getFlight().getStatus() == FlightStatus.DELAYED &&
                booking.getFlight().getDelayHours() != null && booking.getFlight().getDelayHours() == 6) {

            StringBuilder sb = new StringBuilder();
            sb.append("Dear ").append(customerName).append(", thank you for reaching out. As a valued ").append(tier)
              .append(" member, we deeply regret the 6-hour delay on flight ").append(flightNum)
              .append(" (").append(route).append("), now rescheduled to 20:00.\n\n");

            sb.append("Here is the resolution based on airline policy:\n");
            sb.append("1. Hotel Accommodation: Since your delay exceeds 5 hours, I have arranged day-use hotel accommodation covering the 6-hour waiting window until your 20:00 departure. Regarding your request for a full night's stay, policy restricts agent authority to the delayed-hours duration, so an overnight extension has been escalated to supervisory review.\n");

            if (intents.contains(IntentType.FARE_DIFFERENCE) || lowerMsg.contains("2000") || lowerMsg.contains("2,000")) {
                sb.append("2. Alternate Flight Fare Difference: Escalated. Agents have the authority to waive fare differences up to ₹1,500. Because the fare difference for your requested flight is ₹2,000, it exceeds my waiver limit and has been escalated to our duty supervisor for expedited approval.\n");
            }

            sb.append("3. Meal & Lounge: In addition, a ₹500 meal voucher and lounge access pass have been issued to your profile.");
            return sb.toString();
        }

        // General fallback tailored to decisions and actions
        StringBuilder sb = new StringBuilder();
        sb.append("Hello ").append(customerName).append(". Regarding your booking ").append(booking.getPnr())
          .append(" for flight ").append(flightNum).append(":\n\n");

        for (ActionResult action : actions) {
            if (action.getStatus() == ActionStatus.SUCCESS) {
                sb.append("• ").append(action.getDescription()).append("\n");
            }
        }

        if (escalated) {
            sb.append("\nNote: Your request regarding \"").append(escalationReason)
              .append("\" has been forwarded to our human specialist team for supervisor review.");
        }

        return sb.toString();
    }
}
