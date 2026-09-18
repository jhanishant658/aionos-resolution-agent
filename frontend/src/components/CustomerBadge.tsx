import React from 'react';
import { LoyaltyTier } from '../types';
import { Award, Shield, Star } from 'lucide-react';

interface Props {
  tier: LoyaltyTier | string;
  size?: 'sm' | 'md' | 'lg';
}

export const CustomerBadge: React.FC<Props> = ({ tier, size = 'md' }) => {
  const upper = (tier || '').toUpperCase();

  const sizeClasses = {
    sm: 'text-xs px-2 py-0.5 gap-1',
    md: 'text-xs px-2.5 py-1 gap-1.5 font-medium',
    lg: 'text-sm px-3 py-1.5 gap-2 font-semibold'
  }[size];

  if (upper === 'PLATINUM') {
    return (
      <span className={`inline-flex items-center rounded-full bg-purple-950/80 border border-purple-500/50 text-purple-300 shadow-sm shadow-purple-900/40 ${sizeClasses}`}>
        <Star className="w-3.5 h-3.5 text-purple-400 fill-purple-400/30" />
        Platinum Member
      </span>
    );
  }

  if (upper === 'GOLD') {
    return (
      <span className={`inline-flex items-center rounded-full bg-amber-950/80 border border-amber-500/50 text-amber-300 shadow-sm shadow-amber-900/40 ${sizeClasses}`}>
        <Award className="w-3.5 h-3.5 text-amber-400 fill-amber-400/30" />
        Gold Member
      </span>
    );
  }

  return (
    <span className={`inline-flex items-center rounded-full bg-slate-800 border border-slate-600 text-slate-300 shadow-sm ${sizeClasses}`}>
      <Shield className="w-3.5 h-3.5 text-slate-400" />
      Silver Member
    </span>
  );
};