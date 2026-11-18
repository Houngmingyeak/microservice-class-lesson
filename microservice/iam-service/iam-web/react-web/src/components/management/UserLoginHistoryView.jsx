import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { Badge } from '../common/Badge';
import {
  IconCheck,
  IconX,
  IconAlertTriangle,
  IconSearch,
  IconRefresh,
  IconShield,
  IconShieldCheck,
  IconShieldAlert,
  IconGlobe,
  IconLaptop,
  IconSmartphone
} from '../common/Icons';

export const UserLoginHistoryView = () => {
  const { t } = useI18n();
  const [history, setHistory] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadHistory();
  }, []);

  const loadHistory = async () => {
    setLoading(true);
    try {
      const realTelemetry = await api.getAdminLoginHistory();
      setHistory(realTelemetry || []);
    } catch (e) {
      console.error('Failed to load admin login telemetry:', e);
      setHistory([]);
    } finally {
      setLoading(false);
    }
  };

  const filtered = history.filter(item => {
    const term = searchTerm.toLowerCase().trim();
    if (!term) {
      return statusFilter === 'ALL' || item.status === statusFilter;
    }
    const matchesSearch =
      (item.username && item.username.toLowerCase().includes(term)) ||
      (item.ipAddress && item.ipAddress.includes(term)) ||
      (item.location && item.location.toLowerCase().includes(term)) ||
      (item.device && item.device.toLowerCase().includes(term)) ||
      (item.reason && item.reason.toLowerCase().includes(term));

    const matchesStatus = statusFilter === 'ALL' || item.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  const totalCount = history.length;
  const successCount = history.filter(h => h.status === 'SUCCESS').length;
  const failedCount = history.filter(h => h.status === 'FAILED').length;
  const uniqueUsersCount = new Set(history.map(h => h.username)).size;

  const getDeviceIcon = (deviceStr = '', status = 'SUCCESS') => {
    if (status === 'FAILED') {
      return <IconShieldAlert size={18} className="text-rose-600 flex-shrink-0" />;
    }
    const d = (deviceStr || '').toLowerCase();
    if (d.includes('iphone') || d.includes('android') || d.includes('mobile')) {
      return <IconSmartphone size={18} className="text-sky-600 flex-shrink-0" />;
    }
    return <IconLaptop size={18} className="text-indigo-600 flex-shrink-0" />;
  };

  return (
    <div className="view-container">
      {/* View Header with live refresh button */}
      <div className="view-header flex flex-col md:flex-row md:items-center justify-between gap-3">
        <div>
          <h2 className="view-title text-lg font-bold text-slate-900 tracking-tight">
            {t('adminLoginHistoryTitle')}
          </h2>
          <p className="view-subtitle text-slate-500 text-xs mt-0.5">
            {t('adminLoginHistorySubtitle')}
          </p>
        </div>
        <div className="flex items-center gap-2">
          <button
            onClick={loadHistory}
            disabled={loading}
            className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg border border-slate-200 bg-white hover:bg-slate-50 text-slate-700 font-semibold text-xs shadow-2xs transition disabled:opacity-60 cursor-pointer whitespace-nowrap"
            title="Refresh database telemetry"
          >
            <IconRefresh size={14} className={loading ? "animate-spin text-indigo-600" : "text-slate-500"} />
            <span>{loading ? 'Refreshing...' : 'Refresh Telemetry'}</span>
          </button>
        </div>
      </div>

      {/* Real Directory Telemetry Summary Metric Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-5">
        <div className="stat-card p-4 rounded-xl bg-white border border-slate-200/80 shadow-xs flex items-center justify-between">
          <div className="min-w-0">
            <p className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-0.5 whitespace-nowrap truncate">
              Total Monitored Sign-ins
            </p>
            <h3 className="text-xl font-bold text-slate-900">{totalCount}</h3>
            <span className="text-[11px] text-slate-500 font-medium mt-0.5 inline-block whitespace-nowrap">Real directory events</span>
          </div>
          <div className="w-10 h-10 rounded-xl bg-indigo-50 border border-indigo-100 flex items-center justify-center text-indigo-600 shrink-0 ml-3">
            <IconShield size={18} />
          </div>
        </div>

        <div className="stat-card p-4 rounded-xl bg-white border border-slate-200/80 shadow-xs flex items-center justify-between">
          <div className="min-w-0">
            <p className="text-xs font-bold uppercase tracking-wider text-emerald-600 mb-0.5 whitespace-nowrap truncate">
              Authorized Sign-ins
            </p>
            <h3 className="text-xl font-bold text-emerald-700">{successCount}</h3>
            <span className="text-[11px] text-emerald-600 font-medium mt-0.5 inline-block whitespace-nowrap">Verified authentications</span>
          </div>
          <div className="w-10 h-10 rounded-xl bg-emerald-50 border border-emerald-100 flex items-center justify-center text-emerald-600 shrink-0 ml-3">
            <IconShieldCheck size={18} />
          </div>
        </div>

        <div className="stat-card p-4 rounded-xl bg-white border border-slate-200/80 shadow-xs flex items-center justify-between">
          <div className="min-w-0">
            <p className="text-xs font-bold uppercase tracking-wider text-rose-600 mb-0.5 whitespace-nowrap truncate">
              Failed / Locked Anomalies
            </p>
            <h3 className="text-xl font-bold text-rose-700">{failedCount}</h3>
            <span className="text-[11px] text-rose-600 font-medium mt-0.5 inline-block whitespace-nowrap">Locked or disabled accounts</span>
          </div>
          <div className="w-10 h-10 rounded-xl bg-rose-50 border border-rose-100 flex items-center justify-center text-rose-600 shrink-0 ml-3">
            <IconShieldAlert size={18} />
          </div>
        </div>

        <div className="stat-card p-4 rounded-xl bg-white border border-slate-200/80 shadow-xs flex items-center justify-between">
          <div className="min-w-0">
            <p className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-0.5 whitespace-nowrap truncate">
              Tracked Accounts
            </p>
            <h3 className="text-xl font-bold text-slate-900">{uniqueUsersCount}</h3>
            <div className="flex items-center gap-1.5 mt-0.5">
              <span className="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse"></span>
              <span className="text-[11px] text-emerald-600 font-medium whitespace-nowrap">PostgreSQL live sync</span>
            </div>
          </div>
          <div className="w-10 h-10 rounded-xl bg-slate-100 border border-slate-200 flex items-center justify-center text-slate-600 shrink-0 ml-3">
            <IconGlobe size={18} />
          </div>
        </div>
      </div>

      {/* Main Table Card */}
      <div className="glass-card p-4 sm:p-5 bg-white rounded-xl border border-slate-200/80 shadow-xs">
        {/* Search & Filter Toolbar */}
        <div className="flex flex-col lg:flex-row gap-3 mb-4 justify-between items-stretch lg:items-center">
          {/* Search Input with generous padding & clear button */}
          <div className="relative w-full lg:w-80">
            <span className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none">
              <IconSearch size={15} />
            </span>
            <input
              type="text"
              placeholder="Search by user, IP, device, location..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="form-input w-full pl-9 pr-8 py-1.5 rounded-lg border border-slate-200 bg-white text-slate-800 text-xs focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition shadow-2xs"
            />
            {searchTerm && (
              <button
                type="button"
                onClick={() => setSearchTerm('')}
                className="absolute right-2.5 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 p-1 cursor-pointer"
                title="Clear search"
              >
                <IconX size={13} />
              </button>
            )}
          </div>

          {/* Filter Pills with explicit non-squashable container */}
          <div className="flex flex-wrap items-center gap-1.5">
            <button
              type="button"
              onClick={() => setStatusFilter('ALL')}
              className={`filter-pill-btn px-3 py-1.5 text-xs font-semibold rounded-lg border transition flex items-center gap-1.5 cursor-pointer ${
                statusFilter === 'ALL'
                  ? 'filter-pill-active bg-indigo-600 border-indigo-600 text-white shadow-2xs'
                  : 'bg-white border-slate-200 text-slate-600 hover:bg-slate-50'
              }`}
              style={{ minWidth: 'fit-content', whiteSpace: 'nowrap' }}
            >
              <span>{t('all')}</span>
              <span className={`px-1.5 py-0.2 rounded-full text-[10px] font-bold ${
                statusFilter === 'ALL' ? 'bg-indigo-500 text-white' : 'bg-slate-100 text-slate-600'
              }`}>
                {totalCount}
              </span>
            </button>

            <button
              type="button"
              onClick={() => setStatusFilter('SUCCESS')}
              className={`filter-pill-btn px-3 py-1.5 text-xs font-semibold rounded-lg border transition flex items-center gap-1.5 cursor-pointer ${
                statusFilter === 'SUCCESS'
                  ? 'filter-pill-active bg-emerald-600 border-emerald-600 text-white shadow-2xs'
                  : 'bg-white border-slate-200 text-slate-600 hover:bg-slate-50'
              }`}
              style={{ minWidth: 'fit-content', whiteSpace: 'nowrap' }}
            >
              <span>{t('success')}</span>
              <span className={`px-1.5 py-0.2 rounded-full text-[10px] font-bold ${
                statusFilter === 'SUCCESS' ? 'bg-emerald-500 text-white' : 'bg-slate-100 text-slate-600'
              }`}>
                {successCount}
              </span>
            </button>

            <button
              type="button"
              onClick={() => setStatusFilter('FAILED')}
              className={`filter-pill-btn px-3 py-1.5 text-xs font-semibold rounded-lg border transition flex items-center gap-1.5 cursor-pointer ${
                statusFilter === 'FAILED'
                  ? 'filter-pill-active bg-rose-600 border-rose-600 text-white shadow-2xs'
                  : 'bg-white border-slate-200 text-slate-600 hover:bg-slate-50'
              }`}
              style={{ minWidth: 'fit-content', whiteSpace: 'nowrap' }}
            >
              <span>{t('failed')}</span>
              <span className={`px-1.5 py-0.2 rounded-full text-[10px] font-bold ${
                statusFilter === 'FAILED' ? 'bg-rose-500 text-white' : 'bg-slate-100 text-slate-600'
              }`}>
                {failedCount}
              </span>
            </button>
          </div>
        </div>

        {/* Telemetry Table */}
        <div className="table-wrapper border border-slate-200 rounded-xl overflow-hidden bg-white shadow-sm">
          <div className="overflow-x-auto w-full">
            <table className="data-table w-full text-left border-collapse">
              <thead>
                <tr className="bg-slate-50/80 border-b border-slate-200">
                  <th className="py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[170px]">
                    {t('time')}
                  </th>
                  <th className="py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[190px]">
                    Target User
                  </th>
                  <th className="py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[120px]">
                    {t('status')}
                  </th>
                  <th className="py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[140px]">
                    {t('riskLevel')}
                  </th>
                  <th className="py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[150px]">
                    IP Address
                  </th>
                  <th className="py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[180px]">
                    {t('location')}
                  </th>
                  <th className="py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[220px]">
                    {t('details')}
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filtered.map((item) => {
                  const isSuspicious = item.risk?.includes('Suspicious') || item.risk?.includes('Locked') || item.status === 'FAILED';
                  const initialChar = (item.username || 'U').charAt(0).toUpperCase();

                  return (
                    <tr
                      key={item.id}
                      className={`hover:bg-slate-50/70 transition-colors ${
                        isSuspicious ? 'bg-rose-50/30' : ''
                      }`}
                    >
                      {/* Timestamp */}
                      <td className="py-3 px-4 font-mono text-xs text-slate-600 whitespace-nowrap">
                        {item.timestamp ? new Date(item.timestamp).toLocaleString() : 'Just now'}
                      </td>

                      {/* Real User Identity with Avatar */}
                      <td className="py-3 px-4 whitespace-nowrap">
                        <div className="flex items-center gap-3">
                          <div className={`w-8 h-8 rounded-full flex items-center justify-center font-bold text-xs shadow-sm flex-shrink-0 ${
                            isSuspicious
                              ? 'bg-rose-100 text-rose-700 border border-rose-200'
                              : 'bg-indigo-100 text-indigo-700 border border-indigo-200'
                          }`}>
                            {initialChar}
                          </div>
                          <div className="flex flex-col">
                            <span className="font-semibold text-slate-900 text-sm">
                              @{item.username}
                            </span>
                            <span className="text-[11px] text-slate-400 flex items-center gap-1">
                              {getDeviceIcon(item.device, item.status)}
                              <span>{item.device || 'Web Browser'}</span>
                            </span>
                          </div>
                        </div>
                      </td>

                      {/* Status */}
                      <td className="py-3 px-4 whitespace-nowrap">
                        <span className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold ${
                          item.status === 'SUCCESS'
                            ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                            : 'bg-rose-50 text-rose-700 border border-rose-200'
                        }`}>
                          {item.status === 'SUCCESS' ? (
                            <IconCheck size={12} className="stroke-[3]" />
                          ) : (
                            <IconX size={12} className="stroke-[3]" />
                          )}
                          <span>{item.status}</span>
                        </span>
                      </td>

                      {/* Risk Level */}
                      <td className="py-3 px-4 whitespace-nowrap">
                        <span className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-semibold ${
                          isSuspicious
                            ? 'bg-rose-100 text-rose-800 border border-rose-200'
                            : 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                        }`}>
                          {isSuspicious && <IconAlertTriangle size={12} />}
                          <span>{item.risk || (item.status === 'SUCCESS' ? 'Low' : 'High')}</span>
                        </span>
                      </td>

                      {/* IP Address - Separate column with monospace chip */}
                      <td className="py-3 px-4 whitespace-nowrap">
                        <span className="font-mono text-xs font-medium text-indigo-700 bg-indigo-50/80 px-2.5 py-1 rounded-md border border-indigo-100/80 inline-block">
                          {item.ipAddress || '127.0.0.1'}
                        </span>
                      </td>

                      {/* Location - Separate column with clear spacing */}
                      <td className="py-3 px-4 text-xs font-medium text-slate-700 whitespace-nowrap">
                        <div className="flex items-center gap-1.5">
                          <IconGlobe size={14} className="text-slate-400 flex-shrink-0" />
                          <span>{item.location || 'Localhost (Current Session)'}</span>
                        </div>
                      </td>

                      {/* Details / Reason */}
                      <td className="py-3 px-4 text-xs text-slate-500 min-w-[200px]">
                        <span>{item.reason || 'Authenticated Form Login'}</span>
                      </td>
                    </tr>
                  );
                })}

                {filtered.length === 0 && (
                  <tr>
                    <td colSpan="7" className="text-center py-12 text-slate-400">
                      <div className="flex flex-col items-center justify-center gap-2">
                        <IconShield size={32} className="text-slate-300" />
                        <p className="text-sm font-medium text-slate-500">
                          {searchTerm ? `No login events matching "${searchTerm}"` : t('noData')}
                        </p>
                      </div>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};
