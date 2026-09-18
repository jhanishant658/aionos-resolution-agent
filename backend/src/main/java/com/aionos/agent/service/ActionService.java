package com.aionos.agent.service;

import com.aionos.agent.dto.ActionResult;
import com.aionos.agent.entity.Booking;
import com.aionos.agent.entity.Customer;
import com.aionos.agent.entity.Flight;
import com.aionos.agent.enums.ActionStatus;
import com.aionos.agent.enums.ActionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ActionService {

    private static final Logger log = LoggerFactory.getLogger(ActionService.class);
    private final AuditLogService auditLogService;

    public ActionService(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    public ActionResult verifyBooking(Long conversationId, Booking booking) {
        String desc = "Booking " + booking.getPnr() + " verified for " + booking.getCustomer().getName() +
                " (" + booking.getCustomer().getLoyaltyTier() + " Tier). Flight: " + booking.getFlight().getFlightNumber() +
                " (" + booking.getFlight().getRouteFrom() + " -> " + booking.getFlight().getRouteTo() + ").";

        auditLogService.logAction(conversationId, booking.getPnr(), booking.getCustomer().getName(),
                ActionType.VERIFY_BOOKING, ActionStatus.SUCCESS, desc, "System Data Verification");

        return ActionResult.builder()
                .actionType(ActionType.VERIFY_BOOKING)
                .status(ActionStatus.SUCCESS)
                .description(desc)
                .policyReference("Data Verification")
                .build();
    }

    public ActionResult checkFlightStatus(Long conversationId, Booking booking) {
        Flight flight = booking.getFlight();
        String desc = "Flight status verified: " + flight.getFlightNumber() + " is " + flight.getStatus() +
                (flight.getDelayHours() != null && flight.getDelayHours() > 0 ? " (Delayed by " + flight.getDelayHours() + " hours, new departure: " + flight.getNewDeparture() + ")" : "") +
                (flight.getCancellationReason() != null ? " (Reason: " + flight.getCancellationReason() + ")" : "");

        auditLogService.logAction(conversationId, booking.getPnr(), booking.getCustomer().getName(),
                ActionType.CHECK_FLIGHT_STATUS, ActionStatus.INFORMATION_ONLY, desc, "Flight Operational System");

        return ActionResult.builder()
                .actionType(ActionType.CHECK_FLIGHT_STATUS)
                .status(ActionStatus.INFORMATION_ONLY)
                .description(desc)
                .policyReference("Flight Operations")
                .build();
    }

    public ActionResult initiateRefund(Long conversationId, Booking booking) {
        String desc = "Full refund of ₹" + booking.getFareAmount() + " initiated to original payment method (" +
                booking.getOriginalPaymentMethod() + "). Expected credit within 7 business days.";

        auditLogService.logAction(conversationId, booking.getPnr(), booking.getCustomer().getName(),
                ActionType.INITIATE_REFUND, ActionStatus.SUCCESS, desc, "POL_REFUND_PROCESS");

        return ActionResult.builder()
                .actionType(ActionType.INITIATE_REFUND)
                .status(ActionStatus.SUCCESS)
                .description(desc)
                .policyReference("Service Rules: Refund Processing Rule")
                .build();
    }

    public ActionResult issueMealVoucher(Long conversationId, Customer customer, String pnr) {
        String voucherCode = "MEAL-" + System.currentTimeMillis() % 10000;
        String desc = "Issued ₹500 digital meal voucher [" + voucherCode + "] redeemable at airport food outlets.";

        auditLogService.logAction(conversationId, pnr, customer.getName(),
                ActionType.ISSUE_MEAL_VOUCHER, ActionStatus.SUCCESS, desc, "POL_DELAY_LT3 / POL_DELAY_3TO5");

        return ActionResult.builder()
                .actionType(ActionType.ISSUE_MEAL_VOUCHER)
                .status(ActionStatus.SUCCESS)
                .description(desc)
                .policyReference("Service Rules: Delay Compensation Rule")
                .build();
    }

    public ActionResult issueLoungeAccess(Long conversationId, Customer customer, String pnr) {
        String passCode = "LNG-" + System.currentTimeMillis() % 10000;
        String desc = "Issued complimentary airport lounge access pass [" + passCode + "].";

        auditLogService.logAction(conversationId, pnr, customer.getName(),
                ActionType.ISSUE_LOUNGE_ACCESS, ActionStatus.SUCCESS, desc, "POL_DELAY_3TO5");

        return ActionResult.builder()
                .actionType(ActionType.ISSUE_LOUNGE_ACCESS)
                .status(ActionStatus.SUCCESS)
                .description(desc)
                .policyReference("Service Rules: Delay Compensation Rule (> 3 Hours)")
                .build();
    }

    public ActionResult arrangeHotel(Long conversationId, Customer customer, String pnr, int delayHours) {
        String confirmationCode = "HTL-DAY-" + System.currentTimeMillis() % 10000;
        String desc = "Arranged day-use airport transit hotel room [" + confirmationCode +
                "] covering the " + delayHours + "-hour delayed duration until rescheduled departure.";

        auditLogService.logAction(conversationId, pnr, customer.getName(),
                ActionType.ARRANGE_HOTEL, ActionStatus.SUCCESS, desc, "POL_DELAY_GT5");

        return ActionResult.builder()
                .actionType(ActionType.ARRANGE_HOTEL)
                .status(ActionStatus.SUCCESS)
                .description(desc)
                .policyReference("Service Rules: Delay Compensation Rule (> 5 Hours, Delayed Hours Only)")
                .build();
    }
}