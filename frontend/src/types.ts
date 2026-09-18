export type LoyaltyTier = 'SILVER' | 'GOLD' | 'PLATINUM';

export type FlightStatus = 'CANCELLED' | 'DELAYED' | 'UNAFFECTED' | 'ON_TIME';

export type ActionType = 
  | 'VERIFY_BOOKING'
  | 'CHECK_FLIGHT_STATUS'
  | 'CHECK_POLICY'
  | 'INITIATE_REFUND'
  | 'REBOOK_FLIGHT'
  | 'ISSUE_MEAL_VOUCHER'
  | 'ISSUE_LOUNGE_ACCESS'
  | 'ARRANGE_HOTEL'
  | 'PROCESS_INFORMATION_REQUEST'
  | 'ESCALATE_TO_HUMAN';

export type ActionStatus = 'SUCCESS' | 'BLOCKED_BY_POLICY' | 'ESCALATED' | 'INFORMATION_ONLY';

export type IntentType = 
  | 'CANCELLATION'
  | 'DELAY'
  | 'REFUND'
  | 'REBOOKING'
  | 'HOTEL'
  | 'MEAL_VOUCHER'
  | 'LOUNGE_ACCESS'
  | 'COMPENSATION'
  | 'UPGRADE'
  | 'FARE_DIFFERENCE'
  | 'COMPLAINT'
  | 'LEGAL_ESCALATION'
  | 'BOOKING_STATUS'
  | 'FLIGHT_STATUS'
  | 'GENERAL_INQUIRY';

export interface Customer {
  id: number;
  name: string;
  loyaltyTier: LoyaltyTier;
  bookingReference: string;
  email: string;
  maskedPhone: string;
  travelHistoryFlightsCount: number;
  priorComplaints: string;
}

export interface Flight {
  id: number;
  flightNumber: string;
  routeFrom: string;
  routeTo: string;
  flightDate: string;
  scheduledDeparture: string;
  status: FlightStatus;
  delayHours?: number;
  newDeparture?: string;
  cancellationReason?: string;
}

export interface Booking {
  id: number;
  pnr: string;
  customer: Customer;
  flight: Flight;
  bookingStatus: string;
  originalPaymentMethod: string;
  fareAmount: number;
  currency: string;
}

export interface PolicyRule {
  id: number;
  ruleCode: string;
  ruleName: string;
  category: string;
  conditionDescription: string;
  allowedAction: string;
  escalationCondition: string;
}

export interface PolicyDecision {
  ruleCode: string;
  ruleName: string;
  allowed: boolean;
  recommendedAction: ActionType;
  reason: string;
  policyReference: string;
  escalationReason?: string;
  details?: string;
}

export interface ActionResult {
  actionType: ActionType;
  status: ActionStatus;
  description: string;
  policyReference: string;
  escalationCreated: boolean;
  escalationReason?: string;
}

export interface AgentResponse {
  conversationId: number;
  customerName: string;
  loyaltyTier: string;
  pnr: string;
  flightNumber: string;
  flightStatus: string;
  route: string;
  detectedIntents: IntentType[];
  agentMessage: string;
  policyDecisions: PolicyDecision[];
  executedActions: ActionResult[];
  escalated: boolean;
  escalationReason?: string;
  timestamp: string;
}

export interface Message {
  id: number;
  sender: 'CUSTOMER' | 'AGENT' | 'SYSTEM';
  content: string;
  timestamp: string;
  detectedIntent?: string;
}

export interface ActionLog {
  id: number;
  conversationId?: number;
  pnr: string;
  customerName: string;
  actionType: ActionType;
  status: ActionStatus;
  reason: string;
  policyReference: string;
  timestamp: string;
}

export interface Escalation {
  id: number;
  conversationId?: number;
  pnr: string;
  customerName: string;
  reason: string;
  escalationType: string;
  status: string;
  createdAt: string;
}