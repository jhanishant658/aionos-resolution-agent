# Assumptions & Data Grounding Documentation

This document explicitly delineates **Assignment-Provided Facts** (ground truth from the assignment brief) from **Engineering & Implementation Assumptions** (technical choices made to build the working prototype).

---

## 1. Assignment-Provided Facts (Source of Truth)

These facts are directly transcribed from the 3 assignment brief images and have been implemented literally without invention or alteration.

### A. Environment & Context
- **Simulation Anchor Date**: Wednesday, 23 September 2026.
- **Airline Operations**: Domestic network operating routes including Delhi, Goa, Mumbai, Bengaluru, and Hyderabad.

### B. Customer Profiles & History
1. **Priya Nair**:
   - Loyalty Tier: **Gold**
   - Booking Reference: **SK4821X**
   - Contact: `priya.nair@example.com`, `+91-98xxxxxxx1`
   - Travel History (last 12 months): 6 flights, 1 prior complaint (delayed baggage, resolved with voucher).
2. **Arvind Kulkarni**:
   - Loyalty Tier: **Silver**
   - Booking Reference: **TR1190B**
   - Contact: `arvind.kulkarni@example.com`, `+91-98xxxxxxx2`
   - Travel History (last 12 months): 3 flights, no prior complaints.
3. **Meher Kaur**:
   - Loyalty Tier: **Platinum**
   - Booking Reference: **WL7742**
   - Contact: `meher.kaur@example.com`, `+91-98xxxxxxx3`
   - Travel History (last 12 months): 10 flights, 1 prior complaint (overbooking, resolved with a tier-status upgrade).

### C. Booking & Disruption Data
- **Priya Nair (SK4821X)**:
  - Flight `SK-204` (Delhi → Goa), Wed 23 Sep 2026, 18:40. **Status**: Cancelled (operational reasons).
  - Return Flight `SK-205-RETURN` (Goa → Delhi), Fri 25 Sep 2026, 16:20. **Status**: Unaffected.
- **Arvind Kulkarni (TR1190B)**:
  - Flight `SK-118` (Mumbai → Bengaluru), Wed 23 Sep 2026, 07:10. **Status**: Delayed 4h (new departure 11:10).
- **Meher Kaur (WL7742)**:
  - Flight `SK-305` (Delhi → Hyderabad), Wed 23 Sep 2026, 14:00. **Status**: Delayed 6h (new departure 20:00).

### D. Service Policies & Monetary Rules
1. **Cancellation Rebooking Rule**:
   - Airline-caused cancellation entitles the customer to free rebooking on next available flight within 24 hours OR a full refund, customer's choice.
2. **Delay Compensation Rule**:
   - Delay < 3 hours: ₹500 meal voucher.
   - Delay > 3 hours: ₹500 meal voucher + airport lounge access.
   - Delay > 5 hours: meal voucher + hotel accommodation, covering *only* the delayed hours (not a full night's stay).
3. **Refund Processing Rule**:
   - Full refund within 7 business days to the original payment method only.
4. **Fare Difference Rule**:
   - Voluntary rebooking requires paying fare difference.
   - Agents cannot waive fare differences above ₹1,500 without supervisor approval.
5. **Loyalty Tier Rule**:
   - Gold and Platinum tier customers get priority rebooking (first access to next-available seats), but *no additional compensation beyond the standard policy*.
6. **Prohibited Agent Actions (Mandatory Escalation)**:
   - Approving compensation beyond stated policy amounts.
   - Waiving a fare difference above ₹1,500.
   - Making exceptions for non-airline-caused disruptions.
   - Handling threats of legal action or formal complaints.
   - Processing refunds to a different payment method than the original.

---

## 2. Engineering & Implementation Assumptions

These technical choices were made to implement the system without creating new business policies:

### A. Technical Architecture Assumptions
1. **H2 In-Memory Database for Evaluation**:
   - Chosen so reviewers can clone and run the prototype immediately without installing or configuring external database servers. The schema uses JPA annotations so switching to MySQL requires only adjusting `application.properties`.
2. **Deterministic Fallback (Demo Mode)**:
   - Evaluator environments may have restricted network egress or lack an OpenAI/Gemini API key. The application is configured to run fully in `demo` mode out-of-the-box.
3. **Fare Amounts in Seed Bookings**:
   - The assignment specified a ₹2,000 fare difference for Meher Kaur and full refunds for Priya Nair. Base ticket fare values (₹7,500 for Priya, ₹5,200 for Arvind, ₹8,900 for Meher) and original payment method (`CREDIT_CARD`) were populated to provide realistic transaction display in the UI.

### B. Conversational State Assumptions
1. **PNR Auto-Resolution**:
   - If a customer messages from a chat session with an active PNR selected, that PNR is bound to the conversation.
   - If a customer provides a PNR in the chat text (e.g. `"My PNR is SK4821X"`), the system extracts and validates it against the repository.
   - If no PNR is provided, the agent asks only for the 6-character booking reference.
2. **Supervisor Escalation Queue**:
   - When an escalation is created, it is stored in the `escalations` table with status `ESCALATED_PENDING_HUMAN` and logged in the immutable audit log.