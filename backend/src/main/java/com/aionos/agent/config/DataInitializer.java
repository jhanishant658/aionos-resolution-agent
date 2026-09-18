package com.aionos.agent.config;

import com.aionos.agent.entity.*;
import com.aionos.agent.enums.FlightStatus;
import com.aionos.agent.enums.LoyaltyTier;
import com.aionos.agent.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final CustomerRepository customerRepository;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final PolicyRuleRepository policyRuleRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ActionLogRepository actionLogRepository;
    private final EscalationRepository escalationRepository;

    public DataInitializer(CustomerRepository customerRepository, FlightRepository flightRepository,
                           BookingRepository bookingRepository, PolicyRuleRepository policyRuleRepository,
                           ConversationRepository conversationRepository, MessageRepository messageRepository,
                           ActionLogRepository actionLogRepository, EscalationRepository escalationRepository) {
        this.customerRepository = customerRepository;
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.policyRuleRepository = policyRuleRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.actionLogRepository = actionLogRepository;
        this.escalationRepository = escalationRepository;
    }

    @Override
    public void run(String... args) {
        seedDatabase();
    }

    public void seedDatabase() {
        log.info("Seeding initial assignment data into H2 database...");

        messageRepository.deleteAll();
        conversationRepository.deleteAll();
        actionLogRepository.deleteAll();
        escalationRepository.deleteAll();
        bookingRepository.deleteAll();
        flightRepository.deleteAll();
        customerRepository.deleteAll();
        policyRuleRepository.deleteAll();

        Customer priya = Customer.builder()
                .name("Priya Nair")
                .loyaltyTier(LoyaltyTier.GOLD)
                .bookingReference("SK4821X")
                .email("priya.nair@example.com")
                .maskedPhone("+91-98xxxxxxx1")
                .travelHistoryFlightsCount(6)
                .priorComplaints("1 prior complaint (delayed baggage, resolved with voucher)")
                .build();

        Customer arvind = Customer.builder()
                .name("Arvind Kulkarni")
                .loyaltyTier(LoyaltyTier.SILVER)
                .bookingReference("TR1190B")
                .email("arvind.kulkarni@example.com")
                .maskedPhone("+91-98xxxxxxx2")
                .travelHistoryFlightsCount(3)
                .priorComplaints("no prior complaints")
                .build();

        Customer meher = Customer.builder()
                .name("Meher Kaur")
                .loyaltyTier(LoyaltyTier.PLATINUM)
                .bookingReference("WL7742")
                .email("meher.kaur@example.com")
                .maskedPhone("+91-98xxxxxxx3")
                .travelHistoryFlightsCount(10)
                .priorComplaints("1 prior complaint (overbooking, resolved with a tier-status upgrade)")
                .build();

        customerRepository.save(priya);
        customerRepository.save(arvind);
        customerRepository.save(meher);

        Flight flightSK204 = Flight.builder()
                .flightNumber("SK-204")
                .routeFrom("Delhi")
                .routeTo("Goa")
                .flightDate("Wed 23 Sep 2026")
                .scheduledDeparture("18:40")
                .status(FlightStatus.CANCELLED)
                .delayHours(0)
                .newDeparture(null)
                .cancellationReason("operational reasons")
                .build();

        Flight flightReturn = Flight.builder()
                .flightNumber("SK-205-RETURN")
                .routeFrom("Goa")
                .routeTo("Delhi")
                .flightDate("Fri 25 Sep 2026")
                .scheduledDeparture("16:20")
                .status(FlightStatus.UNAFFECTED)
                .delayHours(0)
                .newDeparture(null)
                .cancellationReason(null)
                .build();

        Flight flightSK118 = Flight.builder()
                .flightNumber("SK-118")
                .routeFrom("Mumbai")
                .routeTo("Bengaluru")
                .flightDate("Wed 23 Sep 2026")
                .scheduledDeparture("07:10")
                .status(FlightStatus.DELAYED)
                .delayHours(4)
                .newDeparture("11:10")
                .cancellationReason(null)
                .build();

        Flight flightSK305 = Flight.builder()
                .flightNumber("SK-305")
                .routeFrom("Delhi")
                .routeTo("Hyderabad")
                .flightDate("Wed 23 Sep 2026")
                .scheduledDeparture("14:00")
                .status(FlightStatus.DELAYED)
                .delayHours(6)
                .newDeparture("20:00")
                .cancellationReason(null)
                .build();

        flightRepository.save(flightSK204);
        flightRepository.save(flightReturn);
        flightRepository.save(flightSK118);
        flightRepository.save(flightSK305);

        Booking bookingPriyaOutbound = Booking.builder()
                .pnr("SK4821X")
                .customer(priya)
                .flight(flightSK204)
                .bookingStatus("CONFIRMED")
                .originalPaymentMethod("CREDIT_CARD")
                .fareAmount(7500.0)
                .currency("INR")
                .build();

        Booking bookingPriyaReturn = Booking.builder()
                .pnr("SK4821X")
                .customer(priya)
                .flight(flightReturn)
                .bookingStatus("CONFIRMED")
                .originalPaymentMethod("CREDIT_CARD")
                .fareAmount(7500.0)
                .currency("INR")
                .build();

        Booking bookingArvind = Booking.builder()
                .pnr("TR1190B")
                .customer(arvind)
                .flight(flightSK118)
                .bookingStatus("CONFIRMED")
                .originalPaymentMethod("CREDIT_CARD")
                .fareAmount(5200.0)
                .currency("INR")
                .build();

        Booking bookingMeher = Booking.builder()
                .pnr("WL7742")
                .customer(meher)
                .flight(flightSK305)
                .bookingStatus("CONFIRMED")
                .originalPaymentMethod("CREDIT_CARD")
                .fareAmount(8900.0)
                .currency("INR")
                .build();

        bookingRepository.save(bookingPriyaOutbound);
        bookingRepository.save(bookingPriyaReturn);
        bookingRepository.save(bookingArvind);
        bookingRepository.save(bookingMeher);

        PolicyRule ruleCancelRebook = PolicyRule.builder()
                .ruleCode("POL_CANCEL_REBOOK")
                .ruleName("Cancellation Rebooking Rule")
                .category("Cancellation")
                .conditionDescription("If a flight is cancelled by the airline (operational/technical)")
                .allowedAction("Customer is entitled to a free rebooking on the next available flight within 24 hours, or a full refund, customer's choice.")
                .escalationCondition("Non-airline caused disruptions (e.g. customer missed flight) cannot be granted exceptions by agent.")
                .build();

        PolicyRule ruleDelayLt3 = PolicyRule.builder()
                .ruleCode("POL_DELAY_LT3")
                .ruleName("Delay Compensation Rule (< 3 Hours)")
                .category("Delay")
                .conditionDescription("Flight delayed under 3 hours")
                .allowedAction("₹500 meal voucher")
                .escalationCondition("Approving any compensation beyond stated policy amounts is prohibited.")
                .build();

        PolicyRule ruleDelay3to5 = PolicyRule.builder()
                .ruleCode("POL_DELAY_3TO5")
                .ruleName("Delay Compensation Rule (3 to 5 Hours)")
                .category("Delay")
                .conditionDescription("Flight delayed more than 3 hours (up to 5 hours)")
                .allowedAction("₹500 meal voucher + airport lounge access")
                .escalationCondition("Hotel accommodation is strictly prohibited for delays of 5 hours or less.")
                .build();

        PolicyRule ruleDelayGt5 = PolicyRule.builder()
                .ruleCode("POL_DELAY_GT5")
                .ruleName("Delay Compensation Rule (> 5 Hours)")
                .category("Delay")
                .conditionDescription("Flight delayed more than 5 hours")
                .allowedAction("Meal voucher + hotel accommodation, covering only the delayed hours (not a full night's stay)")
                .escalationCondition("Full night's hotel stay requests exceed agent authority and must be escalated to a supervisor.")
                .build();

        PolicyRule ruleRefund = PolicyRule.builder()
                .ruleCode("POL_REFUND_PROCESS")
                .ruleName("Refund Processing Rule")
                .category("Refund")
                .conditionDescription("Refunds for airline-caused cancellations")
                .allowedAction("Processed in full within 7 business days. Issued to the original payment method only.")
                .escalationCondition("Processing refunds to a different payment method than the original is strictly prohibited and must be escalated.")
                .build();

        PolicyRule ruleFareDiff = PolicyRule.builder()
                .ruleCode("POL_FARE_DIFF")
                .ruleName("Fare Difference Rule")
                .category("Fare Difference")
                .conditionDescription("Customer voluntarily chooses to rebook on a higher-fare flight (not airline-caused)")
                .allowedAction("Customer must pay fare difference. Agents may waive fare differences up to ₹1,500 only.")
                .escalationCondition("Agents cannot waive fare differences above ₹1,500 without supervisor approval. Must escalate.")
                .build();

        PolicyRule ruleLoyalty = PolicyRule.builder()
                .ruleCode("POL_LOYALTY_TIER")
                .ruleName("Loyalty Tier Rule")
                .category("Loyalty")
                .conditionDescription("Gold and Platinum tier customers")
                .allowedAction("Priority rebooking (first access to next-available seats)")
                .escalationCondition("No additional compensation beyond standard policy. Cabin upgrades to Business Class cannot be approved by agent.")
                .build();

        PolicyRule ruleLegal = PolicyRule.builder()
                .ruleCode("POL_LEGAL_ESCALATION")
                .ruleName("Legal / Formal Complaint Escalation")
                .category("Escalation")
                .conditionDescription("Customer threatens legal action or formal complaints")
                .allowedAction("Politely acknowledge frustration and reassure customer.")
                .escalationCondition("Handling threats of legal action or formal complaints — must be escalated immediately to specialist support team.")
                .build();

        policyRuleRepository.save(ruleCancelRebook);
        policyRuleRepository.save(ruleDelayLt3);
        policyRuleRepository.save(ruleDelay3to5);
        policyRuleRepository.save(ruleDelayGt5);
        policyRuleRepository.save(ruleRefund);
        policyRuleRepository.save(ruleFareDiff);
        policyRuleRepository.save(ruleLoyalty);
        policyRuleRepository.save(ruleLegal);

        log.info("Database initialized successfully with 3 customers, 4 flights, 4 bookings, and 8 policy rules.");
    }
}