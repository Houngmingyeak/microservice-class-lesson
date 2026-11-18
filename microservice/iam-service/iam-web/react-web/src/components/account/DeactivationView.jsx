import React, { useState } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { IconShieldAlert, IconAlertTriangle, IconLock } from '../common/Icons';
import { Modal } from '../common/Modal';

export const DeactivationView = ({ onShowToast }) => {
  const { t } = useI18n();
  const [reason, setReason] = useState('');
  const [notes, setNotes] = useState('');
  const [password, setPassword] = useState('');
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleOpenModal = (e) => {
    e.preventDefault();
    if (!reason) {
      onShowToast(t('reasonPlaceholder'), 'error');
      return;
    }
    setShowConfirmModal(true);
  };

  const handleConfirmDeactivation = async () => {
    if (!password) {
      onShowToast(t('confirmDeactivationText'), 'error');
      return;
    }
    setLoading(true);
    try {
      await api.requestDeactivation(reason, password, notes);
      onShowToast(t('deactivationSubmitted'), 'success');
      setShowConfirmModal(false);
      setPassword('');
      setReason('');
      setNotes('');
    } catch (err) {
      onShowToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="view-container">
      <div className="view-header">
        <div>
          <h2 className="view-title text-rose-600">{t('deactivationTitle')}</h2>
          <p className="view-subtitle">{t('deactivationSubtitle')}</p>
        </div>
      </div>

      <div className="space-y-6 max-w-3xl">
        {/* Warning Banner */}
        <div className="p-4 bg-rose-50 border border-rose-200 rounded-2xl flex items-start gap-3 text-rose-800 text-sm">
          <IconAlertTriangle size={20} className="shrink-0 mt-0.5 text-rose-600" />
          <div>{t('deactivationWarning')}</div>
        </div>

        {/* Deactivation Form */}
        <div className="glass-card p-6">
          <form onSubmit={handleOpenModal} className="space-y-4">
            <div className="form-group">
              <label className="form-label">{t('reasonLabel')}</label>
              <select
                value={reason}
                onChange={(e) => setReason(e.target.value)}
                className="form-input"
                required
              >
                <option value="">{t('reasonPlaceholder')}</option>
                <option value="Temporary Break">{t('reasonBreak')}</option>
                <option value="Privacy Concerns">{t('reasonPrivacy')}</option>
                <option value="Duplicate Account">{t('reasonDuplicate')}</option>
                <option value="Other">{t('reasonOther')}</option>
              </select>
            </div>

            <div className="form-group">
              <label className="form-label">{t('additionalNotes')}</label>
              <textarea
                rows="3"
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
                placeholder="Let us know what we could improve..."
                className="form-input resize-none"
              />
            </div>

            <div className="pt-2">
              <button
                type="submit"
                className="btn btn-danger"
              >
                {t('submitDeactivation')}
              </button>
            </div>
          </form>
        </div>
      </div>

      {/* Confirmation Password Modal */}
      <Modal
        isOpen={showConfirmModal}
        onClose={() => setShowConfirmModal(false)}
        title={t('deactivationTitle')}
      >
        <div className="space-y-4">
          <p className="text-sm text-slate-600">
            {t('confirmDeactivationText')}
          </p>

          <div className="form-group">
            <input
              type="password"
              placeholder="Your current password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="form-input"
              autoFocus
            />
          </div>

          <div className="flex justify-end gap-2 pt-2">
            <button
              onClick={() => setShowConfirmModal(false)}
              className="btn btn-secondary"
            >
              {t('cancel')}
            </button>
            <button
              onClick={handleConfirmDeactivation}
              disabled={loading}
              className="btn btn-danger"
            >
              {loading ? t('loading') : t('confirm')}
            </button>
          </div>
        </div>
      </Modal>
    </div>
  );
};
