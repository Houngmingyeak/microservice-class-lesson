import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { IconGoogle, IconGithub, IconFacebook, IconTelegram, IconCheck, IconX, IconGlobe } from '../common/Icons';
import { Badge } from '../common/Badge';
import { Modal } from '../common/Modal';

export const LinkedAccountsView = ({ onShowToast }) => {
  const { t } = useI18n();
  const [identities, setIdentities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedForUnlink, setSelectedForUnlink] = useState(null);

  useEffect(() => {
    loadAccounts();
  }, []);

  const loadAccounts = async () => {
    try {
      const list = await api.getLinkedAccounts();
      setIdentities(list);
    } catch (err) {
      onShowToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleLink = async (provider) => {
    try {
      const updated = await api.linkAccount(provider);
      setIdentities(updated);
      onShowToast(t('linkSuccess'), 'success');
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleConfirmUnlink = async () => {
    if (!selectedForUnlink) return;
    try {
      const updated = await api.unlinkAccount(selectedForUnlink.uuid);
      setIdentities(updated);
      onShowToast(t('unlinkSuccess'), 'success');
      setSelectedForUnlink(null);
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const getProviderIcon = (provider) => {
    switch (provider?.toLowerCase()) {
      case 'google': return <IconGoogle size={24} />;
      case 'github': return <IconGithub size={24} />;
      case 'facebook': return <IconFacebook size={24} />;
      case 'telegram': return <IconTelegram size={24} />;
      default: return <IconGlobe size={24} />;
    }
  };

  return (
    <div className="view-container">
      <div className="view-header">
        <div>
          <h2 className="view-title">{t('linkedAccountsTitle')}</h2>
          <p className="view-subtitle">{t('linkedAccountsSubtitle')}</p>
        </div>
      </div>

      <div className="glass-card divide-y divide-slate-800">
        {identities.map((item) => (
          <div key={item.uuid} className="p-6 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
            <div className="flex items-center gap-4">
              <div className="p-3 bg-slate-900 border border-slate-800 rounded-xl">
                {getProviderIcon(item.provider)}
              </div>
              <div>
                <div className="flex items-center gap-2">
                  <h4 className="text-base font-semibold text-white">{item.provider}</h4>
                  <Badge variant={item.connected ? 'success' : 'default'}>
                    {item.connected ? t('connected') : t('notConnected')}
                  </Badge>
                </div>
                <p className="text-sm text-slate-400 mt-0.5">
                  {item.connected ? (item.displayName || item.email || item.providerSubject) : `No ${item.provider} account currently associated`}
                </p>
                {item.connected && item.linkedAt && (
                  <span className="text-xs text-slate-500 block mt-1">
                    Linked on {new Date(item.linkedAt).toLocaleDateString()}
                  </span>
                )}
              </div>
            </div>

            <div className="flex items-center gap-2 self-end sm:self-center">
              {item.connected ? (
                <button
                  onClick={() => setSelectedForUnlink(item)}
                  className="btn btn-secondary text-rose-400 hover:border-rose-500/50"
                >
                  {t('unlinkAccount')}
                </button>
              ) : (
                <button
                  onClick={() => handleLink(item.provider)}
                  className="btn btn-primary"
                >
                  {t('linkAccount')}
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Unlink Modal */}
      <Modal
        isOpen={!!selectedForUnlink}
        onClose={() => setSelectedForUnlink(null)}
        title={t('unlinkAccount')}
      >
        <div className="space-y-4">
          <p className="text-sm text-slate-300">
            {t('unlinkConfirm')}
          </p>
          <div className="p-3 bg-slate-950 rounded-lg flex items-center gap-3">
            {selectedForUnlink && getProviderIcon(selectedForUnlink.provider)}
            <div>
              <div className="font-semibold text-white">{selectedForUnlink?.provider}</div>
              <div className="text-xs text-slate-400">{selectedForUnlink?.displayName || selectedForUnlink?.email}</div>
            </div>
          </div>
          <div className="flex justify-end gap-2 pt-2">
            <button onClick={() => setSelectedForUnlink(null)} className="btn btn-secondary">
              {t('cancel')}
            </button>
            <button onClick={handleConfirmUnlink} className="btn btn-danger">
              {t('confirm')}
            </button>
          </div>
        </div>
      </Modal>
    </div>
  );
};
