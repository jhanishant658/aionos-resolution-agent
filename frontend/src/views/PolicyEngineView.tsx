import React, { useState, useEffect } from 'react';
import { PolicyRule } from '../types';
import { getPolicies } from '../api';
import { ShieldCheck, CheckCircle2, XCircle, AlertTriangle, Scale, Lock, BookOpen } from 'lucide-react';

export const PolicyEngineView: React.FC = () => {
  const [policies, setPolicies] = useState<PolicyRule[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadPolicies();
  }, []);

  const loadPolicies = async () => {
    try {
      const data = await getPolicies();
      setPolicies(data);
    } catch (err) {
      console.error('Failed to load policies', err);
    } finally {
      setLoading(false);
    }
  };

  const allowedActions = [
    'Rebook the customer on the next available flight within 24 hours at no charge (airline-caused disruption)',
    'Issue meal vouchers and lounge access per the delay compensation rule',
    'Arrange hotel accommodation for the delayed-hours portion, where the delay qualifies (> 5 hours)',
    'Initiate a refund request for airline-caused cancellations to original payment method',
    'Provide the customer\'s own booking and flight status information'
  ];

  const prohibitedActions = [
    'Approving any compensation beyond the stated policy amounts (Must escalate to human agent)',
    'Waiving a fare difference above ₹1,500 without supervisor approval (Must escalate to human agent)',
    'Making exceptions for non-airline-caused disruptions (e.g., customer missed the flight) (Must escalate to human agent)',
    'Handling threats of legal action or formal complaints — must be escalated immediately',
    'Processing refunds to a different payment method than the original (Must escalate to human agent)'
  ];

  return (
    <div className="space-y-8">
      {/* Top Banner */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 pb-4 border-b border-slate-800">
        <div>
          <div className="flex items-center gap-2">
            <h1 className="text-2xl font-black text-white tracking-tight">Policy & Governance Engine</h1>
            <span className="text-xs px-2.5 py-0.5 rounded-full bg-emerald-950 text-emerald-400 border border-emerald-800/50 font-semibold flex items-center gap-1">
              <Lock className="w-3 h-3" /> Deterministic Authority
            </span>
          </div>
          <p className="text-xs text-slate-400 mt-1">
            Official airline service rules from Assignment Section 3 and 4. Evaluated purely by Java code without LLM hallucination risk.
          </p>
        </div>
      </div>

      {/* Allowed vs Prohibited Actions Matrix */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Allowed */}
        <div className="rounded-2xl bg-slate-900 border border-emerald-900/40 p-6 shadow-xl space-y-4">
          <div className="flex items-center gap-2.5 text-emerald-400 font-bold text-base">
            <div className="w-8 h-8 rounded-lg bg-emerald-950/80 border border-emerald-700/50 flex items-center justify-center">
              <CheckCircle2 className="w-5 h-5 text-emerald-400" />
            </div>
            <span>Allowed Agent Actions</span>
          </div>
          <p className="text-xs text-slate-400">
            Actions the autonomous resolution agent is authorized to execute immediately without human approval:
          </p>
          <ul className="space-y-2 text-xs text-slate-300">
            {allowedActions.map((act, i) => (
              <li key={i} className="flex items-start gap-2.5 p-2.5 rounded-lg bg-slate-950/60 border border-slate-800/80">
                <CheckCircle2 className="w-4 h-4 text-emerald-400 shrink-0 mt-0.5" />
                <span className="leading-relaxed">{act}</span>
              </li>
            ))}
          </ul>
        </div>

        {/* Prohibited */}
        <div className="rounded-2xl bg-slate-900 border border-red-900/40 p-6 shadow-xl space-y-4">
          <div className="flex items-center gap-2.5 text-red-400 font-bold text-base">
            <div className="w-8 h-8 rounded-lg bg-red-950/80 border border-red-700/50 flex items-center justify-center">
              <XCircle className="w-5 h-5 text-red-400" />
            </div>
            <span>Prohibited Actions (Mandatory Human Escalation)</span>
          </div>
          <p className="text-xs text-slate-400">
            Strict regulatory and financial boundaries. When detected, the agent MUST escalate to human supervision:
          </p>
          <ul className="space-y-2 text-xs text-slate-300">
            {prohibitedActions.map((act, i) => (
              <li key={i} className="flex items-start gap-2.5 p-2.5 rounded-lg bg-slate-950/60 border border-slate-800/80">
                <AlertTriangle className="w-4 h-4 text-red-400 shrink-0 mt-0.5" />
                <span className="leading-relaxed">{act}</span>
              </li>
            ))}
          </ul>
        </div>
      </div>

      {/* Service Rules Catalog */}
      <div className="rounded-2xl bg-slate-900 border border-slate-800 p-6 shadow-xl space-y-4">
        <div className="flex items-center justify-between pb-3 border-b border-slate-800">
          <div className="flex items-center gap-2">
            <BookOpen className="w-5 h-5 text-blue-400" />
            <h2 className="text-lg font-bold text-white">Assignment Service Rules Database</h2>
          </div>
          <span className="text-xs text-slate-400 font-mono">
            {policies.length} Active Deterministic Invariants
          </span>
        </div>

        {loading ? (
          <div className="py-8 text-center text-xs text-slate-500">Loading service policies...</div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {policies.map(p => (
              <div key={p.id} className="p-4 rounded-xl bg-slate-950 border border-slate-800 text-xs space-y-2">
                <div className="flex items-start justify-between">
                  <div>
                    <span className="text-[10px] font-mono uppercase text-blue-400 font-bold tracking-wider">
                      {p.ruleCode}
                    </span>
                    <h3 className="font-bold text-white text-sm mt-0.5">{p.ruleName}</h3>
                  </div>
                  <span className="text-[10px] px-2 py-0.5 rounded bg-slate-800 text-slate-300 border border-slate-700 font-medium">
                    {p.category}
                  </span>
                </div>

                <div className="pt-2 border-t border-slate-800/80 space-y-1.5">
                  <div>
                    <span className="text-slate-500 font-semibold block text-[11px]">Condition:</span>
                    <p className="text-slate-300 text-[11px]">{p.conditionDescription}</p>
                  </div>
                  <div>
                    <span className="text-emerald-400 font-semibold block text-[11px]">Allowed Action:</span>
                    <p className="text-slate-300 text-[11px]">{p.allowedAction}</p>
                  </div>
                  {p.escalationCondition && (
                    <div>
                      <span className="text-amber-400 font-semibold block text-[11px]">Escalation Condition:</span>
                      <p className="text-slate-400 text-[11px]">{p.escalationCondition}</p>
                    </div>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};