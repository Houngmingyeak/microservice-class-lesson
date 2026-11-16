import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { IconLaptop, IconSmartphone, IconTrash, IconLogOut, IconCheck, IconAlertTriangle } from '../common/Icons';
import { Badge } from '../common/Badge';
import { Modal } from '../common/Modal';

export const ActiveSessionsView = ({ onShowToast }) => {
  const { t } = useI18n();
  const [sessions, setSessions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [terminateTarget, setTerminateTarget] = useState(null);
  const [showTerminateAllModal, setShowTerminateAllModal] = useState(false);

  useEffect(() => {
    loadSessions();
  }, []);

  const loadSessions = async () => {
    try {
      const list = await api.getActiveSessions();
      setSessions(list);
    } catch (err) {
      onShowToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleTerminateSingle = async () => {
    if (!terminateTarget) return;
    try {
      const updated = await api.terminateSession(terminateTarget.id);
      setSessions(updated);
      onShowToast(t('sessionTerminated'), 'success');
      setTerminateTarget(null);
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleTerminateAll = async () => {
    try {
      const updated = await api.terminateAllOtherSessions();
      setSessions(updated);
      onShowToast(t('sessionTerminated'), 'success');
      setShowTerminateAllModal(false);
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const getDeviceIcon = (type) => {
    if (type?.toLowerCase() === 'mobile') {
      return <IconSmartphone size={22} className="text-sky-400" />;
    }
    return <IconLaptop size={22} className="text-indigo-400" />;
  };

  return (
    <div className="view-container">
      <div className="view-header flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h2 className="view-title">{t('activeSessionsTitle')}</h2>
          <p className="view-subtitle">{t('activeSessionsSubtitle')}</p>
        </div>
        {sessions.length > 1 && (
          <button
            onClick={() => setShowTerminateAllModal(true)}
            className="btn btn-danger self-start sm:self-auto flex items-center gap-2"
          >
            <IconLogOut size={16} />
            <span>{t('terminateAllSessions')}</span>
          </button>
        )}
      </div>

      <div className="glass-card divide-y divide-slate-800">
        {sessions.map((sess) => (
          <div key={sess.id} className="p-6 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
            <div className="flex items-start gap-4">
              <div className="p-3 bg-slate-900 border border-slate-800 rounded-xl mt-1">
                {getDeviceIcon(sess.deviceType)}
              </div>
              <div className="space-y-1">
                <div className="flex flex-wrap items-center gap-2">
                  <h4 className="text-base font-semibold text-white">{sess.deviceName}</h4>
                  {sess.isCurrent && (
                    <Badge variant="success" size="sm">
                      <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span>
                      {t('currentDevice')}
                    </Badge>
                  )}
                  <Badge variant="default" size="sm">{sess.browser}</Badge>
                </div>
                <div className="text-xs text-slate-400 flex flex-wrap gap-x-4 gap-y-1">
                  <span><strong>IP:</strong> {sess.ipAddress}</span>
                  <span><strong>{t('location')}:</strong> {sess.location}</span>
                  <span><strong>{t('lastActive')}:</strong> {sess.lastActive}</span>
                </div>
              </div>
            </div>

            <div>
              {!sess.isCurrent && (
                <button
                  onClick={() => setTerminateTarget(sess)}
                  className="btn btn-secondary text-rose-400 hover:border-rose-500/50 flex items-center gap-1.5"
                >
                  <IconTrash size={15} />
                  <span>{t('terminateSession')}</span>
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Terminate Single Modal */}
      <Modal
        isOpen={!!terminateTarget}
        onClose={() => setTerminateTarget(null)}
        title={t('terminateSession')}
      >
        <div className="space-y-4">
          <p className="text-sm text-slate-300">
            Terminate session on <strong>{terminateTarget?.deviceName}</strong> ({terminateTarget?.ipAddress})? This user will be immediately signed out.
          </p>
          <div className="flex justify-end gap-2 pt-2">
            <button onClick={() => setTerminateTarget(null)} className="btn btn-secondary">
              {t('cancel')}
            </button>
            <button onClick={handleTerminateSingle} className="btn btn-danger">
              {t('terminate')}
            </button>
          </div>
        </div>
      </Modal>

      {/* Terminate All Modal */}
      <Modal
        isOpen={showTerminateAllModal}
        onClose={() => setShowTerminateAllModal(false)}
        title={t('terminateAllSessions')}
      >
        <div className="space-y-4">
          <p className="text-sm text-slate-300">
            {t('terminateAllConfirm')}
          </p>
          <div className="flex justify-end gap-2 pt-2">
            <button onClick={() => setShowTerminateAllModal(false)} className="btn btn-secondary">
              {t('cancel')}
            </button>
            <button onClick={handleTerminateAll} className="btn btn-danger">
              {t('confirm')}
            </button>
          </div>
        </div>
      </Modal>
    </div>
  );
};
