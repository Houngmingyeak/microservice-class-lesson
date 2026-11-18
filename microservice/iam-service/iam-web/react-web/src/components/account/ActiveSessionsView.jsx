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
      <div className="view-header flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 mb-4">
        <div>
          <h2 className="view-title">{t('activeSessionsTitle')}</h2>
          <p className="view-subtitle">{t('activeSessionsSubtitle')}</p>
        </div>
        {sessions.length > 1 && (
          <button
            onClick={() => setShowTerminateAllModal(true)}
            className="px-4 py-2 rounded-full bg-rose-50 text-rose-600 border border-rose-200 hover:bg-rose-600 hover:text-white font-semibold text-xs flex items-center gap-1.5 shadow-sm transition whitespace-nowrap self-start sm:self-auto"
          >
            <IconLogOut size={15} />
            <span>{t('terminateAllSessions')}</span>
          </button>
        )}
      </div>

      <div className="bg-white border border-slate-200 rounded-2xl shadow-sm divide-y divide-slate-100 overflow-hidden">
        {sessions.map((sess) => (
          <div key={sess.id} className="p-4 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 hover:bg-slate-50/60 transition">
            <div className="flex items-center gap-3.5">
              <div className="w-10 h-10 rounded-full bg-indigo-50 border border-indigo-100 flex items-center justify-center text-indigo-600 shrink-0">
                {getDeviceIcon(sess.deviceType)}
              </div>
              <div className="space-y-1">
                <div className="flex flex-wrap items-center gap-2">
                  <h4 className="text-sm font-bold text-slate-900 whitespace-nowrap">{sess.deviceName}</h4>
                  {sess.isCurrent && (
                    <span className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-semibold bg-emerald-50 text-emerald-700 border border-emerald-200 whitespace-nowrap">
                      <span className="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse"></span>
                      {t('currentDevice')}
                    </span>
                  )}
                  <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-slate-100 text-slate-700 border border-slate-200 whitespace-nowrap">
                    {sess.browser}
                  </span>
                </div>
                <div className="text-xs text-slate-500 flex flex-wrap gap-x-4 gap-y-1">
                  <span className="whitespace-nowrap"><strong className="text-slate-700">IP:</strong> {sess.ipAddress}</span>
                  <span className="whitespace-nowrap"><strong className="text-slate-700">{t('location')}:</strong> {sess.location}</span>
                  <span className="whitespace-nowrap"><strong className="text-slate-700">{t('lastActive')}:</strong> {sess.lastActive}</span>
                </div>
              </div>
            </div>

            <div>
              {!sess.isCurrent && (
                <button
                  onClick={() => setTerminateTarget(sess)}
                  className="px-3.5 py-1.5 rounded-full bg-rose-50 text-rose-600 border border-rose-200 hover:bg-rose-600 hover:text-white font-semibold text-xs flex items-center gap-1 transition shadow-sm whitespace-nowrap"
                >
                  <IconTrash size={14} />
                  <span>{t('terminateSession')}</span>
                </button>
              )}
            </div>
          </div>
        ))}
        {sessions.length === 0 && (
          <div className="p-8 text-center text-slate-500 text-sm">
            {t('noData')}
          </div>
        )}
      </div>

      {/* Terminate Single Modal */}
      <Modal
        isOpen={!!terminateTarget}
        onClose={() => setTerminateTarget(null)}
        title={t('terminateSession')}
      >
        <div className="space-y-4">
          <p className="text-sm text-slate-600 leading-relaxed">
            Terminate session on <strong>{terminateTarget?.deviceName}</strong> ({terminateTarget?.ipAddress})? This user will be immediately signed out.
          </p>
          <div className="flex justify-end gap-2 pt-2">
            <button onClick={() => setTerminateTarget(null)} className="btn btn-secondary text-xs">
              {t('cancel')}
            </button>
            <button onClick={handleTerminateSingle} className="btn btn-danger text-xs">
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
          <p className="text-sm text-slate-600 leading-relaxed">
            {t('terminateAllConfirm')}
          </p>
          <div className="flex justify-end gap-2 pt-2">
            <button onClick={() => setShowTerminateAllModal(false)} className="btn btn-secondary text-xs">
              {t('cancel')}
            </button>
            <button onClick={handleTerminateAll} className="btn btn-danger text-xs">
              {t('confirm')}
            </button>
          </div>
        </div>
      </Modal>
    </div>
  );
};
