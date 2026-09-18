import React, { useState, useEffect } from 'react';
import { ActionLog } from '../types';
import { getActionLogs } from '../api';
import { 
  FileText, Search, Filter, CheckCircle2, AlertTriangle, 
  XCircle, Clock, RefreshCw, Download 
} from 'lucide-react';

export const ActionLogsView: React.FC = () => {
  const [logs, setLogs] = useState<ActionLog[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('ALL');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadLogs();
  }, []);

  const loadLogs = async () => {
    setLoading(true);
    try {
      const data = await getActionLogs();
      setLogs(data);
    } catch (err) {
      console.error('Failed to load action logs', err);
    } finally {
      setLoading(false);
    }
  };

  const filteredLogs = logs.filter(log => {
    const matchesSearch = 
      log.customerName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      log.pnr.toLowerCase().includes(searchTerm.toLowerCase()) ||
      log.actionType.toLowerCase().includes(searchTerm.toLowerCase()) ||
      log.reason.toLowerCase().includes(searchTerm.toLowerCase());

    const matchesStatus = statusFilter === 'ALL' || log.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  return (
    <div className="space-y-6">
      {/* Top Banner */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 pb-4 border-b border-slate-800">
        <div>
          <div className="flex items-center gap-2">
            <h1 className="text-2xl font-black text-white tracking-tight">Audit & Action Logs</h1>
            <span className="text-xs px-2.5 py-0.5 rounded-full bg-slate-800 text-slate-400 border border-slate-700 font-mono">
              Immutable Records
            </span>
          </div>
          <p className="text-xs text-slate-400 mt-1">
            Every verification, policy evaluation, executed compensation, and human escalation is persisted with full audit context.
          </p>
        </div>

        <button
          onClick={loadLogs}
          disabled={loading}
          className="flex items-center gap-2 px-3.5 py-2 bg-slate-800 hover:bg-slate-700 text-slate-300 rounded-xl text-xs font-semibold border border-slate-700 transition-all shadow-sm"
        >
          <RefreshCw className={`w-3.5 h-3.5 ${loading ? 'animate-spin' : ''}`} />
          <span>Refresh Logs</span>
        </button>
      </div>

      {/* Filter and Search Bar */}
      <div className="flex flex-col sm:flex-row items-center gap-3 bg-slate-900 p-3 rounded-2xl border border-slate-800">
        <div className="relative flex-1 w-full">
          <Search className="w-4 h-4 text-slate-500 absolute left-3 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Search by customer, PNR, action type, or reason..."
            className="w-full bg-slate-950 border border-slate-700 rounded-xl pl-9 pr-4 py-2 text-xs text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div className="flex items-center gap-2 w-full sm:w-auto">
          <Filter className="w-4 h-4 text-slate-500 shrink-0" />
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="bg-slate-950 border border-slate-700 rounded-xl px-3 py-2 text-xs text-slate-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="ALL">All Statuses ({logs.length})</option>
            <option value="SUCCESS">SUCCESS</option>
            <option value="ESCALATED">ESCALATED</option>
            <option value="BLOCKED_BY_POLICY">BLOCKED_BY_POLICY</option>
            <option value="INFORMATION_ONLY">INFORMATION_ONLY</option>
          </select>
        </div>
      </div>

      {/* Logs Table */}
      <div className="rounded-2xl bg-slate-900 border border-slate-800 overflow-hidden shadow-xl">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs text-slate-300">
            <thead className="bg-slate-950 text-[11px] uppercase font-bold text-slate-400 border-b border-slate-800">
              <tr>
                <th className="py-3 px-4">Timestamp</th>
                <th className="py-3 px-4">Customer</th>
                <th className="py-3 px-4">PNR</th>
                <th className="py-3 px-4">Action Type</th>
                <th className="py-3 px-4">Status</th>
                <th className="py-3 px-4">Reason / Details</th>
                <th className="py-3 px-4">Policy Reference</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60 font-normal">
              {filteredLogs.length === 0 ? (
                <tr>
                  <td colSpan={7} className="py-10 text-center text-slate-500">
                    No action logs found matching your filters.
                  </td>
                </tr>
              ) : (
                filteredLogs.map(log => (
                  <tr key={log.id} className="hover:bg-slate-800/40 transition-colors">
                    <td className="py-3 px-4 text-slate-400 font-mono text-[11px] whitespace-nowrap">
                      {new Date(log.timestamp).toLocaleDateString()} {new Date(log.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' })}
                    </td>
                    <td className="py-3 px-4 font-semibold text-white whitespace-nowrap">
                      {log.customerName}
                    </td>
                    <td className="py-3 px-4 font-mono text-blue-400 font-semibold whitespace-nowrap">
                      {log.pnr}
                    </td>
                    <td className="py-3 px-4 font-mono text-slate-300 font-medium whitespace-nowrap">
                      {log.actionType}
                    </td>
                    <td className="py-3 px-4 whitespace-nowrap">
                      <span className={`inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-[10px] font-bold border ${
                        log.status === 'SUCCESS'
                          ? 'bg-emerald-950/80 text-emerald-300 border-emerald-700/50'
                          : log.status === 'ESCALATED'
                          ? 'bg-amber-950/80 text-amber-300 border-amber-700/50'
                          : log.status === 'BLOCKED_BY_POLICY'
                          ? 'bg-red-950/80 text-red-300 border-red-700/50'
                          : 'bg-slate-800 text-slate-400 border-slate-700'
                      }`}>
                        {log.status === 'SUCCESS' && <CheckCircle2 className="w-3 h-3 text-emerald-400" />}
                        {log.status === 'ESCALATED' && <AlertTriangle className="w-3 h-3 text-amber-400" />}
                        {log.status === 'BLOCKED_BY_POLICY' && <XCircle className="w-3 h-3 text-red-400" />}
                        {log.status === 'INFORMATION_ONLY' && <Clock className="w-3 h-3 text-blue-400" />}
                        {log.status}
                      </span>
                    </td>
                    <td className="py-3 px-4 max-w-md text-slate-300 leading-relaxed text-[11px]">
                      {log.reason}
                    </td>
                    <td className="py-3 px-4 text-slate-400 font-mono text-[10px] max-w-xs truncate" title={log.policyReference}>
                      {log.policyReference}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};