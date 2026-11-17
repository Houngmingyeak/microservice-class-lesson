import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { IconGoogle, IconGithub, IconFacebook, IconTelegram, IconCheck, IconGlobe } from '../common/Icons';
import { Modal } from '../common/Modal';

const SOCIAL_PROVIDERS = [
  {
    key: 'google',
    name: 'Google',
    authUrl: '/oauth2/authorization/google',
    description: 'Connect your Google account for secure 1-click single sign-on.',
    icon: (size = 26) => <IconGoogle size={size} />,
    iconBg: 'bg-red-50 text-red-600 border-red-100'
  },
  {
    key: 'github',
    name: 'GitHub',
    authUrl: '/oauth2/authorization/github',
    description: 'Link your GitHub profile for developer authorization and workflow access.',
    icon: (size = 26) => <IconGithub size={size} className="text-slate-900" />,
    iconBg: 'bg-slate-100 text-slate-900 border-slate-200'
  },
  {
    key: 'facebook',
    name: 'Facebook',
    authUrl: '/oauth2/authorization/facebook',
    description: 'Link your Facebook identity for social sign-in and profile synchronization.',
    icon: (size = 26) => <IconFacebook size={size} className="text-blue-600" />,
    iconBg: 'bg-blue-50 text-blue-600 border-blue-100'
  },
  {
    key: 'telegram',
    name: 'Telegram',
    authUrl: '/oauth2/authorization/telegram',
    description: 'Link your Telegram account for instant authentication and security notices.',
    icon: (size = 26) => <IconTelegram size={size} className="text-sky-500" />,
    iconBg: 'bg-sky-50 text-sky-500 border-sky-100'
  }
];

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
      setIdentities(list || []);
    } catch (err) {
      onShowToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleConfirmUnlink = async () => {
    if (!selectedForUnlink) return;
    try {
      const updated = await api.unlinkAccount(selectedForUnlink.uuid);
      setIdentities(updated || []);
      onShowToast(t('unlinkSuccess') || 'Account unlinked successfully', 'success');
      setSelectedForUnlink(null);
    } catch (err) {
      onShowToast(err.message || 'Failed to unlink account', 'error');
    }
  };

  return (
    <div className="view-container">
      <div className="view-header">
        <div>
          <h2 className="view-title">{t('linkedAccountsTitle') || 'Linked Social & OAuth2 Accounts'}</h2>
          <p className="view-subtitle">{t('linkedAccountsSubtitle') || 'Connect your social accounts for fast and secure one-click sign in.'}</p>
        </div>
      </div>

      <div className="bg-white border border-slate-200 rounded-3xl shadow-sm divide-y divide-slate-100 overflow-hidden">
        {SOCIAL_PROVIDERS.map((provider) => {
          const linkedIdentity = identities.find(
            i => (i.provider || '').toLowerCase() === provider.key.toLowerCase()
          );
          const isConnected = !!linkedIdentity;

          return (
            <div
              key={provider.key}
              className="p-6 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-5 hover:bg-slate-50/60 transition"
            >
              <div className="flex items-center gap-4.5">
                <div className={`w-14 h-14 rounded-full border flex items-center justify-center shrink-0 shadow-xs ${provider.iconBg}`}>
                  {provider.icon(28)}
                </div>
                <div>
                  <div className="flex items-center gap-3">
                    <h4 className="text-xl font-bold text-slate-900">{provider.name}</h4>
                    <span
                      className={`inline-flex items-center gap-1 px-3 py-0.5 rounded-full text-sm font-semibold border ${
                        isConnected
                          ? 'bg-emerald-50 text-emerald-700 border-emerald-200'
                          : 'bg-slate-100 text-slate-600 border-slate-200'
                      }`}
                    >
                      {isConnected ? (
                        <>
                          <IconCheck size={14} className="text-emerald-600" />
                          <span>{t('connected') || 'Connected'}</span>
                        </>
                      ) : (
                        <span>{t('notConnected') || 'Not Connected'}</span>
                      )}
                    </span>
                  </div>
                  <p className="text-base text-slate-600 mt-1">
                    {isConnected
                      ? (linkedIdentity.displayName || linkedIdentity.email || linkedIdentity.providerSubject || `Connected via ${provider.name}`)
                      : provider.description}
                  </p>
                  {isConnected && linkedIdentity.linkedAt && (
                    <span className="text-sm text-slate-400 block mt-1">
                      Linked on {new Date(linkedIdentity.linkedAt).toLocaleDateString()}
                    </span>
                  )}
                </div>
              </div>

              <div className="flex items-center gap-3 self-end sm:self-center shrink-0">
                {isConnected ? (
                  <button
                    type="button"
                    onClick={() => setSelectedForUnlink(linkedIdentity)}
                    className="px-6 py-2.5 rounded-full bg-rose-50 text-rose-600 border border-rose-200 hover:bg-rose-600 hover:text-white font-semibold text-base transition shadow-xs cursor-pointer"
                  >
                    {t('unlinkAccount') || 'Unlink Account'}
                  </button>
                ) : (
                  <a
                    href={provider.authUrl}
                    className="px-6 py-2.5 rounded-full bg-indigo-600 hover:bg-indigo-700 active:bg-indigo-800 text-white font-semibold text-base transition shadow-sm hover:shadow flex items-center gap-2 cursor-pointer text-decoration-none"
                    title={`Connect with ${provider.name}`}
                  >
                    <span>{t('linkAccount') || 'Link Account'}</span>
                  </a>
                )}
              </div>
            </div>
          );
        })}
      </div>

      {/* Unlink Confirmation Modal */}
      <Modal
        isOpen={!!selectedForUnlink}
        onClose={() => setSelectedForUnlink(null)}
        title={t('unlinkAccount') || 'Unlink Account'}
      >
        <div className="space-y-5">
          <p className="text-base text-slate-600">
            {t('unlinkConfirm') || 'Are you sure you want to unlink this social account? You will no longer be able to use it for single sign-on.'}
          </p>
          <div className="p-4 bg-slate-50 border border-slate-200 rounded-2xl flex items-center gap-4">
            <div className="w-12 h-12 rounded-full bg-white border border-slate-200 flex items-center justify-center">
              {selectedForUnlink && (
                selectedForUnlink.provider?.toLowerCase() === 'google' ? <IconGoogle size={24} /> :
                selectedForUnlink.provider?.toLowerCase() === 'github' ? <IconGithub size={24} /> :
                selectedForUnlink.provider?.toLowerCase() === 'facebook' ? <IconFacebook size={24} /> :
                selectedForUnlink.provider?.toLowerCase() === 'telegram' ? <IconTelegram size={24} /> :
                <IconGlobe size={24} />
              )}
            </div>
            <div>
              <div className="font-bold text-slate-900 text-lg capitalize">{selectedForUnlink?.provider}</div>
              <div className="text-base text-slate-500">{selectedForUnlink?.displayName || selectedForUnlink?.email || selectedForUnlink?.providerSubject}</div>
            </div>
          </div>
          <div className="flex justify-end gap-3 pt-2">
            <button
              type="button"
              onClick={() => setSelectedForUnlink(null)}
              className="px-6 py-2.5 rounded-full bg-slate-100 hover:bg-slate-200 text-slate-700 font-semibold text-base transition cursor-pointer"
            >
              {t('cancel') || 'Cancel'}
            </button>
            <button
              type="button"
              onClick={handleConfirmUnlink}
              className="px-6 py-2.5 rounded-full bg-rose-600 hover:bg-rose-700 text-white font-semibold text-base shadow-sm transition cursor-pointer"
            >
              {t('confirm') || 'Confirm'}
            </button>
          </div>
        </div>
      </Modal>
    </div>
  );
};
