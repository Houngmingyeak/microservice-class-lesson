import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { Modal } from '../common/Modal';
import { Badge } from '../common/Badge';
import { IconKey, IconTrash, IconPlus, IconCheck } from '../common/Icons';

export const UserRoleAssignModal = ({ user, isOpen, onClose, onUpdated, onShowToast }) => {
  const { t } = useI18n();
  const [availableRoles, setAvailableRoles] = useState([]);
  const [selectedRole, setSelectedRole] = useState('');
  const [reason, setReason] = useState('');
  const [expiresAt, setExpiresAt] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isOpen) {
      loadRoles();
    }
  }, [isOpen]);

  const loadRoles = async () => {
    const roles = await api.getRoles();
    setAvailableRoles(roles);
    if (roles.length > 0) {
      setSelectedRole(roles[0].name);
    }
  };

  const handleAssign = async (e) => {
    e.preventDefault();
    if (!selectedRole) return;
    setLoading(true);
    try {
      await api.assignRolesToUser(user.uuid, [selectedRole], reason, expiresAt || null);
      onShowToast(`Role ${selectedRole} assigned to ${user.username}`, 'success');
      setReason('');
      setExpiresAt('');
      onUpdated();
    } catch (err) {
      onShowToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleRevoke = async (roleName) => {
    setLoading(true);
    try {
      await api.revokeRoleFromUser(user.uuid, roleName, "Revoked via admin dashboard");
      onShowToast(`Role ${roleName} revoked from ${user.username}`, 'success');
      onUpdated();
    } catch (err) {
      onShowToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  if (!user) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={`${t('roleAssignmentTitle')} (${user.username})`}>
      <div className="space-y-6">
        {/* Current Assigned Roles */}
        <div>
          <span className="text-xs uppercase tracking-wider text-slate-500 font-bold mb-2 block">
            Currently Assigned Roles
          </span>
          <div className="flex flex-wrap gap-2">
            {user.roles?.map((r) => (
              <div key={r} className="inline-flex items-center gap-2 bg-indigo-50 border border-indigo-200 px-3 py-1.5 rounded-xl text-sm">
                <IconKey size={14} className="text-indigo-600" />
                <span className="font-bold text-indigo-950">{r}</span>
                <button
                  type="button"
                  onClick={() => handleRevoke(r)}
                  className="text-slate-400 hover:text-rose-600 transition ml-1"
                  title="Revoke role"
                >
                  <IconTrash size={14} />
                </button>
              </div>
            ))}
            {(!user.roles || user.roles.length === 0) && (
              <span className="text-sm text-slate-500 italic">No roles currently assigned</span>
            )}
          </div>
        </div>

        {/* Assign New Role Form */}
        <form onSubmit={handleAssign} className="bg-slate-50 p-4 rounded-2xl border border-slate-200 space-y-4">
          <span className="text-xs uppercase tracking-wider text-indigo-700 font-bold block">
            Assign Additional Role
          </span>

          <div className="form-group">
            <label className="form-label">{t('tabRoles')}</label>
            <select
              value={selectedRole}
              onChange={(e) => setSelectedRole(e.target.value)}
              className="form-input"
            >
              {availableRoles.map(r => (
                <option key={r.uuid} value={r.name}>
                  {r.name} - {r.description}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label className="form-label">{t('expiresAt')}</label>
            <input
              type="datetime-local"
              value={expiresAt}
              onChange={(e) => setExpiresAt(e.target.value)}
              className="form-input"
            />
          </div>

          <div className="form-group">
            <label className="form-label">{t('assignmentReason')}</label>
            <input
              type="text"
              placeholder="e.g., Authorized by Tech Lead for microservice access"
              value={reason}
              onChange={(e) => setReason(e.target.value)}
              className="form-input"
            />
          </div>

          <div className="flex justify-end gap-2 pt-2">
            <button type="button" onClick={onClose} className="btn btn-secondary">
              {t('close')}
            </button>
            <button type="submit" disabled={loading} className="btn btn-primary flex items-center gap-1.5">
              <IconPlus size={15} />
              <span>{t('assign')}</span>
            </button>
          </div>
        </form>
      </div>
    </Modal>
  );
};
