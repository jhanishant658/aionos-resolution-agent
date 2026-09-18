import axios from 'axios';
import { Customer, Booking, Flight, PolicyRule, ActionLog, Escalation, AgentResponse, Message } from './types';

const API_BASE = '/api';

export const apiClient = axios.create({
  baseURL: API_BASE,
  headers: {
    'Content-Type': 'application/json'
  }
});

export const getCustomers = async (): Promise<Customer[]> => {
  const res = await apiClient.get<Customer[]>('/customers');
  return res.data;
};

export const getCustomerByPnr = async (pnr: string): Promise<Customer> => {
  const res = await apiClient.get<Customer>(`/customers/${pnr}`);
  return res.data;
};

export const getBookingsByPnr = async (pnr: string): Promise<Booking[]> => {
  const res = await apiClient.get<Booking[]>(`/bookings/${pnr}`);
  return res.data;
};

export const getFlights = async (): Promise<Flight[]> => {
  const res = await apiClient.get<Flight[]>('/flights');
  return res.data;
};

export const getFlightByNumber = async (flightNumber: string): Promise<Flight> => {
  const res = await apiClient.get<Flight>(`/flights/${flightNumber}`);
  return res.data;
};

export const getPolicies = async (): Promise<PolicyRule[]> => {
  const res = await apiClient.get<PolicyRule[]>('/policies');
  return res.data;
};

export const getActionLogs = async (): Promise<ActionLog[]> => {
  const res = await apiClient.get<ActionLog[]>('/action-logs');
  return res.data;
};

export const getActionLogsByPnr = async (pnr: string): Promise<ActionLog[]> => {
  const res = await apiClient.get<ActionLog[]>(`/action-logs/pnr/${pnr}`);
  return res.data;
};

export const getEscalations = async (): Promise<Escalation[]> => {
  const res = await apiClient.get<Escalation[]>('/escalations');
  return res.data;
};

export const getMessagesByConversation = async (conversationId: number): Promise<Message[]> => {
  const res = await apiClient.get<Message[]>(`/conversations/${conversationId}/messages`);
  return res.data;
};

export const sendAgentMessage = async (pnr: string, message: string, conversationId?: number): Promise<AgentResponse> => {
  const res = await apiClient.post<AgentResponse>('/agent/message', {
    pnr,
    message,
    conversationId
  });
  return res.data;
};

export const runDemoScenario = async (scenarioNumber: number): Promise<AgentResponse> => {
  const res = await apiClient.post<AgentResponse>(`/demo/scenario/${scenarioNumber}`);
  return res.data;
};

export const resetDemoData = async (): Promise<{ message: string }> => {
  const res = await apiClient.post<{ message: string }>('/demo/reset');
  return res.data;
};