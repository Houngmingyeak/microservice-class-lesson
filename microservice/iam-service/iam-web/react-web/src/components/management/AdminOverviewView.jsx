import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { useAuth } from '../../context/AuthContext';
import { api } from '../../services/api';
import {
  IconUsers,
  IconShield,
  IconKey,
  IconShieldCheck,
  IconTerminal,
  IconLaptop,
  IconActivity,
  IconUserPlus,
  IconPlus,
  IconClock
} from '../common/Icons';
import { Badge } from '../common/Badge';

export const AdminOverviewView = () => {
  const { t } = useI18n();
  const { setActiveTab } = useAuth();
  const [metrics, setMetrics] = useState({
    totalUsers: 0,
    activeUsers: 0,
    lockedUsers: 0,
    totalRoles: 0,
    totalPermissions: 0,
    totalClients: 0,
    activeSessions: 0,
    recentActivities: []
  });

  useEffect(() => {
    loadOverview();
  }, []);

  const loadOverview = async () => {
    const data = await api.getAdminOverview();
    setMetrics(data);
  };

  const statCards = [
    { title: t('metricTotalUsers'), value: metrics.totalUsers, icon: IconUsers, color: 'text-indigo-400', bg: 'bg-indigo-500/10', target: 'users' },
    { title: t('metricActiveUsers'), value: metrics.activeUsers, icon: IconShieldCheck, color: 'text-emerald-400', bg: 'bg-emerald-500/10', target: 'users' },
    { title: t('metricTotalRoles'), value: metrics.totalRoles, icon: IconKey, color: 'text-purple-400', bg: 'bg-purple-500/10', target: 'roles' },
    { title: t('metricTotalPerms'), value: metrics.totalPermissions, icon: IconShield, color: 'text-cyan-400', bg: 'bg-cyan-500/10', target: 'permissions' },
    { title: t('metricClients'), value: metrics.totalClients, icon: IconTerminal, color: 'text-amber-400', bg: 'bg-amber-500/10', target: 'clients' },
    { title: t('metricActiveSessions'), value: metrics.activeSessions, icon: IconLaptop, color: 'text-sky-400', bg: 'bg-sky-500/10', target: 'userSessions' },
  ];

  // Visual authentication traffic bars
  const trafficData = [
    { day: 'Mon', count: 142 },
    { day: 'Tue', count: 210 },
    { day: 'Wed', count: 185 },
    { day: 'Thu', count: 260 },
    { day: 'Fri', count: 320 },
    { day: 'Sat', count: 95 },
    { day: 'Sun', count: 120 },
  ];
  const maxTraffic = Math.max(...trafficData.map(d => d.count));

  return (
    <div className="view-container">
      <div className="view-header flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h2 className="view-title">{t('overviewTitle')}</h2>
          <p className="view-subtitle">{t('overviewSubtitle')}</p>
        </div>
        <div className="flex gap-2">
          <button
            onClick={() => setActiveTab('users')}
            className="btn btn-primary flex items-center gap-1.5"
          >
            <IconUserPlus size={15} />
            <span>{t('createUser')}</span>
          </button>
        </div>
      </div>

      {/* KPI Cards Grid */}
      <div className="grid grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-4">
        {statCards.map((card, i) => {
          const Icon = card.icon;
          return (
            <div
              key={i}
              onClick={() => setActiveTab(card.target)}
              className="glass-card p-5 cursor-pointer hover:border-indigo-500/50 transition group flex flex-col justify-between"
            >
              <div className="flex items-center justify-between mb-3">
                <span className="text-xs text-slate-400 font-medium">{card.title}</span>
                <div className={`p-2 rounded-lg ${card.bg}`}>
                  <Icon size={18} className={card.color} />
                </div>
              </div>
              <div className="text-2xl font-bold text-white group-hover:text-indigo-300 transition">
                {card.value}
              </div>
            </div>
          );
        })}
      </div>

      <div className="grid-2-cols mt-6">
        {/* Traffic Chart */}
        <div className="glass-card p-6">
          <div className="flex items-center justify-between mb-4 border-b border-slate-800 pb-3">
            <h3 className="text-base font-bold text-white flex items-center gap-2">
              <IconActivity size={18} className="text-indigo-400" />
              {t('loginTraffic')}
            </h3>
            <Badge variant="primary" size="sm">Last 7 Days</Badge>
          </div>

          <div className="h-44 flex items-end justify-between gap-3 pt-6 px-2">
            {trafficData.map((d, i) => {
              const heightPercent = (d.count / maxTraffic) * 100;
              return (
                <div key={i} className="flex-1 flex flex-col items-center gap-2 h-full justify-end group">
                  <span className="text-[10px] font-mono text-slate-400 opacity-0 group-hover:opacity-100 transition">
                    {d.count}
                  </span>
                  <div
                    className="w-full max-w-[36px] bg-indigo-500/40 rounded-t group-hover:bg-indigo-400 transition"
                    style={{ height: `${heightPercent}%` }}
                  />
                  <span className="text-xs text-slate-400 font-medium">{d.day}</span>
                </div>
              );
            })}
          </div>
        </div>

        {/* Recent System Activity Stream */}
        <div className="glass-card p-6">
          <div className="flex items-center justify-between mb-4 border-b border-slate-800 pb-3">
            <h3 className="text-base font-bold text-white flex items-center gap-2">
              <IconClock size={18} className="text-emerald-400" />
              {t('recentSystemActivity')}
            </h3>
            <button
              onClick={() => setActiveTab('auditLogs')}
              className="text-xs text-indigo-400 hover:underline"
            >
              View all logs →
            </button>
          </div>

          <div className="space-y-3">
            {metrics.recentActivities.map((act) => (
              <div key={act.id} className="p-3 bg-slate-950/60 rounded-lg border border-slate-800/80 flex items-start justify-between gap-3 text-xs">
                <div>
                  <div className="flex items-center gap-1.5 mb-1">
                    <span className="font-semibold text-white">{act.event}</span>
                    <span className="text-slate-400">by</span>
                    <span className="text-indigo-300 font-medium">{act.actor}</span>
                  </div>
                  <p className="text-slate-400">{act.details}</p>
                </div>
                <span className="font-mono text-[10px] text-slate-500 shrink-0">
                  {new Date(act.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                </span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
