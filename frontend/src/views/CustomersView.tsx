import React, { useState, useEffect } from 'react';
import { Customer, Booking } from '../types';
import { getCustomers, getBookingsByPnr } from '../api';
import { CustomerBadge } from '../components/CustomerBadge';
import { User, Mail, Phone, Plane, Calendar, MessageSquare, History, AlertCircle } from 'lucide-react';

interface Props {
  onOpenChat: (pnr: string) => void;
}

export const CustomersView: React.FC<Props> = ({ onOpenChat }) => {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [customerBookings, setCustomerBookings] = useState<Record<string, Booking[]>>({});
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const data = await getCustomers();
      setCustomers(data);

      const bksMap: Record<string, Booking[]> = {};
      for (const c of data) {
        const bks = await getBookingsByPnr(c.bookingReference);
        bksMap[c.bookingReference] = bks;
      }
      setCustomerBookings(bksMap);
    } catch (err) {
      console.error('Failed to load customers', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between pb-4 border-b border-slate-800">
        <div>
          <h1 className="text-2xl font-black text-white tracking-tight">Customer Directory</h1>
          <p className="text-xs text-slate-400 mt-0.5">
            Passengers from Assignment 3 source profiles with full flight history & active bookings
          </p>
        </div>
        <span className="text-xs px-3 py-1 rounded-full bg-slate-800 text-slate-300 border border-slate-700">
          Exercise Date: Wednesday, 23 Sep 2026
        </span>
      </div>

      {loading ? (
        <div className="py-12 text-center text-slate-500 text-sm">Loading passenger profiles...</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {customers.map(c => {
            const bks = customerBookings[c.bookingReference] || [];
            const primaryBooking = bks[0];
            const flight = primaryBooking?.flight;

            return (
              <div
                key={c.id}
                className="rounded-2xl bg-slate-900 border border-slate-800 p-6 flex flex-col justify-between shadow-xl"
              >
                <div className="space-y-4">
                  {/* Header */}
                  <div className="flex items-start justify-between">
                    <div>
                      <h2 className="text-xl font-bold text-white">{c.name}</h2>
                      <p className="text-xs font-mono text-blue-400">PNR: {c.bookingReference}</p>
                    </div>
                    <CustomerBadge tier={c.loyaltyTier} size="md" />
                  </div>

                  {/* Contact Info */}
                  <div className="p-3 rounded-xl bg-slate-950/70 border border-slate-800/80 text-xs space-y-1.5">
                    <div className="flex items-center gap-2 text-slate-300">
                      <Mail className="w-3.5 h-3.5 text-slate-500 shrink-0" />
                      <span>{c.email}</span>
                    </div>
                    <div className="flex items-center gap-2 text-slate-300">
                      <Phone className="w-3.5 h-3.5 text-slate-500 shrink-0" />
                      <span className="font-mono">{c.maskedPhone}</span>
                    </div>
                  </div>

                  {/* Booking / Flight details */}
                  <div className="p-3.5 rounded-xl bg-slate-950 border border-slate-800 space-y-2 text-xs">
                    <span className="text-[10px] uppercase font-bold text-slate-400 tracking-wider flex items-center gap-1.5">
                      <Plane className="w-3.5 h-3.5 text-blue-400" /> Active Disrupted Booking
                    </span>

                    {flight ? (
                      <div>
                        <div className="flex justify-between items-center">
                          <span className="font-bold text-white text-sm">{flight.flightNumber}</span>
                          <span className={`px-2 py-0.5 rounded text-[10px] font-bold ${
                            flight.status === 'CANCELLED'
                              ? 'bg-red-950 text-red-400 border border-red-800/40'
                              : 'bg-amber-950 text-amber-400 border border-amber-800/40'
                          }`}>
                            {flight.status}
                          </span>
                        </div>
                        <p className="text-slate-300 mt-1">
                          {flight.routeFrom} → {flight.routeTo}
                        </p>
                        <p className="text-slate-400 text-[11px] mt-0.5">
                          Scheduled: {flight.scheduledDeparture}
                          {flight.newDeparture ? ` • Revised: ${flight.newDeparture}` : ''}
                        </p>
                        {flight.cancellationReason && (
                          <p className="text-red-400 text-[11px] mt-0.5">
                            Reason: {flight.cancellationReason}
                          </p>
                        )}
                        <div className="mt-2 pt-2 border-t border-slate-800/80 flex justify-between text-slate-400">
                          <span>Original Fare:</span>
                          <span className="font-semibold text-slate-200">₹{primaryBooking?.fareAmount} ({primaryBooking?.originalPaymentMethod})</span>
                        </div>
                      </div>
                    ) : (
                      <p className="text-slate-500">No active bookings found.</p>
                    )}
                  </div>

                  {/* Travel History & Complaints */}
                  <div className="text-xs space-y-2 text-slate-400">
                    <div className="flex items-center gap-1.5">
                      <History className="w-3.5 h-3.5 text-slate-500" />
                      <span>{c.travelHistoryFlightsCount} flights in last 12 months</span>
                    </div>
                    <div className="p-2.5 rounded-lg bg-slate-950/40 border border-slate-800/60 text-[11px]">
                      <span className="font-semibold text-slate-300 block mb-0.5">Prior Disruption History:</span>
                      <p className="text-slate-400">{c.priorComplaints}</p>
                    </div>
                  </div>
                </div>

                {/* Footer Action */}
                <div className="mt-6 pt-4 border-t border-slate-800">
                  <button
                    onClick={() => onOpenChat(c.bookingReference)}
                    className="w-full py-2.5 rounded-xl bg-blue-600 hover:bg-blue-500 text-white font-semibold text-xs flex items-center justify-center gap-1.5 transition-all shadow-md shadow-blue-600/20"
                  >
                    <MessageSquare className="w-3.5 h-3.5" />
                    <span>Launch Live Chat</span>
                  </button>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};