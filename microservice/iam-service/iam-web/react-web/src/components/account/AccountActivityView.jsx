import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import {
  IconActivity,
  IconLock,
  IconUser,
  IconGlobe,
  IconShield,
  IconTrash,
  IconCheck,
  IconSearch,
  IconX
} from '../common/Icons';

export const AccountActivityView = () => {
  const { t } = useI18n();
  const [activities, setActivities] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('ALL');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadActivities();
  }, []);

  const loadActivities = async () => {
    try {
      const list = await api.getAccountActivities();
      setActivities(list || []);
    } finally {
      setLoading(false);
    }
  };

  const getActivityIcon = (action = '') => {
    const act = action.toUpperCase();
    if (act.includes('PASSWORD')) return <IconLock size={20} className="text-amber-600" />;
    if (act.includes('PROFILE') || act.includes('AVATAR')) return <IconUser size={20} className="text-sky-600" />;
    if (act.includes('2FA') || act.includes('SECURITY') || act.includes('MFA')) return <IconShield size={20} className="text-emerald-600" />;
    if (act.includes('IDENTITY') || act.includes('OAUTH')) return <IconGlobe size={20} className="text-purple-600" />;
    if (act.includes('SESSION') || act.includes('TERMINAT')) return <IconTrash size={20} className="text-rose-600" />;
    return <IconActivity size={20} className="text-indigo-600" />;
  };

  const getActivityBg = (action = '') => {
    const act = action.toUpperCase();
    if (act.includes('PASSWORD')) return 'bg-amber-50 border-amber-200';
    if (act.includes('PROFILE') || act.includes('AVATAR')) return 'bg-sky-50 border-sky-200';
    if (act.includes('2FA') || act.includes('SECURITY') || act.includes('MFA')) return 'bg-emerald-50 border-emerald-200';
    if (act.includes('IDENTITY') || act.includes('OAUTH')) return 'bg-purple-50 border-purple-200';
    if (act.includes('SESSION') || act.includes('TERMINAT')) return 'bg-rose-50 border-rose-200';
    return 'bg-indigo-50 border-indigo-200';
  };

  const filtered = activities.filter(item => {
    const term = searchTerm.toLowerCase();
    const matchesSearch =
      (item.action && item.action.toLowerCase().includes(term)) ||
      (item.description && item.description.toLowerCase().includes(term)) ||
      (item.actor && item.actor.toLowerCase().includes(term)) ||
      (item.ip && item.ip.toLowerCase().includes(term));

    const act = (item.action || '').toUpperCase();
    let matchesCategory = true;
    if (categoryFilter === 'PROFILE') {
      matchesCategory = act.includes('PROFILE') || act.includes('AVATAR') || act.includes('USER');
    } else if (categoryFilter === 'SECURITY') {
      matchesCategory = act.includes('PASSWORD') || act.includes('SECURITY') || act.includes('MFA') || act.includes('2FA');
    } else if (categoryFilter === 'SESSIONS') {
      matchesCategory = act.includes('LOGIN') || act.includes('SESSION') || act.includes('AUTH');
    }

    return matchesSearch && matchesCategory;
  });

  const totalCount = activities.length;
  const profileCount = activities.filter(a => {
    const act = (a.action || '').toUpperCase();
    return act.includes('PROFILE') || act.includes('AVATAR');
  }).length;
  const securityCount = activities.filter(a => {
    const act = (a.action || '').toUpperCase();
    return act.includes('PASSWORD') || act.includes('SECURITY') || act.includes('MFA');
  }).length;

  return (
    <div className="view-container">
      {/* View Header */}
      <div className="view-header">
        <div>
          <h2 className="view-title">{t('activityTitle') || 'Security & Account Activity'}</h2>
          <p className="view-subtitle">
            {t('activitySubtitle') || 'Chronological log of changes made to your profile, credentials, and settings.'}
          </p>
        </div>
      </div>

      {/* Top Stat Summary Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-5 mb-8">
        <div className="p-6 bg-white border border-slate-200 rounded-3xl shadow-sm flex items-center justify-between">
          <div>
            <span className="text-base font-semibold text-slate-500 uppercase tracking-wider block mb-1">
              Total Logged Events
            </span>
            <span className="text-3xl font-extrabold text-slate-900">{totalCount}</span>
          </div>
          <div className="w-14 h-14 rounded-full bg-slate-100 border border-slate-200 flex items-center justify-center text-slate-700">
            <IconActivity size={26} />
          </div>
        </div>

        <div className="p-6 bg-white border border-slate-200 rounded-3xl shadow-sm flex items-center justify-between">
          <div>
            <span className="text-base font-semibold text-sky-600 uppercase tracking-wider block mb-1">
              Profile Modifications
            </span>
            <span className="text-3xl font-extrabold text-sky-700">{profileCount}</span>
          </div>
          <div className="w-14 h-14 rounded-full bg-sky-50 border border-sky-200 flex items-center justify-center text-sky-600">
            <IconUser size={26} />
          </div>
        </div>

        <div className="p-6 bg-white border border-slate-200 rounded-3xl shadow-sm flex items-center justify-between">
          <div>
            <span className="text-base font-semibold text-amber-600 uppercase tracking-wider block mb-1">
              Security Updates
            </span>
            <span className="text-3xl font-extrabold text-amber-700">{securityCount}</span>
          </div>
          <div className="w-14 h-14 rounded-full bg-amber-50 border border-amber-200 flex items-center justify-center text-amber-600">
            <IconShield size={26} />
          </div>
        </div>
      </div>

      {/* Main Activity Log Card */}
      <div className="bg-white border border-slate-200 rounded-3xl shadow-sm overflow-hidden">
        {/* Search & Filter Toolbar */}
        <div className="p-6 border-b border-slate-100 flex flex-col md:flex-row items-stretch md:items-center justify-between gap-4 bg-slate-50/50">
          {/* Search Box */}
          <div className="relative flex-1 max-w-md">
            <input
              type="text"
              placeholder="Search activity by keyword, action, or IP..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-12 pr-6 py-3 rounded-full bg-white border border-slate-200 text-slate-900 placeholder:text-slate-400 focus:outline-none focus:ring-4 focus:ring-indigo-100 focus:border-indigo-600 text-base transition shadow-2xs"
            />
            <span
              className="absolute left-4 text-slate-400 pointer-events-none flex items-center justify-center"
              style={{ top: '50%', transform: 'translateY(-50%)' }}
            >
              <IconSearch size={20} />
            </span>
            {searchTerm && (
              <button
                type="button"
                onClick={() => setSearchTerm('')}
                className="absolute right-4 text-slate-400 hover:text-slate-600 p-1 rounded-full cursor-pointer"
                style={{ top: '50%', transform: 'translateY(-50%)' }}
                title="Clear search"
              >
                <IconX size={16} />
              </button>
            )}
          </div>

          {/* Filter Pills */}
          <div className="flex items-center gap-2 flex-wrap">
            {[
              { key: 'ALL', label: 'All Activities' },
              { key: 'PROFILE', label: 'Profile' },
              { key: 'SECURITY', label: 'Security' },
              { key: 'SESSIONS', label: 'Auth & Sessions' }
            ].map(cat => (
              <button
                key={cat.key}
                type="button"
                onClick={() => setCategoryFilter(cat.key)}
                className={`px-5 py-2.5 rounded-full font-bold text-base transition cursor-pointer whitespace-nowrap ${
                  categoryFilter === cat.key
                    ? 'bg-indigo-600 text-white shadow-sm'
                    : 'bg-white border border-slate-200 text-slate-700 hover:bg-slate-100'
                }`}
              >
                {cat.label}
              </button>
            ))}
          </div>
        </div>

        {/* Chronological Activity List */}
        <div className="divide-y divide-slate-100">
          {filtered.map((act) => (
            <div
              key={act.id}
              className="p-6 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-5 hover:bg-slate-50/70 transition"
            >
              <div className="flex items-start gap-4.5">
                <div
                  className={`w-14 h-14 rounded-full border flex items-center justify-center shrink-0 mt-0.5 shadow-2xs ${getActivityBg(
                    act.action
                  )}`}
                >
                  {getActivityIcon(act.action)}
                </div>

                <div>
                  <div className="flex flex-wrap items-center gap-2.5">
                    <h4 className="text-xl font-bold text-slate-900">{act.action}</h4>
                    <span className="px-3 py-0.5 rounded-full text-xs font-semibold bg-indigo-50 text-indigo-700 border border-indigo-200">
                      {act.actor}
                    </span>
                  </div>

                  <p className="text-base text-slate-700 mt-1 font-medium">{act.description}</p>

                  <div className="mt-2 flex flex-wrap items-center gap-x-6 gap-y-1 text-sm text-slate-400 font-mono">
                    <span>
                      🕒 {new Date(act.timestamp).toLocaleString(undefined, {
                        dateStyle: 'medium',
                        timeStyle: 'medium'
                      })}
                    </span>
                    {act.ip && <span>🌐 IP: {act.ip}</span>}
                  </div>
                </div>
              </div>

              <div className="self-end sm:self-center shrink-0">
                <span className="inline-flex items-center gap-1.5 px-3.5 py-1 rounded-full text-sm font-semibold bg-emerald-50 text-emerald-700 border border-emerald-200">
                  <IconCheck size={14} className="text-emerald-600" />
                  <span>Recorded</span>
                </span>
              </div>
            </div>
          ))}

          {filtered.length === 0 && (
            <div className="py-16 text-center">
              <div className="w-16 h-16 rounded-full bg-slate-100 border border-slate-200 flex items-center justify-center text-slate-400 mx-auto mb-4">
                <IconActivity size={28} />
              </div>
              <h4 className="text-xl font-bold text-slate-800 mb-1">
                {t('noResultsFound') || 'No account activities recorded yet'}
              </h4>
              <p className="text-base text-slate-500">
                {searchTerm
                  ? 'No activities matched your search. Try another keyword.'
                  : 'Any changes made to your profile, avatar, credentials, or sessions will be automatically logged here.'}
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
