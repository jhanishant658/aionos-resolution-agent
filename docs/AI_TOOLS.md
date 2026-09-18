# AI Tools Documentation

This document provides a transparent and accurate disclosure of all AI tools, models, and paradigms utilized in the design, development, and runtime execution of the **AIONOS Customer-Facing Resolution Agent**.

---

## 1. Development Tools Utilized

### Google Antigravity (Advanced Agentic Coding Assistant)
- **Role**: Pair-programming and development assistant.
- **Tasks Performed**:
  - Transcribing and structuring assignment briefs, policies, customer profiles, and flight tables from image sources into typed Java domain models and Spring Boot entities.
  - Scaffolding the Spring Boot 3.3 Maven project and React 18 TypeScript frontend architecture.
  - Formulating deterministic JUnit 5 test suites covering all policy invariants and edge cases.
  - Ensuring strict separation between probabilistic LLM components and deterministic Java business logic.

---

## 2. Runtime AI Architecture

### Separation of Responsibilities

```
                                  +---------------------------------------+
                                  |         Passenger Input               |
                                  +---------------------------------------+
                                                      |
                                                      v
                                  +---------------------------------------+
                                  |         Intent Understanding          |
                                  | (Pattern Recognition / Natural Lang)  |
                                  +---------------------------------------+
                                                      |
                                                      v
      +-----------------------------------------------------------------------------------------------+
      |                                DETERMINISTIC CONTROL BOUNDARY                                 |
      |                                                                                               |
      |       [ AI PROHIBITED FROM OVERRIDING BUSINESS RULES OR COMMITTING FINANCIAL LIABILITIES ]    |
      |                                                                                               |
      |  +-----------------------------------------------------------------------------------------+  |
      |  |                           Java PolicyEngine & DecisionEngine                            |  |
      |  |  • Evaluates delay hours threshold (<3h, 3-5h, >5h)                                     |  |
      |  |  • Validates airline cancellation & original payment method constraint                  |  |
      |  |  • Enforces agent waiver limits (Max ₹1,500; ₹2,000 strictly requires escalation)      |  |
      |  |  • Restricts loyalty tier benefits (Gold/Platinum get priority, not free upgrades)      |  |
      |  |  • Dispatches automated supervisor escalation tickets                                   |  |
      |  +-----------------------------------------------------------------------------------------+  |
      |                                                                                               |
      +-----------------------------------------------------------------------------------------------+
                                                      |
                                                      v
                                  +---------------------------------------+
                                  |         Customer Communication        |
                                  |  (Empathetic Response Generation)     |
                                  +---------------------------------------+
```

### What AI IS Used For:
1. **Natural Language Understanding (NLU)**:
   - Extracting passenger intents (cancellation queries, refund requests, upgrade desires, hotel requests, fare difference disputes).
   - Sentiment & Frustration Detection: Detecting when a passenger expresses distress (e.g. Priya's "furious" state or Arvind's frustration over missed business meetings) to adjust communication tone.
   - Identifying emergency keywords: Detecting legal threats or formal complaint triggers (`"sue"`, `"lawyer"`, `"consumer forum"`, `"formal complaint"`) to immediately trigger specialist escalation.

2. **Empathetic Response Formatting**:
   - Synthesizing polite, empathetic, and de-escalating customer-facing explanations that acknowledge distress without admitting legal liability or overpromising out-of-policy benefits.

### What AI is STRICTLY PROHIBITED from Doing (Implemented Deterministically in Java):
1. **No Hallucinated Compensation**: AI cannot decide compensation amounts, issue vouchers, or create exceptions.
2. **No Policy Waivers**: Fare differences exceeding ₹1,500 (e.g. Meher Kaur's ₹2,000 difference) are rejected by the Java `PolicyEngine` and routed to supervisors regardless of customer prompt persuasion.
3. **No Unauthorized Upgrades**: Gold and Platinum loyalty tiers receive priority rebooking only; cabin upgrades to Business Class cannot be granted by the agent and are escalated.
4. **No Alternate Payment Methods**: Refunds are strictly locked to the original payment method processed within 7 business days per airline policy.
5. **No Full Night Hotel Exceptions**: For flights delayed over 5 hours (e.g. Meher's 6h delay), hotel accommodation covers only the delayed-hours waiting duration; full overnight hotel requests are blocked and escalated.

---

## 3. Demo Mode (Zero External API Key Dependency)

To guarantee high reliability during evaluation, the prototype includes `DemoAIService`, allowing the system to run locally without an external OpenAI or Gemini API key.

When an external LLM key is configured in `application.properties`, the LLM operates as an advisory layer for conversational tone while the Java `PolicyEngine` remains the final authority.