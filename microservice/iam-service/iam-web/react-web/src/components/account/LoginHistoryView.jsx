import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { IconCheck, IconX, IconAlertTriangle, IconSearch } from '../common/Icons';
import { Badge } from '../common/Badge';

export const LoginHistoryView = () => {
  const { t } = useI18n();
  const [history, setHistory] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');

  useEffect(() => {
    loadHistory();
  }, []);

  const loadHistory = async () => {
    const list = await api.getLoginHistory();
    setHistory(list);
  };

  const filtered = history.filter(item => {
    const matchesSearch = item.ipAddress.includes(searchTerm) ||
                          item.device.toLowerCase().includes(searchTerm.toLowerCase()) ||
                          item.location.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = statusFilter === 'ALL' || item.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  return (
    <div className="view-container">
      <div className="view-header">
        <div>
          <h2 className="view-title">{t('loginHistoryTitle')}</h2>
          <p className="view-subtitle">{t('loginHistorySubtitle')}</p>
        </div>
      </div>

      <div className="glass-card p-6">
        {/* Filters */}
        <div className="flex flex-col sm:flex-row gap-3 mb-6 justify-between">
          <div className="relative w-full sm:w-72">
            <input
              type="text"
              placeholder={t('search')}
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="form-input pl-9"
            />
            <span className="absolute left-3 top-2.5 text-slate-400">
              <IconSearch size={16} />
            </span>
          </div>

          <div className="flex gap-2">
            {['ALL', 'SUCCESS', 'FAILED'].map(st => (
              <button
                key={st}
                onClick={() => setStatusFilter(st)}
                className={`filter-pill-btn ${statusFilter === st ? 'filter-pill-active' : ''}`}
              >
                {st === 'ALL' ? t('all') : st === 'SUCCESS' ? t('success') : t('failed')}
              </button>
            ))}
          </div>
        </div>

        {/* Table */}
        <div className="table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>{t('time')}</th>
                <th>{t('status')}</th>
                <th>{t('device')}</th>
                <th>{t('ipAddress')}</th>
                <th>{t('location')}</th>
                <th>{t('details')}</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((item) => (
                <tr key={item.id}>
                  <td className="font-mono text-xs text-slate-300">
                    {new Date(item.timestamp).toLocaleString()}
                  </td>
                  <td>
                    <Badge variant={item.status === 'SUCCESS' ? 'success' : 'danger'}>
                      {item.status === 'SUCCESS' ? (
                        <>
                          <IconCheck size={12} />
                          <span>{t('success')}</span>
                        </>
                      ) : (
                        <>
                          <IconX size={12} />
                          <span>{t('failed')}</span>
                        </>
                      )}
                    </Badge>
                  </td>
                  <td className="text-slate-200 font-medium">{item.device}</td>
                  <td className="font-mono text-xs text-indigo-400">{item.ipAddress}</td>
                  <td className="text-slate-300">{item.location}</td>
                  <td className="text-xs text-slate-400">{item.reason}</td>
                </tr>
              ))}
              {filtered.length === 0 && (
                <tr>
                  <td colSpan="6" className="text-center py-8 text-slate-500">
                    {t('noData')}
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
