import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { Badge } from '../common/Badge';
import { Modal } from '../common/Modal';
import { IconLaptop, IconSmartphone, IconTrash, IconSearch, IconLogOut } from '../common/Icons';

export const UserSessionsView = ({ onShowToast }) => {
  const { t } = useI18n();
  const [sessions, setSessions] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [terminatingSession, setTerminatingSession] = useState(null);
  const [terminatingUser, setTerminatingUser] = useState(null);

  useEffect(() => {
    loadSessions();
  }, []);

  const loadSessions = async () => {
    const list = await api.getActiveSessions();
    setSessions(list);
  };

  const handleTerminateSession = async () => {
    if (!terminatingSession) return;
    try {
      const updated = await api.terminateSession(terminatingSession.id);
      setSessions(updated);
      onShowToast(`Session on ${terminatingSession.deviceName} terminated`, 'success');
      setTerminatingSession(null);
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleTerminateAllForUser = async () => {
    if (!terminatingUser) return;
    try {
      let current = [...sessions];
      current = current.filter(s => s.username !== terminatingUser);
      setSessions(current);
      onShowToast(`All active sessions terminated for ${terminatingUser}`, 'success');
      setTerminatingUser(null);
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const filtered = sessions.filter(s =>
    s.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
    s.ipAddress.includes(searchTerm) ||
    s.deviceName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    s.location.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="view-container">
      <div className="view-header">
        <div>
          <h2 className="view-title">{t('adminSessionsTitle')}</h2>
          <p className="view-subtitle">{t('adminSessionsSubtitle')}</p>
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

          <Badge variant="primary">
            {sessions.length} Live Sessions Connected
          </Badge>
        </div>

        <div className="table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>User</th>
                <th>Device</th>
                <th>IP Address</th>
                <th>Location</th>
                <th>Last Active</th>
                <th className="text-right">{t('actions')}</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((s) => (
                <tr key={s.id}>
                  <td>
                    <div className="flex items-center gap-2">
                      <span className="font-bold text-slate-900 text-sm">@{s.username}</span>
                      {s.isCurrent && (
                        <Badge variant="success" size="sm">Current</Badge>
                      )}
                    </div>
                  </td>
                  <td>
                    <div className="flex items-center gap-2 text-slate-700 font-medium">
                      {s.deviceType === 'Mobile' ? (
                        <IconSmartphone size={16} className="text-sky-600" />
                      ) : (
                        <IconLaptop size={16} className="text-indigo-600" />
                      )}
                      <span>{s.deviceName} ({s.browser})</span>
                    </div>
                  </td>
                  <td className="font-mono text-xs text-indigo-600 font-semibold">{s.ipAddress}</td>
                  <td className="text-slate-600">{s.location}</td>
                  <td className="text-xs text-slate-500">{s.lastActive}</td>
                  <td className="text-right">
                    <div className="flex items-center justify-end gap-1.5">
                      <button
                        onClick={() => setTerminatingSession(s)}
                        className="btn btn-secondary text-rose-600 text-xs py-1 px-3 flex items-center gap-1 hover:bg-rose-50 hover:border-rose-300"
                        title={t('terminateSession')}
                      >
                        <IconTrash size={13} />
                        <span>Terminate</span>
                      </button>
                      <button
                        onClick={() => setTerminatingUser(s.username)}
                        className="btn btn-secondary text-amber-600 text-xs py-1 px-3 flex items-center gap-1 hover:bg-amber-50 hover:border-amber-300"
                        title={t('terminateAllUserSessions')}
                      >
                        <IconLogOut size={13} />
                        <span>All for User</span>
                      </button>
                    </div>
                  </td>
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

      {/* Terminate Single Session Modal */}
      <Modal
        isOpen={!!terminatingSession}
        onClose={() => setTerminatingSession(null)}
        title={t('terminateSession')}
      >
        <div className="space-y-4">
          <p className="text-sm text-slate-600">
            Forcibly terminate session on <strong>{terminatingSession?.deviceName}</strong> for user <strong>@{terminatingSession?.username}</strong> ({terminatingSession?.ipAddress})?
          </p>
          <div className="flex justify-end gap-2 pt-2">
            <button onClick={() => setTerminatingSession(null)} className="btn btn-secondary">
              {t('cancel')}
            </button>
            <button onClick={handleTerminateSession} className="btn btn-danger">
              {t('terminate')}
            </button>
          </div>
        </div>
      </Modal>

      {/* Terminate All for User Modal */}
      <Modal
        isOpen={!!terminatingUser}
        onClose={() => setTerminatingUser(null)}
        title={t('terminateAllUserSessions')}
      >
        <div className="space-y-4">
          <p className="text-sm text-slate-600">
            Terminate all active concurrent sessions across all devices for user <strong>@{terminatingUser}</strong>?
          </p>
          <div className="flex justify-end gap-2 pt-2">
            <button onClick={() => setTerminatingUser(null)} className="btn btn-secondary">
              {t('cancel')}
            </button>
            <button onClick={handleTerminateAllForUser} className="btn btn-danger">
              {t('confirm')}
            </button>
          </div>
        </div>
      </Modal>
    </div>
  );
};
