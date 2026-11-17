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

      <div className="bg-white border border-slate-200 rounded-3xl shadow-sm p-6 md:p-8">
        <div className="relative pl-7 space-y-8 before:absolute before:left-3 before:top-3 before:bottom-3 before:w-0.5 before:bg-slate-200">
          {activities.map((act) => (
            <div key={act.id} className="relative group">
              <div className="absolute -left-7 top-1 w-7 h-7 rounded-full bg-white border-2 border-indigo-500 flex items-center justify-center shadow-xs">
                {getActivityIcon(act.action)}
              </div>
              <div className="bg-slate-50 border border-slate-200 rounded-2xl p-5 hover:border-slate-300 transition">
                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 mb-2">
                  <div className="flex items-center gap-2.5">
                    <span className="font-bold text-slate-900 text-base">{act.action}</span>
                    <span className="px-3 py-0.5 rounded-full text-xs font-semibold bg-indigo-50 text-indigo-700 border border-indigo-200">
                      {act.actor}
                    </span>
                  </div>
                  <span className="text-sm text-slate-400 font-mono">
                    {new Date(act.timestamp).toLocaleString()}
                  </span>
                </div>
                <p className="text-base text-slate-700">{act.description}</p>
                {act.ip && (
                  <div className="mt-2 text-sm font-mono text-slate-400">
                    Originated from IP: {act.ip}
                  </div>
                )}
              </div>
            </div>
          ))}

          {activities.length === 0 && (
            <div className="text-center py-12 text-slate-400 text-lg">
              {t('noData')}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
