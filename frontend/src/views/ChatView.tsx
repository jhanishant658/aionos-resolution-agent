import React, { useState, useEffect, useRef } from 'react';
import { Customer, Booking, PolicyDecision, ActionResult, ActionLog, IntentType } from '../types';
import { CustomerBadge } from '../components/CustomerBadge';
import { sendAgentMessage, getCustomerByPnr, getBookingsByPnr, getActionLogsByPnr } from '../api';
import { 
  Send, Bot, User, ShieldCheck, AlertOctagon, CheckCircle2, 
  XCircle, Clock, Plane, FileText, ArrowRight, Sparkles, AlertTriangle 
} from 'lucide-react';

interface Props {
  selectedPnr: string;
  onSelectPnr: (pnr: string) => void;
}

interface ChatMessage {
  id: string;
  sender: 'CUSTOMER' | 'AGENT' | 'SYSTEM';
  text: string;
  timestamp: string;
  intents?: IntentType[];
  actions?: ActionResult[];
  escalated?: boolean;
  escalationReason?: string;
}

export const ChatView: React.FC<Props> = ({ selectedPnr, onSelectPnr }) => {
  const [customer, setCustomer] = useState<Customer | null>(null);
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [inputText, setInputText] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [latestDecisions, setLatestDecisions] = useState<PolicyDecision[]>([]);
  const [latestActions, setLatestActions] = useState<ActionResult[]>([]);
  const [auditLogs, setAuditLogs] = useState<ActionLog[]>([]);

  const messagesEndRef = useRef<HTMLDivElement>(null);

  // Load customer and booking data when PNR changes
  useEffect(() => {
    loadCustomerData(selectedPnr);
  }, [selectedPnr]);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, isLoading]);

  const loadCustomerData = async (pnr: string) => {
    try {
      const cust = await getCustomerByPnr(pnr);
      const bks = await getBookingsByPnr(pnr);
      const logs = await getActionLogsByPnr(pnr);
      setCustomer(cust);
      setBookings(bks);
      setAuditLogs(logs);

      // Set initial greeting
      const primaryBooking = bks[0];
      setMessages([
        {
          id: 'welcome-' + pnr,
          sender: 'AGENT',
          text: `Hello ${cust.name}! I am your AIONOS Customer Resolution Agent. I have pulled up your booking (${pnr}) for flight ${primaryBooking?.flight?.flightNumber} (${primaryBooking?.flight?.routeFrom} → ${primaryBooking?.flight?.routeTo}). How can I assist you with your travel today?`,
          timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
        }
      ]);
      setLatestDecisions([]);
      setLatestActions([]);
    } catch (err) {
      console.error('Failed to load customer context', err);
    }
  };

  const handleSendMessage = async (textToSend?: string) => {
    const message = textToSend || inputText;
    if (!message.trim() || !customer) return;

    const userTimestamp = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

    // Add user message to UI
    const userMsgObj: ChatMessage = {
      id: 'user-' + Date.now(),
      sender: 'CUSTOMER',
      text: message,
      timestamp: userTimestamp
    };

    setMessages(prev => [...prev, userMsgObj]);
    setInputText('');
    setIsLoading(true);

    try {
      const response = await sendAgentMessage(customer.bookingReference, message);

      const agentMsgObj: ChatMessage = {
        id: 'agent-' + Date.now(),
        sender: 'AGENT',
        text: response.agentMessage,
        timestamp: new Date(response.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        intents: response.detectedIntents,
        actions: response.executedActions,
        escalated: response.escalated,
        escalationReason: response.escalationReason
      };

      setMessages(prev => [...prev, agentMsgObj]);
      setLatestDecisions(response.policyDecisions || []);
      setLatestActions(response.executedActions || []);

      // Refresh audit logs
      const updatedLogs = await getActionLogsByPnr(customer.bookingReference);
      setAuditLogs(updatedLogs);
    } catch (err) {
      console.error('Failed to send message', err);
      setMessages(prev => [
        ...prev,
        {
          id: 'err-' + Date.now(),
          sender: 'SYSTEM',
          text: 'Error processing your request. Please ensure the backend is running.',
          timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
        }
      ]);
    } finally {
      setIsLoading(false);
    }
  };

  const primaryBooking = bookings[0];
  const flight = primaryBooking?.flight;

  // Preset quick prompt buttons
  const quickPrompts = [
    {
      label: 'Priya Request (Scenario 1)',
      text: 'My flight SK-204 was cancelled! I am furious and want a full cash refund and a free upgrade to business class on my return flight.'
    },
    {
      label: 'Arvind Request (Scenario 2)',
      text: 'My flight SK-118 is delayed 4 hours and I am missing my meeting. Since it is such a long delay, please arrange hotel accommodation.'
    },
    {
      label: 'Meher Request (Scenario 3)',
      text: 'Flight SK-305 is delayed 6 hours. I want a full night hotel stay, and I want to move to another flight with ₹2,000 fare difference waived.'
    },
    {
      label: 'Legal Action Threat',
      text: 'This is completely unacceptable! I am going to contact my lawyer, file a formal complaint, and sue for damages.'
    }
  ];

  return (
    <div className="flex flex-col lg:flex-row gap-6 h-[calc(100vh-8rem)]">
      {/* LEFT / CENTER: CHAT AREA */}
      <div className="flex-1 flex flex-col bg-slate-900 rounded-2xl border border-slate-800 shadow-xl overflow-hidden">
        {/* Top Header */}
        <div className="p-4 bg-slate-950/70 border-b border-slate-800 flex flex-wrap items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-blue-600/20 border border-blue-500/30 flex items-center justify-center text-blue-400 font-bold">
              {customer?.name?.charAt(0) || 'P'}
            </div>
            <div>
              <div className="flex items-center gap-2">
                <span className="font-bold text-white text-base">{customer?.name || 'Loading...'}</span>
                {customer && <CustomerBadge tier={customer.loyaltyTier} size="sm" />}
              </div>
              <p className="text-xs text-slate-400 font-mono">
                PNR: <span className="text-slate-200 font-semibold">{customer?.bookingReference}</span> • 
                Flight: <span className="text-blue-400 font-semibold">{flight?.flightNumber}</span> ({flight?.routeFrom} → {flight?.routeTo})
              </p>
            </div>
          </div>

          {/* Passenger Selector Switcher */}
          <div className="flex items-center gap-1.5 bg-slate-900 p-1 rounded-xl border border-slate-800">
            <span className="text-[11px] text-slate-400 px-2 font-medium">Passenger:</span>
            {[
              { pnr: 'SK4821X', label: 'Priya (Gold)' },
              { pnr: 'TR1190B', label: 'Arvind (Silver)' },
              { pnr: 'WL7742', label: 'Meher (Platinum)' }
            ].map(p => (
              <button
                key={p.pnr}
                onClick={() => onSelectPnr(p.pnr)}
                className={`text-xs px-2.5 py-1 rounded-lg font-medium transition-all ${
                  selectedPnr === p.pnr
                    ? 'bg-blue-600 text-white shadow-sm'
                    : 'text-slate-400 hover:text-white hover:bg-slate-800'
                }`}
              >
                {p.label}
              </button>
            ))}
          </div>
        </div>

        {/* Quick-Prompt Suggestions Bar */}
        <div className="px-4 py-2 bg-slate-950/40 border-b border-slate-800/60 flex items-center gap-2 overflow-x-auto text-xs">
          <span className="text-slate-500 flex items-center gap-1 shrink-0 font-medium">
            <Sparkles className="w-3.5 h-3.5 text-amber-400" /> Test Prompts:
          </span>
          {quickPrompts.map((qp, idx) => (
            <button
              key={idx}
              onClick={() => handleSendMessage(qp.text)}
              disabled={isLoading}
              className="shrink-0 px-2.5 py-1 rounded-md bg-slate-800/90 hover:bg-blue-950/50 text-slate-300 hover:text-blue-300 border border-slate-700/60 hover:border-blue-600/40 transition-all font-normal"
            >
              {qp.label}
            </button>
          ))}
        </div>

        {/* Message Stream */}
        <div className="flex-1 p-4 overflow-y-auto space-y-4">
          {messages.map(msg => {
            const isAgent = msg.sender === 'AGENT';
            const isSystem = msg.sender === 'SYSTEM';

            if (isSystem) {
              return (
                <div key={msg.id} className="flex justify-center my-2">
                  <span className="px-3 py-1 rounded-full bg-red-950/60 border border-red-800/40 text-red-300 text-xs font-mono">
                    {msg.text}
                  </span>
                </div>
              );
            }

            return (
              <div
                key={msg.id}
                className={`flex gap-3 max-w-[85%] ${isAgent ? 'mr-auto' : 'ml-auto flex-row-reverse'}`}
              >
                <div className={`w-8 h-8 rounded-full flex items-center justify-center shrink-0 text-xs font-bold ${
                  isAgent
                    ? 'bg-gradient-to-tr from-blue-600 to-indigo-600 text-white shadow-md shadow-blue-500/20'
                    : 'bg-slate-700 text-slate-200'
                }`}>
                  {isAgent ? <Bot className="w-4 h-4" /> : <User className="w-4 h-4" />}
                </div>

                <div className="space-y-1.5">
                  <div className={`p-4 rounded-2xl text-sm leading-relaxed shadow-md ${
                    isAgent
                      ? 'bg-slate-800 text-slate-100 border border-slate-700/70 rounded-tl-sm'
                      : 'bg-blue-600 text-white rounded-tr-sm shadow-blue-600/20'
                  }`}>
                    <p className="whitespace-pre-line">{msg.text}</p>
                  </div>

                  {/* Intent & Action badges for Agent responses */}
                  {isAgent && (
                    <div className="flex flex-wrap items-center gap-1.5 text-[11px] pt-1">
                      {msg.intents && msg.intents.map((intent, i) => (
                        <span key={i} className="px-2 py-0.5 rounded bg-slate-800 text-blue-400 border border-slate-700 font-mono">
                          Intent: {intent}
                        </span>
                      ))}

                      {msg.actions && msg.actions.map((act, i) => (
                        <span
                          key={i}
                          className={`px-2 py-0.5 rounded font-medium border ${
                            act.status === 'SUCCESS'
                              ? 'bg-emerald-950/60 text-emerald-300 border-emerald-700/50'
                              : act.status === 'ESCALATED'
                              ? 'bg-amber-950/60 text-amber-300 border-amber-700/50'
                              : act.status === 'BLOCKED_BY_POLICY'
                              ? 'bg-red-950/60 text-red-300 border-red-700/50'
                              : 'bg-slate-800 text-slate-400 border-slate-700'
                          }`}
                        >
                          {act.actionType} ({act.status})
                        </span>
                      ))}

                      {msg.escalated && (
                        <span className="px-2 py-0.5 rounded bg-red-950/80 text-red-300 border border-red-700/50 font-semibold flex items-center gap-1">
                          <AlertTriangle className="w-3 h-3" />
                          Escalated to Human Supervisor
                        </span>
                      )}

                      <span className="text-slate-500 text-[10px] ml-auto">{msg.timestamp}</span>
                    </div>
                  )}

                  {!isAgent && (
                    <div className="text-right text-slate-500 text-[10px]">{msg.timestamp}</div>
                  )}
                </div>
              </div>
            );
          })}

          {isLoading && (
            <div className="flex gap-3 max-w-[85%] mr-auto">
              <div className="w-8 h-8 rounded-full bg-blue-600 flex items-center justify-center shrink-0 text-white">
                <Bot className="w-4 h-4 animate-spin" />
              </div>
              <div className="p-3.5 rounded-2xl bg-slate-800 border border-slate-700 text-slate-300 text-xs flex items-center gap-2">
                <span className="w-2 h-2 rounded-full bg-blue-400 animate-ping"></span>
                <span>Evaluating policies & checking deterministic rules...</span>
              </div>
            </div>
          )}

          <div ref={messagesEndRef} />
        </div>

        {/* Input Bar */}
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleSendMessage();
          }}
          className="p-3 bg-slate-950/80 border-t border-slate-800 flex items-center gap-2"
        >
          <input
            type="text"
            value={inputText}
            onChange={(e) => setInputText(e.target.value)}
            placeholder={`Message as ${customer?.name || 'passenger'} (e.g. "I want a refund", "Can I get hotel accommodation?")...`}
            disabled={isLoading}
            className="flex-1 bg-slate-900 border border-slate-700 rounded-xl px-4 py-2.5 text-sm text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50"
          />
          <button
            type="submit"
            disabled={isLoading || !inputText.trim()}
            className="px-4 py-2.5 rounded-xl bg-blue-600 hover:bg-blue-500 disabled:opacity-40 text-white font-semibold text-sm flex items-center gap-1.5 transition-all shadow-md shadow-blue-600/30"
          >
            <span>Send</span>
            <Send className="w-4 h-4" />
          </button>
        </form>
      </div>

      {/* RIGHT SIDE PANELS */}
      <div className="w-full lg:w-96 flex flex-col gap-4 overflow-y-auto">
        {/* PANEL 1: CUSTOMER CONTEXT */}
        <div className="p-4 rounded-xl bg-slate-900 border border-slate-800 shadow-md">
          <div className="flex items-center justify-between pb-2 mb-3 border-b border-slate-800">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider flex items-center gap-1.5">
              <User className="w-3.5 h-3.5 text-blue-400" /> Customer Context
            </span>
            {customer && <CustomerBadge tier={customer.loyaltyTier} size="sm" />}
          </div>

          <div className="space-y-2 text-xs">
            <div className="flex justify-between">
              <span className="text-slate-500">Customer:</span>
              <span className="font-semibold text-white">{customer?.name}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-slate-500">PNR / Reference:</span>
              <span className="font-mono text-blue-400 font-semibold">{customer?.bookingReference}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-slate-500">Flight / Route:</span>
              <span className="text-white font-semibold">
                {flight?.flightNumber} ({flight?.routeFrom} → {flight?.routeTo})
              </span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-slate-500">Status:</span>
              <span className={`px-2 py-0.5 rounded text-[10px] font-bold ${
                flight?.status === 'CANCELLED'
                  ? 'bg-red-950 text-red-400 border border-red-800/40'
                  : flight?.status === 'DELAYED'
                  ? 'bg-amber-950 text-amber-400 border border-amber-800/40'
                  : 'bg-emerald-950 text-emerald-400'
              }`}>
                {flight?.status} {flight?.delayHours ? `(${flight.delayHours}h)` : ''}
              </span>
            </div>
            {flight?.newDeparture && (
              <div className="flex justify-between">
                <span className="text-slate-500">New Departure:</span>
                <span className="text-amber-300 font-semibold">{flight.newDeparture}</span>
              </div>
            )}
            <div className="flex justify-between">
              <span className="text-slate-500">Payment Method:</span>
              <span className="text-slate-300">{primaryBooking?.originalPaymentMethod} (₹{primaryBooking?.fareAmount})</span>
            </div>
            <div className="pt-2 border-t border-slate-800 text-[11px] text-slate-400">
              <p><span className="text-slate-500">Travel History:</span> {customer?.travelHistoryFlightsCount} flights (12m)</p>
              <p className="mt-1"><span className="text-slate-500">Prior Complaints:</span> {customer?.priorComplaints}</p>
            </div>
          </div>
        </div>

        {/* PANEL 2: POLICY DECISION */}
        <div className="p-4 rounded-xl bg-slate-900 border border-slate-800 shadow-md">
          <div className="flex items-center justify-between pb-2 mb-3 border-b border-slate-800">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider flex items-center gap-1.5">
              <ShieldCheck className="w-3.5 h-3.5 text-emerald-400" /> Policy Decision
            </span>
            <span className="text-[10px] font-mono px-1.5 py-0.5 rounded bg-blue-950 text-blue-400 border border-blue-800/40">
              Deterministic Java Engine
            </span>
          </div>

          {latestDecisions.length === 0 ? (
            <div className="py-6 text-center text-xs text-slate-500">
              Send a message to trigger real-time policy evaluation.
            </div>
          ) : (
            <div className="space-y-3">
              {latestDecisions.map((dec, idx) => (
                <div key={idx} className="p-3 rounded-lg bg-slate-950 border border-slate-800 text-xs space-y-1.5">
                  <div className="flex items-start justify-between gap-2">
                    <span className="font-bold text-slate-200">{dec.ruleName}</span>
                    <span className={`px-2 py-0.5 rounded text-[10px] font-bold ${
                      dec.allowed
                        ? 'bg-emerald-950 text-emerald-400 border border-emerald-800/40'
                        : 'bg-amber-950 text-amber-400 border border-amber-800/40'
                    }`}>
                      {dec.allowed ? 'ALLOWED' : 'ESCALATED / BLOCKED'}
                    </span>
                  </div>
                  <p className="text-slate-400 text-[11px] leading-relaxed">{dec.reason}</p>
                  <p className="text-[10px] text-slate-500 font-mono pt-1 border-t border-slate-800/60">
                    Ref: {dec.policyReference}
                  </p>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* PANEL 3: ACTION LOG */}
        <div className="flex-1 p-4 rounded-xl bg-slate-900 border border-slate-800 shadow-md flex flex-col">
          <div className="flex items-center justify-between pb-2 mb-3 border-b border-slate-800">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider flex items-center gap-1.5">
              <FileText className="w-3.5 h-3.5 text-purple-400" /> Live Action Audit
            </span>
            <span className="text-[10px] text-slate-500 font-mono">
              {auditLogs.length} Events
            </span>
          </div>

          <div className="flex-1 overflow-y-auto space-y-2 pr-1 text-xs">
            {auditLogs.length === 0 ? (
              <p className="text-slate-500 text-center py-6">No actions logged yet.</p>
            ) : (
              auditLogs.slice(0, 10).map(log => (
                <div key={log.id} className="p-2.5 rounded-lg bg-slate-950/80 border border-slate-800/80 text-[11px] space-y-1">
                  <div className="flex items-center justify-between">
                    <span className="font-semibold text-slate-200 flex items-center gap-1">
                      {log.status === 'SUCCESS' && <CheckCircle2 className="w-3 h-3 text-emerald-400" />}
                      {log.status === 'ESCALATED' && <AlertTriangle className="w-3 h-3 text-amber-400" />}
                      {log.status === 'BLOCKED_BY_POLICY' && <XCircle className="w-3 h-3 text-red-400" />}
                      {log.status === 'INFORMATION_ONLY' && <Clock className="w-3 h-3 text-blue-400" />}
                      {log.actionType}
                    </span>
                    <span className="text-[10px] text-slate-500">
                      {new Date(log.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' })}
                    </span>
                  </div>
                  <p className="text-slate-400">{log.reason}</p>
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
};