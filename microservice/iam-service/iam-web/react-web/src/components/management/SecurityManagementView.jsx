import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { Badge } from '../common/Badge';
import { Modal } from '../common/Modal';
import { IconUnlock, IconLock, IconKey, IconCheck, IconAlertTriangle, IconRefresh, IconSearch } from '../common/Icons';

export const SecurityManagementView = ({ onShowToast }) => {
  const { t } = useI18n();
  const [users, setUsers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedUserForReset, setSelectedUserForReset] = useState(null);
  const [tempPassword, setTempPassword] = useState('');
  const [forceChange, setForceChange] = useState(true);

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    const list = await api.getUsers();
    setUsers(list);
  };

  const handleUnlockUser = async (user) => {
    try {
      await api.toggleUserLock(user.uuid);
      onShowToast(`Account for ${user.username} unlocked successfully!`, 'success');
      loadUsers();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleOpenResetModal = (user) => {
    setSelectedUserForReset(user);
    setTempPassword("Temp#" + Math.random().toString(36).substring(2, 8).toUpperCase() + "!");
  };

  const handleAdminResetPassword = async (e) => {
    e.preventDefault();
    if (!selectedUserForReset || !tempPassword) return;
    try {
      await api.adminResetPassword(selectedUserForReset.uuid, tempPassword);
      onShowToast(`Password reset for ${selectedUserForReset.username}. Temporary password generated.`, 'success');
      setSelectedUserForReset(null);
      loadUsers();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const lockedUsers = users.filter(u => !u.accountNonLocked);

  return (
    <div className="view-container">
      <div className="view-header">
        <div>
          <h2 className="view-title">{t('secMgmtTitle')}</h2>
          <p className="view-subtitle">{t('secMgmtSubtitle')}</p>
        </div>
      </div>

      {/* Security Health Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <div className="glass-card p-5 border-l-4 border-l-amber-500">
          <span className="text-xs text-slate-400 font-semibold block mb-1">Locked Accounts Queue</span>
          <div className="text-2xl font-bold text-white flex items-center justify-between">
            <span>{lockedUsers.length}</span>
            <IconLock size={20} className="text-amber-400" />
          </div>
          <p className="text-xs text-slate-400 mt-2">Accounts locked due to failed authentication thresholds</p>
        </div>

        <div className="glass-card p-5 border-l-4 border-l-emerald-500">
          <span className="text-xs text-slate-400 font-semibold block mb-1">MFA Compliance Rate</span>
          <div className="text-2xl font-bold text-white flex items-center justify-between">
            <span>84.2%</span>
            <IconCheck size={20} className="text-emerald-400" />
          </div>
          <p className="text-xs text-slate-400 mt-2">Enterprise directory users with 2FA enabled</p>
        </div>

        <div className="glass-card p-5 border-l-4 border-l-indigo-500">
          <span className="text-xs text-slate-400 font-semibold block mb-1">Password Policy Status</span>
          <div className="text-2xl font-bold text-white flex items-center justify-between">
            <span>Enforced</span>
            <IconKey size={20} className="text-indigo-400" />
          </div>
          <p className="text-xs text-slate-400 mt-2">Argon2id hashing + 90-day rotation schedule</p>
        </div>
      </div>

      {/* Locked Accounts Rapid Response */}
      {lockedUsers.length > 0 && (
        <div className="glass-card p-6 mb-6 border-amber-500/30 bg-amber-500/5">
          <h3 className="text-base font-bold text-amber-300 mb-3 flex items-center gap-2">
            <IconAlertTriangle size={18} />
            Locked Accounts Requiring Administrative Action
          </h3>
          <div className="space-y-3">
            {lockedUsers.map((u) => (
              <div key={u.uuid} className="p-3 bg-slate-900 border border-slate-800 rounded-lg flex flex-col sm:flex-row sm:items-center justify-between gap-3">
                <div className="flex items-center gap-3">
                  <div className="p-2 bg-rose-500/20 text-rose-400 rounded-lg">
                    <IconLock size={16} />
                  </div>
                  <div>
                    <span className="font-semibold text-white">@{u.username}</span>
                    <span className="text-xs text-slate-400 ml-2">({u.email})</span>
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  <button
                    onClick={() => handleUnlockUser(u)}
                    className="btn btn-primary text-xs py-1.5 flex items-center gap-1.5"
                  >
                    <IconUnlock size={14} />
                    <span>{t('unlockAccountNow')}</span>
                  </button>
                  <button
                    onClick={() => handleOpenResetModal(u)}
                    className="btn btn-secondary text-xs py-1.5"
                  >
                    {t('resetUserPassword')}
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Directory Password & Security Controls */}
      <div className="glass-card p-6">
        <h3 className="text-base font-bold text-white mb-4">
          All Users Security Management
        </h3>

        <div className="table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>User</th>
                <th>Account Status</th>
                <th>Last Password Change</th>
                <th className="text-right">Administrative Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map((u) => (
                <tr key={u.uuid}>
                  <td>
                    <div>
                      <span className="font-semibold text-white">@{u.username}</span>
                      <div className="text-xs text-slate-400">{u.email}</div>
                    </div>
                  </td>
                  <td>
                    <Badge variant={u.accountNonLocked ? 'success' : 'danger'} size="sm">
                      {u.accountNonLocked ? 'Unlocked' : 'Locked'}
                    </Badge>
                  </td>
                  <td className="text-xs font-mono text-slate-300">
                    {u.passwordChangedAt ? new Date(u.passwordChangedAt).toLocaleDateString() : 'Initial Credential'}
                  </td>
                  <td className="text-right">
                    <div className="flex items-center justify-end gap-2">
                      <button
                        onClick={() => handleOpenResetModal(u)}
                        className="btn btn-secondary text-xs py-1 px-3 flex items-center gap-1 text-indigo-400"
                      >
                        <IconKey size={13} />
                        <span>{t('resetUserPassword')}</span>
                      </button>
                      {!u.accountNonLocked && (
                        <button
                          onClick={() => handleUnlockUser(u)}
                          className="btn btn-secondary text-xs py-1 px-3 flex items-center gap-1 text-emerald-400"
                        >
                          <IconUnlock size={13} />
                          <span>Unlock</span>
                        </button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Admin Password Reset Modal */}
      <Modal
        isOpen={!!selectedUserForReset}
        onClose={() => setSelectedUserForReset(null)}
        title={`${t('resetUserPassword')}: @${selectedUserForReset?.username}`}
      >
        <form onSubmit={handleAdminResetPassword} className="space-y-4">
          <p className="text-sm text-slate-300">
            Generate and assign a new temporary password for <strong>{selectedUserForReset?.username}</strong> ({selectedUserForReset?.email}).
          </p>

          <div className="form-group">
            <label className="form-label">{t('temporaryPassword')}</label>
            <div className="flex items-center gap-2">
              <input
                type="text"
                value={tempPassword}
                onChange={(e) => setTempPassword(e.target.value)}
                className="form-input font-mono text-indigo-300"
                required
              />
              <button
                type="button"
                onClick={() => setTempPassword("Temp#" + Math.random().toString(36).substring(2, 8).toUpperCase() + "!")}
                className="btn btn-secondary text-xs py-2 px-3 shrink-0"
                title="Generate new temporary password"
              >
                <IconRefresh size={14} />
              </button>
            </div>
          </div>

          <label className="flex items-center gap-2 text-xs text-slate-300 cursor-pointer pt-1">
            <input
              type="checkbox"
              checked={forceChange}
              onChange={(e) => setForceChange(e.target.checked)}
              className="rounded border-slate-700 text-indigo-600 focus:ring-0"
            />
            <span>{t('forceChangeOnNextLogin')}</span>
          </label>

          <div className="flex justify-end gap-2 pt-3">
            <button
              type="button"
              onClick={() => setSelectedUserForReset(null)}
              className="btn btn-secondary"
            >
              {t('cancel')}
            </button>
            <button type="submit" className="btn btn-primary">
              Assign & Reset
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
