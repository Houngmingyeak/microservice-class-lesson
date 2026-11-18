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
    { title: t('metricTotalUsers'), value: metrics.totalUsers, icon: IconUsers, color: 'text-indigo-600', bg: 'bg-indigo-50 border border-indigo-100', target: 'users' },
    { title: t('metricActiveUsers'), value: metrics.activeUsers, icon: IconShieldCheck, color: 'text-emerald-600', bg: 'bg-emerald-50 border border-emerald-100', target: 'users' },
    { title: t('metricTotalRoles'), value: metrics.totalRoles, icon: IconKey, color: 'text-purple-600', bg: 'bg-purple-50 border border-purple-100', target: 'roles' },
    { title: t('metricTotalPerms'), value: metrics.totalPermissions, icon: IconShield, color: 'text-cyan-600', bg: 'bg-cyan-50 border border-cyan-100', target: 'permissions' },
    { title: t('metricClients'), value: metrics.totalClients, icon: IconTerminal, color: 'text-amber-600', bg: 'bg-amber-50 border border-amber-100', target: 'clients' },
    { title: t('metricActiveSessions'), value: metrics.activeSessions, icon: IconLaptop, color: 'text-sky-600', bg: 'bg-sky-50 border border-sky-100', target: 'userSessions' },
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
      <div className="grid grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-3.5">
        {statCards.map((card, i) => {
          const Icon = card.icon;
          return (
            <div
              key={i}
              onClick={() => setActiveTab(card.target)}
              className="glass-card p-4 cursor-pointer hover:border-indigo-400 hover:shadow-sm transition group flex flex-col justify-between min-w-0"
            >
              <div className="flex items-center justify-between gap-2 mb-2 min-w-0">
                <span className="text-xs text-slate-500 font-semibold truncate whitespace-nowrap" title={card.title}>
                  {card.title}
                </span>
                <div className={`p-1.5 rounded-lg shrink-0 ${card.bg}`}>
                  <Icon size={16} className={card.color} />
                </div>
              </div>
              <div className="text-xl font-bold text-slate-900 group-hover:text-indigo-600 transition">
                {card.value}
              </div>
            </div>
          );
        })}
      </div>

      <div className="grid-2-cols mt-6">
        {/* Traffic Chart */}
        <div className="glass-card p-6">
          <div className="flex items-center justify-between mb-4 border-b border-slate-100 pb-3">
            <h3 className="text-base font-bold text-slate-900 flex items-center gap-2">
              <IconActivity size={18} className="text-indigo-600" />
              {t('loginTraffic')}
            </h3>
            <Badge variant="primary" size="sm">Last 7 Days</Badge>
          </div>

          <div className="h-44 flex items-end justify-between gap-3 pt-6 px-2">
            {trafficData.map((d, i) => {
              const heightPercent = (d.count / maxTraffic) * 100;
              return (
                <div key={i} className="flex-1 flex flex-col items-center gap-2 h-full justify-end group">
                  <span className="text-xs font-mono text-slate-600 opacity-0 group-hover:opacity-100 transition font-semibold">
                    {d.count}
                  </span>
                  <div
                    className="w-full max-w-[36px] bg-indigo-100 rounded-t-md group-hover:bg-indigo-600 transition"
                    style={{ height: `${heightPercent}%` }}
                  />
                  <span className="text-xs text-slate-500 font-semibold">{d.day}</span>
                </div>
              );
            })}
          </div>
        </div>

        {/* Recent System Activity Stream */}
        <div className="glass-card p-6">
          <div className="flex items-center justify-between mb-4 border-b border-slate-100 pb-3">
            <h3 className="text-base font-bold text-slate-900 flex items-center gap-2">
              <IconClock size={18} className="text-emerald-600" />
              {t('recentSystemActivity')}
            </h3>
            <button
              onClick={() => setActiveTab('auditLogs')}
              className="text-xs font-semibold text-indigo-600 hover:text-indigo-800 hover:underline"
            >
              View all logs →
            </button>
          </div>

          <div className="space-y-3">
            {metrics.recentActivities.map((act) => (
              <div key={act.id} className="p-3 bg-slate-50/80 rounded-xl border border-slate-200/80 flex items-start justify-between gap-3 text-xs hover:bg-indigo-50/30 transition">
                <div>
                  <div className="flex items-center gap-1.5 mb-1">
                    <span className="font-bold text-slate-900">{act.event}</span>
                    <span className="text-slate-400">by</span>
                    <span className="text-indigo-600 font-semibold">{act.actor}</span>
                  </div>
                  <p className="text-slate-600">{act.details}</p>
                </div>
                <span className="font-mono text-xs text-slate-400 shrink-0">
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
