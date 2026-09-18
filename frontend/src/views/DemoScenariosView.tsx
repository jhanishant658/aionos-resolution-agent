import React, { useState } from 'react';
import { CustomerBadge } from '../components/CustomerBadge';
import { runDemoScenario, resetDemoData } from '../api';
import { AgentResponse } from '../types';
import { 
  Play, RotateCcw, ShieldCheck, CheckCircle2, AlertTriangle, 
  XCircle, ArrowRight, User, Plane, Bot, Sparkles 
} from 'lucide-react';

interface Props {
  onOpenChatWithPnr: (pnr: string) => void;
}

export const DemoScenariosView: React.FC<Props> = ({ onOpenChatWithPnr }) => {
  const [activeScenario, setActiveScenario] = useState<number | null>(null);
  const [scenarioResult, setScenarioResult] = useState<AgentResponse | null>(null);
  const [isRunning, setIsRunning] = useState(false);
  const [resetMessage, setResetMessage] = useState<string | null>(null);

  const scenarios = [
    {
      num: 1,
      passenger: 'Priya Nair',
      tier: 'GOLD',
      pnr: 'SK4821X',
      flight: 'SK-204',
      route: 'Delhi → Goa',
      disruption: 'Cancelled (Operational reasons)',
      badgeColor: 'amber',
      objective: 'Cancellation + Full Refund + Free Business-Class Upgrade Request',
      customerPrompt: 'My flight SK-204 from Delhi to Goa was cancelled! I am furious about this. I want a full cash refund for my booking and a free upgrade to business class on my return flight for all the trouble!',
      expectedOutcome: [
        'Acknowledge anger with empathy without overpromising',
        'Verify airline-caused cancellation on flight SK-204',
        'Approve full refund of ₹7,500 to original payment method (Credit Card) within 7 business days',
        'Enforce Loyalty Tier Policy: Gold tier gets priority rebooking, NOT free class upgrades',
        'Escalate Business Class upgrade to human supervisor (Prohibited for agent to approve)'
      ]
    },
    {
      num: 2,
      passenger: 'Arvind Kulkarni',
      tier: 'SILVER',
      pnr: 'TR1190B',
      flight: 'SK-118',
      route: 'Mumbai → Bengaluru',
      disruption: 'Delayed 4 Hours (07:10 → 11:10)',
      badgeColor: 'blue',
      objective: '4-Hour Delay + Missing Connecting Meeting + Hotel Request',
      customerPrompt: 'My flight SK-118 to Bengaluru is delayed by 4 hours. I\'m very frustrated because I am missing an important connecting meeting. Since it\'s been such a long delay, I ask for hotel accommodation.',
      expectedOutcome: [
        'Acknowledge frustration regarding missing connecting meeting',
        'Verify 4-hour delay on flight SK-118 (new departure 11:10)',
        'Check Delay Compensation Rule (> 3 hours): Issue ₹500 meal voucher + lounge access',
        'Check Hotel Policy: Hotel requires delay > 5 hours; 4h delay is DISQUALIFIED',
        'Deny hotel accommodation strictly per policy and invite customer to airport lounge'
      ]
    },
    {
      num: 3,
      passenger: 'Meher Kaur',
      tier: 'PLATINUM',
      pnr: 'WL7742',
      flight: 'SK-305',
      route: 'Delhi → Hyderabad',
      disruption: 'Delayed 6 Hours (14:00 → 20:00)',
      badgeColor: 'purple',
      objective: '6-Hour Delay + Full Night Hotel Stay + ₹2,000 Fare Difference Waiver',
      customerPrompt: 'My flight SK-305 is delayed 6 hours. I would like a full night\'s hotel stay rather than coverage for just the delayed hours. Also, I would like to be moved onto a different, higher-fare flight instead of waiting — the fare difference for that flight is ₹2,000.',
      expectedOutcome: [
        'Verify 6-hour delay on flight SK-305 (rescheduled to 20:00)',
        'Delay > 5 hours qualifies for day-use hotel room covering delayed hours only',
        'Full night stay request exceeds agent authority -> Escalate to supervisor',
        'Evaluate ₹2,000 fare difference waiver: Agent waiver limit is ₹1,500 max',
        'Escalate ₹2,000 fare difference waiver to supervisor (Exceeds authority)',
        'Issue meal voucher and airport lounge access'
      ]
    }
  ];

  const handleRunScenario = async (scenarioNum: number) => {
    setIsRunning(true);
    setActiveScenario(scenarioNum);
    setScenarioResult(null);
    setResetMessage(null);

    try {
      const result = await runDemoScenario(scenarioNum);
      setScenarioResult(result);
    } catch (err) {
      console.error('Failed to run scenario', err);
    } finally {
      setIsRunning(false);
    }
  };

  const handleReset = async () => {
    try {
      const res = await resetDemoData();
      setResetMessage(res.message);
      setScenarioResult(null);
      setActiveScenario(null);
      setTimeout(() => setResetMessage(null), 4000);
    } catch (err) {
      console.error('Failed to reset', err);
    }
  };

  return (
    <div className="space-y-8">
      {/* Top Header */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 pb-4 border-b border-slate-800">
        <div>
          <div className="flex items-center gap-2">
            <h1 className="text-2xl font-black text-white tracking-tight">Assignment Demo Runner</h1>
            <span className="text-xs px-2.5 py-0.5 rounded-full bg-blue-950 text-blue-400 border border-blue-800/50 font-semibold">
              Evaluation Mode
            </span>
          </div>
          <p className="text-xs text-slate-400 mt-1">
            Execute and inspect all three mandatory assignment test cases with real deterministic verification.
          </p>
        </div>

        <button
          onClick={handleReset}
          className="flex items-center gap-2 px-4 py-2 bg-slate-800 hover:bg-red-950/40 text-slate-300 hover:text-red-300 border border-slate-700 hover:border-red-600/40 rounded-xl text-xs font-semibold transition-all shadow-sm"
        >
          <RotateCcw className="w-4 h-4 text-red-400" />
          <span>Reset All Demo Data</span>
        </button>
      </div>

      {resetMessage && (
        <div className="p-3 bg-emerald-950/60 border border-emerald-600/50 rounded-xl text-emerald-300 text-xs flex items-center gap-2">
          <CheckCircle2 className="w-4 h-4" />
          <span>{resetMessage}</span>
        </div>
      )}

      {/* Scenario Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {scenarios.map(sc => {
          const isSelected = activeScenario === sc.num;
          return (
            <div
              key={sc.num}
              className={`rounded-2xl bg-slate-900 border transition-all p-5 flex flex-col justify-between shadow-xl ${
                isSelected
                  ? 'border-blue-500 ring-2 ring-blue-500/20 shadow-blue-500/10'
                  : 'border-slate-800 hover:border-slate-700'
              }`}
            >
              <div>
                <div className="flex items-start justify-between">
                  <div>
                    <span className="text-[10px] font-bold uppercase tracking-wider text-blue-400">
                      Scenario {sc.num}
                    </span>
                    <h2 className="text-lg font-bold text-white mt-0.5">{sc.passenger}</h2>
                    <p className="text-xs font-mono text-slate-400">PNR: {sc.pnr}</p>
                  </div>
                  <CustomerBadge tier={sc.tier} size="sm" />
                </div>

                <div className="mt-3 p-2.5 rounded-lg bg-slate-950/70 border border-slate-800 text-xs space-y-1">
                  <div className="flex justify-between">
                    <span className="text-slate-500">Flight:</span>
                    <span className="text-slate-200 font-semibold">{sc.flight} ({sc.route})</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-slate-500">Disruption:</span>
                    <span className="text-amber-400 font-medium">{sc.disruption}</span>
                  </div>
                </div>

                <div className="mt-4 text-xs space-y-2">
                  <div>
                    <p className="font-semibold text-slate-300 mb-1">Customer Input:</p>
                    <p className="italic text-slate-400 text-[11px] bg-slate-950/40 p-2 rounded border border-slate-800/60">
                      "{sc.customerPrompt}"
                    </p>
                  </div>

                  <div>
                    <p className="font-semibold text-slate-300 mb-1">Mandated Invariants:</p>
                    <ul className="space-y-1 text-[11px] text-slate-400">
                      {sc.expectedOutcome.map((item, idx) => (
                        <li key={idx} className="flex items-start gap-1.5">
                          <span className="text-blue-400 font-bold shrink-0">•</span>
                          <span>{item}</span>
                        </li>
                      ))}
                    </ul>
                  </div>
                </div>
              </div>

              <div className="mt-6 pt-4 border-t border-slate-800 flex gap-2">
                <button
                  onClick={() => handleRunScenario(sc.num)}
                  disabled={isRunning}
                  className={`flex-1 py-2.5 rounded-xl font-bold text-xs flex items-center justify-center gap-1.5 transition-all shadow-md ${
                    isSelected
                      ? 'bg-blue-600 hover:bg-blue-500 text-white shadow-blue-600/30'
                      : 'bg-slate-800 hover:bg-slate-700 text-white'
                  }`}
                >
                  <Play className="w-3.5 h-3.5 fill-current" />
                  <span>{isRunning && isSelected ? 'Evaluating...' : `Run Scenario ${sc.num}`}</span>
                </button>

                <button
                  onClick={() => onOpenChatWithPnr(sc.pnr)}
                  className="px-3 py-2.5 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-300 text-xs font-medium border border-slate-700"
                  title="Open live interactive chat with this customer"
                >
                  Chat
                </button>
              </div>
            </div>
          );
        })}
      </div>

      {/* Execution Results Viewer */}
      {scenarioResult && (
        <div className="rounded-2xl bg-slate-900 border border-blue-900/60 p-6 shadow-2xl space-y-6">
          <div className="flex items-center justify-between pb-4 border-b border-slate-800">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-blue-600/20 border border-blue-500/30 flex items-center justify-center text-blue-400">
                <ShieldCheck className="w-6 h-6" />
              </div>
              <div>
                <div className="flex items-center gap-2">
                  <h3 className="text-lg font-bold text-white">
                    Scenario {activeScenario} Execution Result: {scenarioResult.customerName}
                  </h3>
                  <CustomerBadge tier={scenarioResult.loyaltyTier} size="sm" />
                </div>
                <p className="text-xs text-slate-400">
                  PNR: {scenarioResult.pnr} • Flight: {scenarioResult.flightNumber} ({scenarioResult.route}) • Status: {scenarioResult.flightStatus}
                </p>
              </div>
            </div>

            {scenarioResult.escalated ? (
              <span className="px-3 py-1 rounded-full bg-amber-950/80 border border-amber-600/50 text-amber-300 text-xs font-semibold flex items-center gap-1.5">
                <AlertTriangle className="w-4 h-4 text-amber-400" />
                Escalated to Human Supervisor
              </span>
            ) : (
              <span className="px-3 py-1 rounded-full bg-emerald-950/80 border border-emerald-600/50 text-emerald-300 text-xs font-semibold flex items-center gap-1.5">
                <CheckCircle2 className="w-4 h-4 text-emerald-400" />
                Resolved within Agent Policy
              </span>
            )}
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {/* Left: Policy & Action Trace */}
            <div className="space-y-4">
              <h4 className="text-xs font-bold uppercase tracking-wider text-slate-400 flex items-center gap-1.5">
                <ShieldCheck className="w-4 h-4 text-blue-400" /> Deterministic Policy Evaluation
              </h4>

              <div className="space-y-2">
                {scenarioResult.policyDecisions.map((dec, i) => (
                  <div key={i} className="p-3 rounded-xl bg-slate-950 border border-slate-800 text-xs space-y-1">
                    <div className="flex items-center justify-between">
                      <span className="font-bold text-white">{dec.ruleName}</span>
                      <span className={`px-2 py-0.5 rounded text-[10px] font-bold ${
                        dec.allowed ? 'bg-emerald-950 text-emerald-400' : 'bg-amber-950 text-amber-400'
                      }`}>
                        {dec.allowed ? 'ALLOWED' : 'PROHIBITED / ESCALATED'}
                      </span>
                    </div>
                    <p className="text-slate-400 text-[11px]">{dec.reason}</p>
                    <p className="text-[10px] text-slate-500 font-mono">Rule Ref: {dec.policyReference}</p>
                  </div>
                ))}
              </div>

              <h4 className="text-xs font-bold uppercase tracking-wider text-slate-400 flex items-center gap-1.5 pt-2">
                <CheckCircle2 className="w-4 h-4 text-emerald-400" /> Executed Actions Checklist
              </h4>

              <div className="space-y-2">
                {scenarioResult.executedActions.map((act, i) => (
                  <div key={i} className="p-3 rounded-xl bg-slate-950 border border-slate-800 text-xs space-y-1">
                    <div className="flex items-center justify-between">
                      <span className="font-semibold text-slate-200 flex items-center gap-1.5">
                        {act.status === 'SUCCESS' && <CheckCircle2 className="w-3.5 h-3.5 text-emerald-400" />}
                        {act.status === 'ESCALATED' && <AlertTriangle className="w-3.5 h-3.5 text-amber-400" />}
                        {act.status === 'BLOCKED_BY_POLICY' && <XCircle className="w-3.5 h-3.5 text-red-400" />}
                        {act.actionType}
                      </span>
                      <span className="text-[10px] px-2 py-0.5 rounded bg-slate-800 text-slate-300 font-mono">
                        {act.status}
                      </span>
                    </div>
                    <p className="text-slate-400 text-[11px]">{act.description}</p>
                  </div>
                ))}
              </div>
            </div>

            {/* Right: Generated Customer Response */}
            <div className="flex flex-col h-full space-y-3">
              <h4 className="text-xs font-bold uppercase tracking-wider text-slate-400 flex items-center gap-1.5">
                <Bot className="w-4 h-4 text-purple-400" /> Agent Customer-Facing Response
              </h4>

              <div className="flex-1 p-5 rounded-xl bg-slate-950 border border-slate-800 text-slate-200 text-sm leading-relaxed whitespace-pre-line shadow-inner">
                {scenarioResult.agentMessage}
              </div>

              <div className="p-3 rounded-xl bg-blue-950/30 border border-blue-900/40 text-xs text-slate-300 flex items-center justify-between">
                <span>Would you like to test custom follow-up queries with this passenger?</span>
                <button
                  onClick={() => onOpenChatWithPnr(scenarioResult.pnr)}
                  className="px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-500 text-white font-medium flex items-center gap-1 shadow-sm"
                >
                  <span>Open Live Chat</span>
                  <ArrowRight className="w-3.5 h-3.5" />
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};