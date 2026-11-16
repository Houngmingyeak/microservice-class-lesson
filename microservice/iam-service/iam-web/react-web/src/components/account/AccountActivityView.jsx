import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { IconActivity, IconLock, IconUser, IconGlobe, IconShield, IconTrash } from '../common/Icons';
import { Badge } from '../common/Badge';

export const AccountActivityView = () => {
  const { t } = useI18n();
  const [activities, setActivities] = useState([]);

  useEffect(() => {
    loadActivities();
  }, []);

  const loadActivities = async () => {
    const list = await api.getAccountActivities();
    setActivities(list);
  };

  const getActivityIcon = (action) => {
    if (action.includes('PASSWORD')) return <IconLock size={16} className="text-amber-400" />;
    if (action.includes('PROFILE')) return <IconUser size={16} className="text-sky-400" />;
    if (action.includes('2FA') || action.includes('SECURITY')) return <IconShield size={16} className="text-emerald-400" />;
    if (action.includes('IDENTITY')) return <IconGlobe size={16} className="text-purple-400" />;
    if (action.includes('SESSION')) return <IconTrash size={16} className="text-rose-400" />;
    return <IconActivity size={16} className="text-indigo-400" />;
  };

  return (
    <div className="view-container">
      <div className="view-header">
        <div>
          <h2 className="view-title">{t('activityTitle')}</h2>
          <p className="view-subtitle">{t('activitySubtitle')}</p>
        </div>
      </div>

      <div className="glass-card p-6">
        <div className="relative pl-6 space-y-8 before:absolute before:left-2.5 before:top-3 before:bottom-3 before:w-0.5 before:bg-slate-800">
          {activities.map((act) => (
            <div key={act.id} className="relative group">
              <div className="absolute -left-6 top-1 w-6 h-6 rounded-full bg-slate-900 border border-slate-700 flex items-center justify-center shadow">
                {getActivityIcon(act.action)}
              </div>
              <div className="bg-slate-950/60 border border-slate-800/80 rounded-xl p-4 hover:border-slate-700 transition">
                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-1 mb-2">
                  <div className="flex items-center gap-2">
                    <span className="font-semibold text-white text-sm">{act.action}</span>
                    <Badge variant="default" size="sm">{act.actor}</Badge>
                  </div>
                  <span className="text-xs text-slate-500 font-mono">
                    {new Date(act.timestamp).toLocaleString()}
                  </span>
                </div>
                <p className="text-sm text-slate-300">{act.description}</p>
                {act.ip && (
                  <div className="mt-2 text-xs font-mono text-slate-500">
                    Originated from IP: {act.ip}
                  </div>
                )}
              </div>
            </div>
          ))}

          {activities.length === 0 && (
            <div className="text-center py-8 text-slate-500">
              {t('noData')}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
