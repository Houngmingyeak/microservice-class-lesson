import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import {
  IconCheck,
  IconX,
  IconSearch,
  IconLaptop,
  IconSmartphone,
  IconShield,
  IconShieldAlert,
  IconActivity,
  IconGlobe
} from '../common/Icons';

export const LoginHistoryView = () => {
  const { t } = useI18n();
  const [history, setHistory] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadHistory();
  }, []);

  const loadHistory = async () => {
    try {
      const list = await api.getLoginHistory();
      setHistory(list || []);
    } finally {
      setLoading(false);
    }
  };

  const filtered = history.filter(item => {
    const term = searchTerm.toLowerCase();
    const matchesSearch =
      (item.ipAddress && item.ipAddress.toLowerCase().includes(term)) ||
      (item.device && item.device.toLowerCase().includes(term)) ||
      (item.location && item.location.toLowerCase().includes(term)) ||
      (item.reason && item.reason.toLowerCase().includes(term));
    const matchesStatus = statusFilter === 'ALL' || item.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  const totalCount = history.length;
  const successCount = history.filter(h => h.status === 'SUCCESS').length;
  const failedCount = history.filter(h => h.status === 'FAILED').length;

  const getDeviceIcon = (deviceStr = '', status = 'SUCCESS') => {
    if (status === 'FAILED') {
      return <IconShieldAlert size={24} className="text-rose-600" />;
    }
    const d = deviceStr.toLowerCase();
    if (d.includes('iphone') || d.includes('android') || d.includes('mobile')) {
      return <IconSmartphone size={24} className="text-sky-600" />;
    }
    return <IconLaptop size={24} className="text-indigo-600" />;
  };

  const getDeviceBg = (deviceStr = '', status = 'SUCCESS') => {
    if (status === 'FAILED') {
      return 'bg-rose-50 border-rose-200';
    }
    const d = deviceStr.toLowerCase();
    if (d.includes('iphone') || d.includes('android') || d.includes('mobile')) {
      return 'bg-sky-50 border-sky-200';
    }
    return 'bg-indigo-50 border-indigo-200';
  };

  return (
    <div className="view-container">
      {/* Header */}
      <div className="view-header">
        <div>
          <h2 className="view-title">{t('loginHistoryTitle') || 'Login Activity History'}</h2>
          <p className="view-subtitle">
            {t('loginHistorySubtitle') || 'Track recent successful and failed sign-in attempts for your account.'}
          </p>
        </div>
      </div>

      {/* Top Stat Summary Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-5 mb-8">
        <div className="p-6 bg-white border border-slate-200 rounded-3xl shadow-sm flex items-center justify-between">
          <div>
            <span className="text-base font-semibold text-slate-500 uppercase tracking-wider block mb-1">
              {t('totalAttempts') || 'Total Attempts'}
            </span>
            <span className="text-3xl font-extrabold text-slate-900">{totalCount}</span>
          </div>
          <div className="w-14 h-14 rounded-full bg-slate-100 border border-slate-200 flex items-center justify-center text-slate-700">
            <IconActivity size={26} />
          </div>
        </div>

        <div className="p-6 bg-white border border-slate-200 rounded-3xl shadow-sm flex items-center justify-between">
          <div>
            <span className="text-base font-semibold text-emerald-600 uppercase tracking-wider block mb-1">
              {t('successfulLogins') || 'Successful'}
            </span>
            <span className="text-3xl font-extrabold text-emerald-700">{successCount}</span>
          </div>
          <div className="w-14 h-14 rounded-full bg-emerald-50 border border-emerald-200 flex items-center justify-center text-emerald-600">
            <IconCheck size={26} />
          </div>
        </div>

        <div className="p-6 bg-white border border-slate-200 rounded-3xl shadow-sm flex items-center justify-between">
          <div>
            <span className="text-base font-semibold text-rose-600 uppercase tracking-wider block mb-1">
              {t('failedAttempts') || 'Failed / Blocked'}
            </span>
            <span className="text-3xl font-extrabold text-rose-700">{failedCount}</span>
          </div>
          <div className="w-14 h-14 rounded-full bg-rose-50 border border-rose-200 flex items-center justify-center text-rose-600">
            <IconShield size={26} />
          </div>
        </div>
      </div>

      {/* Main Container */}
      <div className="bg-white border border-slate-200 rounded-3xl shadow-sm overflow-hidden">
        {/* Search & Filter Toolbar */}
        <div className="p-6 border-b border-slate-100 flex flex-col md:flex-row items-stretch md:items-center justify-between gap-4 bg-slate-50/50">
          {/* Search Box */}
          <div className="relative flex-1 max-w-md">
            <input
              type="text"
              placeholder={t('searchHistory') || 'Search IP, device, location, or details...'}
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
            <button
              type="button"
              onClick={() => setStatusFilter('ALL')}
              className={`px-5 py-2.5 rounded-full font-bold text-base transition cursor-pointer whitespace-nowrap flex items-center gap-2 ${
                statusFilter === 'ALL'
                  ? 'bg-indigo-600 text-white shadow-sm'
                  : 'bg-white border border-slate-200 text-slate-700 hover:bg-slate-100'
              }`}
            >
              <span>{t('all') || 'All'}</span>
              <span className={`px-2 py-0.5 rounded-full text-xs font-semibold ${
                statusFilter === 'ALL' ? 'bg-indigo-700 text-white' : 'bg-slate-100 text-slate-600'
              }`}>
                {totalCount}
              </span>
            </button>

            <button
              type="button"
              onClick={() => setStatusFilter('SUCCESS')}
              className={`px-5 py-2.5 rounded-full font-bold text-base transition cursor-pointer whitespace-nowrap flex items-center gap-2 ${
                statusFilter === 'SUCCESS'
                  ? 'bg-emerald-600 text-white shadow-sm'
                  : 'bg-white border border-slate-200 text-slate-700 hover:bg-slate-100'
              }`}
            >
              <span>{t('success') || 'Success'}</span>
              <span className={`px-2 py-0.5 rounded-full text-xs font-semibold ${
                statusFilter === 'SUCCESS' ? 'bg-emerald-700 text-white' : 'bg-slate-100 text-slate-600'
              }`}>
                {successCount}
              </span>
            </button>

            <button
              type="button"
              onClick={() => setStatusFilter('FAILED')}
              className={`px-5 py-2.5 rounded-full font-bold text-base transition cursor-pointer whitespace-nowrap flex items-center gap-2 ${
                statusFilter === 'FAILED'
                  ? 'bg-rose-600 text-white shadow-sm'
                  : 'bg-white border border-slate-200 text-slate-700 hover:bg-slate-100'
              }`}
            >
              <span>{t('failed') || 'Failed'}</span>
              <span className={`px-2 py-0.5 rounded-full text-xs font-semibold ${
                statusFilter === 'FAILED' ? 'bg-rose-700 text-white' : 'bg-slate-100 text-slate-600'
              }`}>
                {failedCount}
              </span>
            </button>
          </div>
        </div>

        {/* Activity List */}
        <div className="divide-y divide-slate-100">
          {filtered.map((item) => {
            const isSuccess = item.status === 'SUCCESS';

            return (
              <div
                key={item.id}
                className="p-6 flex flex-col lg:flex-row items-start lg:items-center justify-between gap-5 hover:bg-slate-50/70 transition"
              >
                {/* Left Section: Icon + Details */}
                <div className="flex items-start gap-4.5">
                  <div
                    className={`w-14 h-14 rounded-full border flex items-center justify-center shrink-0 mt-0.5 shadow-2xs ${getDeviceBg(
                      item.device,
                      item.status
                    )}`}
                  >
                    {getDeviceIcon(item.device, item.status)}
                  </div>

                  <div>
                    {/* Device name & Status badge */}
                    <div className="flex flex-wrap items-center gap-3">
                      <h4 className="text-xl font-bold text-slate-900">{item.device}</h4>

                      <span
                        className={`inline-flex items-center gap-1.5 px-3.5 py-1 rounded-full text-sm font-semibold border ${
                          isSuccess
                            ? 'bg-emerald-50 text-emerald-700 border-emerald-200'
                            : 'bg-rose-50 text-rose-700 border-rose-200'
                        }`}
                      >
                        {isSuccess ? (
                          <>
                            <IconCheck size={14} className="text-emerald-600" />
                            <span>{t('success') || 'Success'}</span>
                          </>
                        ) : (
                          <>
                            <IconX size={14} className="text-rose-600" />
                            <span>{t('failed') || 'Failed'}</span>
                          </>
                        )}
                      </span>

                      {item.reason && (
                        <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-slate-100 text-slate-600 border border-slate-200">
                          {item.reason}
                        </span>
                      )}
                    </div>

                    {/* Metadata: Timestamp, IP, Location */}
                    <div className="mt-2.5 flex flex-wrap items-center gap-x-6 gap-y-1.5 text-base text-slate-600">
                      <span className="flex items-center gap-1.5">
                        <strong className="text-slate-800 font-semibold">{t('time') || 'Time'}:</strong>{' '}
                        <span className="text-slate-600 font-mono">
                          {new Date(item.timestamp).toLocaleString(undefined, {
                            dateStyle: 'medium',
                            timeStyle: 'medium'
                          })}
                        </span>
                      </span>

                      <span className="flex items-center gap-1.5">
                        <strong className="text-slate-800 font-semibold">IP:</strong>{' '}
                        <span className="px-2.5 py-0.5 rounded-full bg-slate-100 font-mono text-indigo-600 font-bold text-sm border border-slate-200">
                          {item.ipAddress}
                        </span>
                      </span>

                      <span className="flex items-center gap-1.5">
                        <strong className="text-slate-800 font-semibold">{t('location') || 'Location'}:</strong>{' '}
                        <span className="text-slate-700">{item.location}</span>
                      </span>
                    </div>
                  </div>
                </div>

                {/* Right Section: Status Indicator */}
                <div className="self-end lg:self-center shrink-0">
                  <span
                    className={`inline-flex items-center gap-2 px-4 py-1.5 rounded-full text-sm font-semibold border ${
                      isSuccess
                        ? 'bg-slate-50 text-slate-700 border-slate-200'
                        : 'bg-rose-50/80 text-rose-700 border-rose-200'
                    }`}
                  >
                    <span
                      className={`w-2.5 h-2.5 rounded-full ${
                        isSuccess ? 'bg-emerald-500' : 'bg-rose-500'
                      }`}
                    ></span>
                    <span>{isSuccess ? 'Authorized' : 'Rejected'}</span>
                  </span>
                </div>
              </div>
            );
          })}

          {filtered.length === 0 && (
            <div className="py-16 text-center">
              <div className="w-16 h-16 rounded-full bg-slate-100 border border-slate-200 flex items-center justify-center text-slate-400 mx-auto mb-4">
                <IconSearch size={28} />
              </div>
              <h4 className="text-xl font-bold text-slate-800 mb-1">
                {t('noResultsFound') || 'No login activities found'}
              </h4>
              <p className="text-base text-slate-500">
                {searchTerm
                  ? 'No sign-in records matched your search query. Try another keyword.'
                  : 'There are no recorded login attempts matching this filter.'}
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
