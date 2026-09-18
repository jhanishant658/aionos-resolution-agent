import React, { useState } from 'react';
import { Navbar, TabType } from './components/Navbar';
import { DashboardView } from './views/DashboardView';
import { ChatView } from './views/ChatView';
import { DemoScenariosView } from './views/DemoScenariosView';
import { CustomersView } from './views/CustomersView';
import { PolicyEngineView } from './views/PolicyEngineView';
import { ActionLogsView } from './views/ActionLogsView';
import { ArchitectureView } from './views/ArchitectureView';
import { resetDemoData } from './api';

export const App: React.FC = () => {
  const [activeTab, setActiveTab] = useState<TabType>('dashboard');
  const [selectedPnr, setSelectedPnr] = useState<string>('SK4821X'); // Default to Priya Nair (Scenario 1)
  const [isResetting, setIsResetting] = useState<boolean>(false);
  const [notification, setNotification] = useState<string | null>(null);

  const handleResetDemo = async () => {
    setIsResetting(true);
    try {
      const res = await resetDemoData();
      showNotification(res.message);
    } catch (err) {
      console.error('Failed to reset demo', err);
      showNotification('Failed to reset demo database. Make sure backend is running.');
    } finally {
      setIsResetting(false);
    }
  };

  const showNotification = (msg: string) => {
    setNotification(msg);
    setTimeout(() => {
      setNotification(null);
    }, 4000);
  };

  const handleSelectCustomer = (pnr: string) => {
    setSelectedPnr(pnr);
    setActiveTab('chat');
  };

  const handleLaunchScenario = (scenarioNum: number) => {
    if (scenarioNum === 1) setSelectedPnr('SK4821X');
    if (scenarioNum === 2) setSelectedPnr('TR1190B');
    if (scenarioNum === 3) setSelectedPnr('WL7742');
    setActiveTab('scenarios');
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col font-sans">
      <Navbar
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        onResetDemo={handleResetDemo}
        isResetting={isResetting}
      />

      {/* Global Notification Banner */}
      {notification && (
        <div className="bg-blue-600 text-white text-xs font-semibold py-2 px-4 text-center shadow-lg transition-all animate-fade-in">
          {notification}
        </div>
      )}

      {/* Main Content Area */}
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-6">
        {activeTab === 'dashboard' && (
          <DashboardView
            onSelectCustomer={handleSelectCustomer}
            onLaunchScenario={handleLaunchScenario}
            setActiveTab={setActiveTab}
          />
        )}

        {activeTab === 'chat' && (
          <ChatView
            selectedPnr={selectedPnr}
            onSelectPnr={setSelectedPnr}
          />
        )}

        {activeTab === 'scenarios' && (
          <DemoScenariosView
            onOpenChatWithPnr={handleSelectCustomer}
          />
        )}

        {activeTab === 'customers' && (
          <CustomersView
            onOpenChat={handleSelectCustomer}
          />
        )}

        {activeTab === 'policies' && (
          <PolicyEngineView />
        )}

        {activeTab === 'logs' && (
          <ActionLogsView />
        )}

        {activeTab === 'architecture' && (
          <ArchitectureView />
        )}
      </main>

      {/* Footer */}
      <footer className="bg-slate-900/60 border-t border-slate-800/80 py-4 text-center text-xs text-slate-500">
        <div className="max-w-7xl mx-auto px-4 flex flex-col sm:flex-row items-center justify-between gap-2">
          <span>AIONOS Customer-Facing Resolution Agent • Prototype Defense System</span>
          <span>Java 21 + Spring Boot 3.3 + React 18 + H2 Database</span>
        </div>
      </footer>
    </div>
  );
};