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
            className="px-6 py-2.5 rounded-full bg-rose-50 text-rose-600 border border-rose-200 hover:bg-rose-600 hover:text-white font-semibold text-lg flex items-center gap-2 shadow-sm transition"
          >
            <IconLogOut size={18} />
            <span>{t('terminateAllSessions')}</span>
          </button>
        )}
      </div>

      <div className="bg-white border border-slate-200 rounded-3xl shadow-sm divide-y divide-slate-100 overflow-hidden">
        {sessions.map((sess) => (
          <div key={sess.id} className="p-6 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-5 hover:bg-slate-50/60 transition">
            <div className="flex items-start gap-4">
              <div className="w-14 h-14 rounded-full bg-indigo-50 border border-indigo-100 flex items-center justify-center text-indigo-600 shrink-0 mt-1">
                {getDeviceIcon(sess.deviceType)}
              </div>
              <div className="space-y-1.5">
                <div className="flex flex-wrap items-center gap-2.5">
                  <h4 className="text-xl font-bold text-slate-900">{sess.deviceName}</h4>
                  {sess.isCurrent && (
                    <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-sm font-semibold bg-emerald-50 text-emerald-700 border border-emerald-200">
                      <span className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
                      {t('currentDevice')}
                    </span>
                  )}
                  <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-semibold bg-slate-100 text-slate-700 border border-slate-200">
                    {sess.browser}
                  </span>
                </div>
                <div className="text-base text-slate-600 flex flex-wrap gap-x-6 gap-y-1">
                  <span><strong className="text-slate-800">IP:</strong> {sess.ipAddress}</span>
                  <span><strong className="text-slate-800">{t('location')}:</strong> {sess.location}</span>
                  <span><strong className="text-slate-800">{t('lastActive')}:</strong> {sess.lastActive}</span>
                </div>
              </div>
            </div>

            <div>
              {!sess.isCurrent && (
                <button
                  onClick={() => setTerminateTarget(sess)}
                  className="px-5 py-2 rounded-full bg-rose-50 text-rose-600 border border-rose-200 hover:bg-rose-600 hover:text-white font-semibold text-base flex items-center gap-1.5 transition shadow-sm"
                >
                  <IconTrash size={16} />
                  <span>{t('terminateSession')}</span>
                </button>
              )}
            </div>
          </div>
        ))}
        {sessions.length === 0 && (
          <div className="p-12 text-center text-slate-500 text-lg">
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
        <div className="space-y-5">
          <p className="text-lg text-slate-600">
            Terminate session on <strong>{terminateTarget?.deviceName}</strong> ({terminateTarget?.ipAddress})? This user will be immediately signed out.
          </p>
          <div className="flex justify-end gap-3 pt-2">
            <button onClick={() => setTerminateTarget(null)} className="px-6 py-2.5 rounded-full bg-slate-100 hover:bg-slate-200 text-slate-700 font-semibold text-lg transition">
              {t('cancel')}
            </button>
            <button onClick={handleTerminateSingle} className="px-6 py-2.5 rounded-full bg-rose-600 hover:bg-rose-700 text-white font-semibold text-lg shadow-sm transition">
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
        <div className="space-y-5">
          <p className="text-lg text-slate-600">
            {t('terminateAllConfirm')}
          </p>
          <div className="flex justify-end gap-3 pt-2">
            <button onClick={() => setShowTerminateAllModal(false)} className="px-6 py-2.5 rounded-full bg-slate-100 hover:bg-slate-200 text-slate-700 font-semibold text-lg transition">
              {t('cancel')}
            </button>
            <button onClick={handleTerminateAll} className="px-6 py-2.5 rounded-full bg-rose-600 hover:bg-rose-700 text-white font-semibold text-lg shadow-sm transition">
              {t('confirm')}
            </button>
          </div>
        </div>
      </Modal>
    </div>
  );
};
