import React from 'react';
import { Plane, Bot, ShieldCheck, FileText, Layers, Users, MessageSquare, RotateCcw } from 'lucide-react';

export type TabType = 'dashboard' | 'chat' | 'scenarios' | 'customers' | 'policies' | 'logs' | 'architecture';

interface NavbarProps {
  activeTab: TabType;
  setActiveTab: (tab: TabType) => void;
  onResetDemo: () => void;
  isResetting: boolean;
}

export const Navbar: React.FC<NavbarProps> = ({ activeTab, setActiveTab, onResetDemo, isResetting }) => {
  const tabs = [
    { id: 'dashboard' as TabType, label: 'Dashboard', icon: Layers },
    { id: 'chat' as TabType, label: 'Conversations', icon: MessageSquare },
    { id: 'scenarios' as TabType, label: 'Demo Scenarios', icon: Bot, badge: '3 Scenarios' },
    { id: 'customers' as TabType, label: 'Customers', icon: Users },
    { id: 'policies' as TabType, label: 'Policy Engine', icon: ShieldCheck },
    { id: 'logs' as TabType, label: 'Action Logs', icon: FileText },
    { id: 'architecture' as TabType, label: 'Architecture', icon: Plane }
  ];

  return (
    <header className="sticky top-0 z-50 bg-slate-900/95 border-b border-slate-800 backdrop-blur-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Brand */}
          <div className="flex items-center gap-3 cursor-pointer" onClick={() => setActiveTab('dashboard')}>
            <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-blue-600 to-indigo-500 flex items-center justify-center shadow-lg shadow-blue-500/25">
              <Plane className="w-5 h-5 text-white transform -rotate-45" />
            </div>
            <div>
              <div className="flex items-center gap-2">
                <span className="font-black text-lg tracking-wider text-white">AIONOS</span>
                <span className="text-[10px] uppercase font-bold tracking-widest px-1.5 py-0.5 bg-blue-500/20 text-blue-400 border border-blue-500/30 rounded">
                  Resolution Agent
                </span>
              </div>
              <p className="text-xs text-slate-400">Deterministic Airline Disruption Resolution</p>
            </div>
          </div>

          {/* Navigation Links */}
          <nav className="hidden md:flex items-center gap-1">
            {tabs.map(tab => {
              const Icon = tab.icon;
              const isActive = activeTab === tab.id;
              return (
                <button
                  key={tab.id}
                  onClick={() => setActiveTab(tab.id)}
                  className={`flex items-center gap-2 px-3 py-2 rounded-lg text-sm font-medium transition-all relative ${
                    isActive
                      ? 'bg-blue-600 text-white shadow-md shadow-blue-600/30'
                      : 'text-slate-300 hover:text-white hover:bg-slate-800'
                  }`}
                >
                  <Icon className={`w-4 h-4 ${isActive ? 'text-white' : 'text-slate-400'}`} />
                  <span>{tab.label}</span>
                  {tab.badge && (
                    <span className={`text-[10px] px-1.5 py-0.2 rounded-full font-bold ${
                      isActive ? 'bg-white/20 text-white' : 'bg-blue-900/60 text-blue-300 border border-blue-700/50'
                    }`}>
                      {tab.badge}
                    </span>
                  )}
                </button>
              );
            })}
          </nav>

          {/* Action buttons */}
          <div className="flex items-center gap-3">
            <div className="hidden sm:flex items-center gap-2 text-xs px-2.5 py-1 rounded-full bg-emerald-950/60 border border-emerald-600/40 text-emerald-400">
              <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
              <span>Backend Ready (Spring Boot 3.3)</span>
            </div>

            <button
              onClick={onResetDemo}
              disabled={isResetting}
              className="flex items-center gap-1.5 px-3 py-1.5 bg-slate-800 hover:bg-red-900/30 text-slate-300 hover:text-red-400 border border-slate-700 hover:border-red-600/40 rounded-lg text-xs font-semibold transition-all shadow-sm disabled:opacity-50"
              title="Reset H2 database to original assignment seed state"
            >
              <RotateCcw className={`w-3.5 h-3.5 ${isResetting ? 'animate-spin text-red-400' : ''}`} />
              <span>{isResetting ? 'Resetting...' : 'Reset Demo'}</span>
            </button>
          </div>
        </div>
      </div>
    </header>
  );
};