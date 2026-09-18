import React from 'react';
import { CustomerBadge } from '../components/CustomerBadge';
import { Plane, AlertTriangle, ShieldCheck, CheckCircle2, ArrowRight, Clock, RefreshCw, Cpu } from 'lucide-react';
import { TabType } from '../components/Navbar';

interface Props {
  onSelectCustomer: (pnr: string) => void;
  onLaunchScenario: (scenarioNum: number) => void;
  setActiveTab: (tab: TabType) => void;
}

export const DashboardView: React.FC<Props> = ({ onSelectCustomer, onLaunchScenario, setActiveTab }) => {
  return (
    <div className="space-y-8">
      {/* Hero Banner */}
      <div className="relative overflow-hidden rounded-2xl bg-gradient-to-r from-blue-950 via-indigo-950 to-slate-900 border border-blue-900/50 p-6 sm:p-8 shadow-2xl">
        <div className="relative z-10 max-w-3xl">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-blue-500/20 text-blue-300 border border-blue-400/30 text-xs font-semibold mb-4">
            <ShieldCheck className="w-3.5 h-3.5" /> Deterministic Business Rules • Zero Hallucinations
          </div>
          <h1 className="text-3xl sm:text-4xl font-black text-white tracking-tight leading-tight">
            Autonomous Disruption Resolution Agent
          </h1>
          <p className="mt-3 text-sm sm:text-base text-slate-300 leading-relaxed">
            Engineered for high-stakes airline disruptions. Translates ambiguous passenger inquiries into deterministic policy evaluations, executes allowed remedies within milliseconds, and escalates out-of-policy exceptions directly to human supervisors.
          </p>
          <div className="mt-6 flex flex-wrap gap-3">
            <button
              onClick={() => setActiveTab('scenarios')}
              className="inline-flex items-center gap-2 px-5 py-2.5 rounded-xl bg-blue-600 hover:bg-blue-500 text-white font-semibold text-sm transition-all shadow-lg shadow-blue-600/30"
            >
              <span>Explore 3 Scenarios</span>
              <ArrowRight className="w-4 h-4" />
            </button>
            <button
              onClick={() => setActiveTab('chat')}
              className="inline-flex items-center gap-2 px-5 py-2.5 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-200 font-semibold text-sm border border-slate-700 transition-all"
            >
              <span>Open Live Chat</span>
            </button>
            <button
              onClick={() => setActiveTab('architecture')}
              className="inline-flex items-center gap-2 px-4 py-2.5 rounded-xl bg-indigo-950/60 hover:bg-indigo-900/60 text-indigo-300 font-semibold text-sm border border-indigo-700/40 transition-all"
            >
              <Cpu className="w-4 h-4" />
              <span>Backend Architecture</span>
            </button>
          </div>
        </div>
        <div className="absolute right-6 top-1/2 -translate-y-1/2 hidden lg:block opacity-15 pointer-events-none">
          <Plane className="w-80 h-80 text-blue-400 transform -rotate-12" />
        </div>
      </div>

      {/* Metrics Row */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <div className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 shadow-md">
          <div className="flex items-center justify-between text-slate-400 text-xs font-semibold uppercase tracking-wider">
            <span>Assignment Date</span>
            <Clock className="w-4 h-4 text-blue-400" />
          </div>
          <p className="mt-2 text-xl font-bold text-white">Wed, 23 Sep 2026</p>
          <p className="text-xs text-slate-500 mt-0.5">Fixed simulation anchor</p>
        </div>

        <div className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 shadow-md">
          <div className="flex items-center justify-between text-slate-400 text-xs font-semibold uppercase tracking-wider">
            <span>Disrupted Cases</span>
            <AlertTriangle className="w-4 h-4 text-amber-400" />
          </div>
          <p className="mt-2 text-xl font-bold text-amber-300">3 Verified Profiles</p>
          <p className="text-xs text-slate-500 mt-0.5">Priya, Arvind, Meher</p>
        </div>

        <div className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 shadow-md">
          <div className="flex items-center justify-between text-slate-400 text-xs font-semibold uppercase tracking-wider">
            <span>Policy Authority</span>
            <ShieldCheck className="w-4 h-4 text-emerald-400" />
          </div>
          <p className="mt-2 text-xl font-bold text-emerald-400">100% Deterministic</p>
          <p className="text-xs text-slate-500 mt-0.5">Java PolicyEngine in control</p>
        </div>

        <div className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 shadow-md">
          <div className="flex items-center justify-between text-slate-400 text-xs font-semibold uppercase tracking-wider">
            <span>Prohibited Action Guard</span>
            <CheckCircle2 className="w-4 h-4 text-purple-400" />
          </div>
          <p className="mt-2 text-xl font-bold text-purple-300">Zero Hallucination</p>
          <p className="text-xs text-slate-500 mt-0.5">Automated human escalation</p>
        </div>
      </div>

      {/* Disruption Flight Cards (The 3 Scenarios from the Assignment) */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <div>
            <h2 className="text-xl font-bold text-white">Active Disruption Scenarios</h2>
            <p className="text-xs text-slate-400">Assignment 3 test cases loaded into Spring Boot H2</p>
          </div>
          <span className="text-xs px-2.5 py-1 rounded bg-slate-800 text-slate-400 border border-slate-700">
            Source: Assignment Brief
          </span>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {/* Card 1: Priya Nair */}
          <div className="rounded-xl bg-slate-900 border border-slate-800 hover:border-amber-500/50 p-5 transition-all shadow-lg flex flex-col justify-between">
            <div>
              <div className="flex items-start justify-between">
                <div>
                  <CustomerBadge tier="GOLD" size="sm" />
                  <h3 className="mt-2 text-lg font-bold text-white">Priya Nair</h3>
                  <p className="text-xs font-mono text-slate-400">PNR: SK4821X</p>
                </div>
                <span className="px-2 py-0.5 text-[11px] font-bold rounded bg-red-950 text-red-400 border border-red-800/50 uppercase">
                  Cancelled
                </span>
              </div>

              <div className="mt-4 p-3 rounded-lg bg-slate-950/60 border border-slate-800/80 space-y-1.5 text-xs">
                <div className="flex justify-between text-slate-300">
                  <span className="text-slate-500">Flight:</span>
                  <span className="font-semibold text-white">SK-204 (Delhi → Goa)</span>
                </div>
                <div className="flex justify-between text-slate-300">
                  <span className="text-slate-500">Scheduled:</span>
                  <span>18:40 (Operational Reasons)</span>
                </div>
                <div className="flex justify-between text-slate-300">
                  <span className="text-slate-500">Return:</span>
                  <span className="text-emerald-400">Unaffected (Fri 25 Sep)</span>
                </div>
              </div>

              <div className="mt-4 text-xs text-slate-300 bg-amber-950/20 border border-amber-900/30 p-2.5 rounded-lg">
                <p className="font-semibold text-amber-300 mb-1">Customer Demands:</p>
                <p>• "Furious" customer</p>
                <p>• Full cash refund</p>
                <p>• Free Business Class upgrade on return</p>
              </div>
            </div>

            <div className="mt-5 pt-4 border-t border-slate-800 flex gap-2">
              <button
                onClick={() => onLaunchScenario(1)}
                className="flex-1 py-2 rounded-lg bg-amber-600 hover:bg-amber-500 text-white text-xs font-semibold transition-all shadow-md shadow-amber-900/20"
              >
                Run Scenario 1
              </button>
              <button
                onClick={() => onSelectCustomer('SK4821X')}
                className="px-3 py-2 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-300 text-xs font-medium border border-slate-700"
              >
                Open Chat
              </button>
            </div>
          </div>

          {/* Card 2: Arvind Kulkarni */}
          <div className="rounded-xl bg-slate-900 border border-slate-800 hover:border-blue-500/50 p-5 transition-all shadow-lg flex flex-col justify-between">
            <div>
              <div className="flex items-start justify-between">
                <div>
                  <CustomerBadge tier="SILVER" size="sm" />
                  <h3 className="mt-2 text-lg font-bold text-white">Arvind Kulkarni</h3>
                  <p className="text-xs font-mono text-slate-400">PNR: TR1190B</p>
                </div>
                <span className="px-2 py-0.5 text-[11px] font-bold rounded bg-amber-950 text-amber-400 border border-amber-800/50 uppercase">
                  Delayed 4h
                </span>
              </div>

              <div className="mt-4 p-3 rounded-lg bg-slate-950/60 border border-slate-800/80 space-y-1.5 text-xs">
                <div className="flex justify-between text-slate-300">
                  <span className="text-slate-500">Flight:</span>
                  <span className="font-semibold text-white">SK-118 (Mumbai → Bengaluru)</span>
                </div>
                <div className="flex justify-between text-slate-300">
                  <span className="text-slate-500">Scheduled:</span>
                  <span>07:10 (Revised: 11:10)</span>
                </div>
                <div className="flex justify-between text-slate-300">
                  <span className="text-slate-500">Delay Time:</span>
                  <span className="text-amber-400 font-semibold">4 Hours Delay</span>
                </div>
              </div>

              <div className="mt-4 text-xs text-slate-300 bg-blue-950/20 border border-blue-900/30 p-2.5 rounded-lg">
                <p className="font-semibold text-blue-300 mb-1">Customer Demands:</p>
                <p>• Missing connecting meeting</p>
                <p>• Asks for hotel accommodation</p>
                <p>• Entitled to meal voucher + lounge access</p>
              </div>
            </div>

            <div className="mt-5 pt-4 border-t border-slate-800 flex gap-2">
              <button
                onClick={() => onLaunchScenario(2)}
                className="flex-1 py-2 rounded-lg bg-blue-600 hover:bg-blue-500 text-white text-xs font-semibold transition-all shadow-md shadow-blue-900/20"
              >
                Run Scenario 2
              </button>
              <button
                onClick={() => onSelectCustomer('TR1190B')}
                className="px-3 py-2 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-300 text-xs font-medium border border-slate-700"
              >
                Open Chat
              </button>
            </div>
          </div>

          {/* Card 3: Meher Kaur */}
          <div className="rounded-xl bg-slate-900 border border-slate-800 hover:border-purple-500/50 p-5 transition-all shadow-lg flex flex-col justify-between">
            <div>
              <div className="flex items-start justify-between">
                <div>
                  <CustomerBadge tier="PLATINUM" size="sm" />
                  <h3 className="mt-2 text-lg font-bold text-white">Meher Kaur</h3>
                  <p className="text-xs font-mono text-slate-400">PNR: WL7742</p>
                </div>
                <span className="px-2 py-0.5 text-[11px] font-bold rounded bg-purple-950 text-purple-400 border border-purple-800/50 uppercase">
                  Delayed 6h
                </span>
              </div>

              <div className="mt-4 p-3 rounded-lg bg-slate-950/60 border border-slate-800/80 space-y-1.5 text-xs">
                <div className="flex justify-between text-slate-300">
                  <span className="text-slate-500">Flight:</span>
                  <span className="font-semibold text-white">SK-305 (Delhi → Hyderabad)</span>
                </div>
                <div className="flex justify-between text-slate-300">
                  <span className="text-slate-500">Scheduled:</span>
                  <span>14:00 (Revised: 20:00)</span>
                </div>
                <div className="flex justify-between text-slate-300">
                  <span className="text-slate-500">Delay Time:</span>
                  <span className="text-purple-400 font-semibold">6 Hours Delay</span>
                </div>
              </div>

              <div className="mt-4 text-xs text-slate-300 bg-purple-950/20 border border-purple-900/30 p-2.5 rounded-lg">
                <p className="font-semibold text-purple-300 mb-1">Customer Demands:</p>
                <p>• Full night hotel stay (beyond delayed-hours)</p>
                <p>• Move to higher-fare alternate flight</p>
                <p>• ₹2,000 fare difference waiver</p>
              </div>
            </div>

            <div className="mt-5 pt-4 border-t border-slate-800 flex gap-2">
              <button
                onClick={() => onLaunchScenario(3)}
                className="flex-1 py-2 rounded-lg bg-purple-600 hover:bg-purple-500 text-white text-xs font-semibold transition-all shadow-md shadow-purple-900/20"
              >
                Run Scenario 3
              </button>
              <button
                onClick={() => onSelectCustomer('WL7742')}
                className="px-3 py-2 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-300 text-xs font-medium border border-slate-700"
              >
                Open Chat
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};