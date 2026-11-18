import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { Modal } from '../common/Modal';
import { Badge } from '../common/Badge';
import { IconShieldCheck, IconTrash, IconPlus } from '../common/Icons';

export const UserPermAssignModal = ({ user, isOpen, onClose, onUpdated, onShowToast }) => {
  const { t } = useI18n();
  const [availablePerms, setAvailablePerms] = useState([]);
  const [selectedPerm, setSelectedPerm] = useState('');
  const [effect, setEffect] = useState('ALLOW');
  const [reason, setReason] = useState('');
  const [expiresAt, setExpiresAt] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isOpen) {
      loadPerms();
    }
  }, [isOpen]);

  const loadPerms = async () => {
    const perms = await api.getPermissions();
    setAvailablePerms(perms);
    if (perms.length > 0) {
      setSelectedPerm(perms[0].name);
    }
  };

  const handleGrant = async (e) => {
    e.preventDefault();
    if (!selectedPerm) return;
    setLoading(true);
    try {
      await api.grantPermissionToUser(user.uuid, selectedPerm, effect, expiresAt || null, reason);
      onShowToast(`Permission ${selectedPerm} granted to ${user.username}`, 'success');
      setReason('');
      setExpiresAt('');
      onUpdated();
    } catch (err) {
      onShowToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleRevoke = async (permName) => {
    setLoading(true);
    try {
      await api.revokePermissionFromUser(user.uuid, permName);
      onShowToast(`Permission ${permName} revoked from ${user.username}`, 'success');
      onUpdated();
    } catch (err) {
      onShowToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  if (!user) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={`${t('permAssignmentTitle')} (${user.username})`}>
      <div className="space-y-6">
        {/* Current Direct Permissions */}
        <div>
          <span className="text-xs uppercase tracking-wider text-slate-500 font-bold mb-2 block">
            Directly Granted Permissions
          </span>
          <div className="space-y-2">
            {user.directPermissions?.map((p) => (
              <div key={p.name} className="flex items-center justify-between p-3 bg-slate-50 border border-slate-200 rounded-xl text-sm">
                <div className="flex items-center gap-2">
                  <IconShieldCheck size={16} className="text-emerald-600" />
                  <span className="font-mono text-xs font-bold text-slate-900">{p.name}</span>
                  <Badge variant={p.effect === 'ALLOW' ? 'success' : 'danger'} size="sm">
                    {p.effect}
                  </Badge>
                  {p.expiresAt && (
                    <span className="text-xs text-slate-500">Exp: {new Date(p.expiresAt).toLocaleDateString()}</span>
                  )}
                </div>
                <button
                  type="button"
                  onClick={() => handleRevoke(p.name)}
                  className="text-slate-400 hover:text-rose-600 p-1"
                >
                  <IconTrash size={14} />
                </button>
              </div>
            ))}
            {(!user.directPermissions || user.directPermissions.length === 0) && (
              <span className="text-sm text-slate-500 italic block py-2">
                No direct permissions explicitly granted. (Inheriting from roles)
              </span>
            )}
          </div>
        </div>

        {/* Grant New Permission Form */}
        <form onSubmit={handleGrant} className="bg-slate-50 p-4 rounded-2xl border border-slate-200 space-y-4">
          <span className="text-xs uppercase tracking-wider text-emerald-700 font-bold block">
            Grant Direct Permission Override
          </span>

          <div className="form-grid-2">
            <div className="form-group">
              <label className="form-label">{t('tabPermissions')}</label>
              <select
                value={selectedPerm}
                onChange={(e) => setSelectedPerm(e.target.value)}
                className="form-input"
              >
                {availablePerms.map(p => (
                  <option key={p.uuid} value={p.name}>
                    {p.name} ({p.resource})
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label className="form-label">{t('effect')}</label>
              <select
                value={effect}
                onChange={(e) => setEffect(e.target.value)}
                className="form-input"
              >
                <option value="ALLOW">{t('allow')}</option>
                <option value="DENY">{t('deny')}</option>
              </select>
            </div>
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
              placeholder="e.g., Temporary elevated grant for customer audit"
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
