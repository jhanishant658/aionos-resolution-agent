import React from 'react';
import { 
  Cpu, ShieldCheck, Database, Layers, ArrowDown, 
  Bot, AlertTriangle, CheckCircle2, Lock, GitCommit 
} from 'lucide-react';

export const ArchitectureView: React.FC = () => {
  return (
    <div className="space-y-8 max-w-5xl mx-auto">
      {/* Top Banner */}
      <div className="pb-4 border-b border-slate-800">
        <div className="flex items-center gap-2">
          <h1 className="text-2xl font-black text-white tracking-tight">System Architecture</h1>
          <span className="text-xs px-2.5 py-0.5 rounded-full bg-blue-950 text-blue-400 border border-blue-800/50 font-semibold">
            Spring Boot 3.3 Modular Monolith
          </span>
        </div>
        <p className="text-xs text-slate-400 mt-1">
          Architectural overview of the AIONOS Customer-Facing Resolution Agent demonstrating the strict separation between AI communication and deterministic Java policy execution.
        </p>
      </div>

      {/* Visual Flow Diagram */}
      <div className="p-6 rounded-2xl bg-slate-900 border border-slate-800 shadow-xl space-y-6">
        <div className="flex items-center justify-between pb-3 border-b border-slate-800">
          <h2 className="text-base font-bold text-white flex items-center gap-2">
            <Layers className="w-4 h-4 text-blue-400" /> End-to-End Request Pipeline
          </h2>
          <span className="text-xs text-emerald-400 font-mono flex items-center gap-1">
            <Lock className="w-3.5 h-3.5" /> Deterministic Authority
          </span>
        </div>

        {/* Pipeline Nodes */}
        <div className="space-y-3 max-w-2xl mx-auto">
          {/* Node 1: Customer Input */}
          <div className="p-3.5 rounded-xl bg-slate-950 border border-slate-800 flex items-center justify-between">
            <div className="flex items-center gap-3">
              <span className="w-7 h-7 rounded-lg bg-blue-600/20 border border-blue-500/40 text-blue-400 font-bold text-xs flex items-center justify-center">1</span>
              <div>
                <h3 className="text-sm font-bold text-white">Passenger / User Interface</h3>
                <p className="text-[11px] text-slate-400">React + TypeScript + Tailwind UI (Vite)</p>
              </div>
            </div>
            <span className="text-[10px] px-2 py-0.5 rounded bg-slate-800 text-slate-400 font-mono">REST JSON</span>
          </div>

          <div className="flex justify-center text-slate-600"><ArrowDown className="w-4 h-4" /></div>

          {/* Node 2: REST Controller */}
          <div className="p-3.5 rounded-xl bg-slate-950 border border-slate-800 flex items-center justify-between">
            <div className="flex items-center gap-3">
              <span className="w-7 h-7 rounded-lg bg-blue-600/20 border border-blue-500/40 text-blue-400 font-bold text-xs flex items-center justify-center">2</span>
              <div>
                <h3 className="text-sm font-bold text-white">Spring Boot REST Controllers</h3>
                <p className="text-[11px] text-slate-400">AgentController, CustomerController, PolicyController, ActionLogController</p>
              </div>
            </div>
            <span className="text-[10px] px-2 py-0.5 rounded bg-slate-800 text-slate-400 font-mono">POST /api/agent/message</span>
          </div>

          <div className="flex justify-center text-slate-600"><ArrowDown className="w-4 h-4" /></div>

          {/* Node 3: Agent Orchestrator */}
          <div className="p-3.5 rounded-xl bg-slate-950 border border-blue-900/50 flex items-center justify-between">
            <div className="flex items-center gap-3">
              <span className="w-7 h-7 rounded-lg bg-blue-600 text-white font-bold text-xs flex items-center justify-center">3</span>
              <div>
                <h3 className="text-sm font-bold text-white">AgentService (Pipeline Orchestrator)</h3>
                <p className="text-[11px] text-slate-400">Manages conversation lifecycle, PNR resolution, and execution sequence</p>
              </div>
            </div>
            <span className="text-[10px] px-2 py-0.5 rounded bg-blue-950 text-blue-300 font-mono">Orchestration</span>
          </div>

          <div className="flex justify-center text-slate-600"><ArrowDown className="w-4 h-4" /></div>

          {/* Node 4: Intent & Data Lookup */}
          <div className="grid grid-cols-2 gap-3">
            <div className="p-3 rounded-xl bg-slate-950 border border-slate-800">
              <div className="flex items-center gap-2 mb-1">
                <Bot className="w-4 h-4 text-purple-400" />
                <h4 className="text-xs font-bold text-white">IntentService</h4>
              </div>
              <p className="text-[11px] text-slate-400">Classifies Cancellation, Delay, Refund, Upgrade, Hotel, Legal Escalation</p>
            </div>
            <div className="p-3 rounded-xl bg-slate-950 border border-slate-800">
              <div className="flex items-center gap-2 mb-1">
                <Database className="w-4 h-4 text-cyan-400" />
                <h4 className="text-xs font-bold text-white">Customer & Booking Data</h4>
              </div>
              <p className="text-[11px] text-slate-400">Extracts flight disruption status, loyalty tier, and prior travel history</p>
            </div>
          </div>

          <div className="flex justify-center text-slate-600"><ArrowDown className="w-4 h-4" /></div>

          {/* Node 5: PolicyEngine & DecisionEngine */}
          <div className="p-4 rounded-xl bg-gradient-to-r from-emerald-950/70 via-slate-950 to-blue-950/70 border border-emerald-600/50 shadow-lg">
            <div className="flex items-center justify-between mb-2">
              <div className="flex items-center gap-2">
                <ShieldCheck className="w-5 h-5 text-emerald-400" />
                <h3 className="text-sm font-bold text-white">Deterministic Java PolicyEngine & DecisionEngine</h3>
              </div>
              <span className="text-[10px] px-2 py-0.5 rounded bg-emerald-950 text-emerald-300 border border-emerald-700 font-bold">
                SOLE AUTHORITY
              </span>
            </div>
            <p className="text-xs text-slate-300 leading-relaxed">
              Strict deterministic Java business rules evaluate entitlement against exact assignment rules. LLMs are prohibited from granting compensation or waiving limits.
            </p>
          </div>

          <div className="flex justify-center text-slate-600"><ArrowDown className="w-4 h-4" /></div>

          {/* Node 6: Bifurcation (Allowed vs Prohibited) */}
          <div className="grid grid-cols-2 gap-3">
            <div className="p-3.5 rounded-xl bg-emerald-950/30 border border-emerald-800/60">
              <div className="flex items-center gap-2 mb-1 text-emerald-400 font-bold text-xs">
                <CheckCircle2 className="w-4 h-4" />
                <span>ActionService (Allowed)</span>
              </div>
              <p className="text-[11px] text-slate-400">Executes refund, issues ₹500 meal voucher, activates lounge access, books transit day room</p>
            </div>

            <div className="p-3.5 rounded-xl bg-red-950/30 border border-red-800/60">
              <div className="flex items-center gap-2 mb-1 text-red-400 font-bold text-xs">
                <AlertTriangle className="w-4 h-4" />
                <span>EscalationService (Prohibited)</span>
              </div>
              <p className="text-[11px] text-slate-400">Escalates ₹2,000 fare difference, cabin upgrades, full night hotel, legal threats to humans</p>
            </div>
          </div>

          <div className="flex justify-center text-slate-600"><ArrowDown className="w-4 h-4" /></div>

          {/* Node 7: AuditLogService & Database */}
          <div className="p-3.5 rounded-xl bg-slate-950 border border-slate-800 flex items-center justify-between">
            <div className="flex items-center gap-3">
              <span className="w-7 h-7 rounded-lg bg-purple-600/20 border border-purple-500/40 text-purple-400 font-bold text-xs flex items-center justify-center">7</span>
              <div>
                <h3 className="text-sm font-bold text-white">AuditLogService & H2 Database</h3>
                <p className="text-[11px] text-slate-400">Spring Data JPA repositories persist every action, reason, and policy reference</p>
              </div>
            </div>
            <span className="text-[10px] px-2 py-0.5 rounded bg-purple-950 text-purple-300 font-mono">Immutable Audit</span>
          </div>
        </div>
      </div>

      {/* Key Architectural Answers */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="p-6 rounded-2xl bg-slate-900 border border-slate-800 shadow-xl space-y-3">
          <h3 className="text-sm font-bold text-white flex items-center gap-2">
            <ShieldCheck className="w-4 h-4 text-emerald-400" />
            Why Separate PolicyEngine from AI?
          </h3>
          <p className="text-xs text-slate-300 leading-relaxed">
            In aviation customer service, financial and safety decisions must adhere to strict regulatory and fiduciary limits. Large Language Models are probabilistic and prone to hallucination under adversarial prompting (e.g. an angry customer demanding ₹50,000 cash or free first-class upgrades).
          </p>
          <p className="text-xs text-slate-400 leading-relaxed">
            By delegating all decision-making to the deterministic Java <code className="text-emerald-300">PolicyEngine</code>, business policy invariants are mathematically guaranteed to never be bypassed, while AI is utilized strictly for natural language synthesis and empathy.
          </p>
        </div>

        <div className="p-6 rounded-2xl bg-slate-900 border border-slate-800 shadow-xl space-y-3">
          <h3 className="text-sm font-bold text-white flex items-center gap-2">
            <Cpu className="w-4 h-4 text-blue-400" />
            Why Spring Boot Modular Monolith?
          </h3>
          <p className="text-xs text-slate-300 leading-relaxed">
            A modular monolith within a single deployable Spring Boot artifact provides exceptional developer productivity, zero network serialization latency between services, ACID transactional integrity across conversations and audit logs, and effortless local setup via H2.
          </p>
          <p className="text-xs text-slate-400 leading-relaxed">
            When scaling to enterprise production, each module (<code className="text-blue-300">PolicyEngine</code>, <code className="text-blue-300">ActionService</code>, <code className="text-blue-300">AuditLogService</code>) can seamlessly transition to microservices or Kafka event streaming without refactoring core business logic.
          </p>
        </div>
      </div>
    </div>
  );
};