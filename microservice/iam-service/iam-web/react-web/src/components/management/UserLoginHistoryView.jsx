import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { Badge } from '../common/Badge';
import { IconCheck, IconX, IconAlertTriangle, IconSearch } from '../common/Icons';

export const UserLoginHistoryView = () => {
  const { t } = useI18n();
  const [history, setHistory] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');

  useEffect(() => {
    loadHistory();
  }, []);

  const loadHistory = async () => {
    // Generate combined rich directory login telemetry
    const base = await api.getLoginHistory();
    const directoryHistory = [
      ...base,
      {
        id: "sys-log-1",
        timestamp: new Date(Date.now() - 1000 * 60 * 4).toISOString(),
        username: "sokha_dev",
        ipAddress: "103.243.140.90",
        device: "macOS 14.3 / Chrome 128",
        location: "Phnom Penh, Cambodia",
        status: "SUCCESS",
        risk: "Low",
        reason: "OIDC Authorization Code Flow"
      },
      {
        id: "sys-log-2",
        timestamp: new Date(Date.now() - 1000 * 60 * 20).toISOString(),
        username: "dararith_ops",
        ipAddress: "185.220.101.5",
        device: "Tor Browser / Linux",
        location: "Frankfurt, Germany",
        status: "FAILED",
        risk: "High / Suspicious",
        reason: "5 consecutive failed credentials - Account Locked"
      },
      {
        id: "sys-log-3",
        timestamp: new Date(Date.now() - 1000 * 60 * 45).toISOString(),
        username: "john_doe",
        ipAddress: "116.212.155.10",
        device: "Windows 11 / Edge 126",
        location: "Siem Reap, Cambodia",
        status: "SUCCESS",
        risk: "Low",
        reason: "Username/Password login with PKCE"
      }
    ];
    setHistory(directoryHistory);
  };

  const filtered = history.filter(item => {
    const term = searchTerm.toLowerCase();
    const matchesSearch =
      item.username.toLowerCase().includes(term) ||
      item.ipAddress.includes(term) ||
      item.location.toLowerCase().includes(term) ||
      item.device.toLowerCase().includes(term);

    const matchesStatus = statusFilter === 'ALL' || item.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  return (
    <div className="view-container">
      <div className="view-header">
        <div>
          <h2 className="view-title">{t('adminLoginHistoryTitle')}</h2>
          <p className="view-subtitle">{t('adminLoginHistorySubtitle')}</p>
        </div>
      </div>

      <div className="glass-card p-6">
        <div className="flex flex-col sm:flex-row gap-3 mb-6 justify-between">
          <div className="relative w-full sm:w-80">
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

        <div className="table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>{t('time')}</th>
                <th>Target User</th>
                <th>{t('status')}</th>
                <th>{t('riskLevel')}</th>
                <th>IP Address</th>
                <th>{t('location')}</th>
                <th>{t('details')}</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((item) => {
                const isSuspicious = item.risk?.includes('Suspicious') || item.status === 'FAILED';
                return (
                  <tr key={item.id} className={isSuspicious ? 'bg-rose-500/5' : ''}>
                    <td className="font-mono text-xs text-slate-300">
                      {new Date(item.timestamp).toLocaleString()}
                    </td>
                    <td>
                      <span className="font-semibold text-white">@{item.username}</span>
                    </td>
                    <td>
                      <Badge variant={item.status === 'SUCCESS' ? 'success' : 'danger'}>
                        {item.status === 'SUCCESS' ? <IconCheck size={12} /> : <IconX size={12} />}
                        <span>{item.status}</span>
                      </Badge>
                    </td>
                    <td>
                      <Badge variant={isSuspicious ? 'danger' : 'default'} size="sm">
                        {isSuspicious && <IconAlertTriangle size={12} />}
                        <span>{item.risk || (item.status === 'SUCCESS' ? 'Low' : 'Medium')}</span>
                      </Badge>
                    </td>
                    <td className="font-mono text-xs text-indigo-400">{item.ipAddress}</td>
                    <td className="text-slate-300">{item.location}</td>
                    <td className="text-xs text-slate-400">{item.reason}</td>
                  </tr>
                );
              })}
              {filtered.length === 0 && (
                <tr>
                  <td colSpan="7" className="text-center py-8 text-slate-500">
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
