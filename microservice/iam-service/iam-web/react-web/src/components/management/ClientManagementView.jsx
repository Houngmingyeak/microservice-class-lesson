import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { Badge } from '../common/Badge';
import { Modal } from '../common/Modal';
import { IconTerminal, IconPlus, IconEdit, IconTrash, IconCopy, IconRefresh, IconCheck, IconEye, IconEyeOff } from '../common/Icons';

export const ClientManagementView = ({ onShowToast }) => {
  const { t } = useI18n();
  const [clients, setClients] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');

  // Modals state
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [editingClient, setEditingClient] = useState(null);
  const [deletingClient, setDeletingClient] = useState(null);
  const [revealedSecrets, setRevealedSecrets] = useState({});

  // New client form state
  const [newClient, setNewClient] = useState({
    clientId: '',
    clientName: '',
    clientSecret: '',
    authorizationGrantTypes: ['authorization_code', 'refresh_token'],
    redirectUris: 'http://localhost:9999/login/oauth2/code/front-bff-client',
    scopes: ['openid', 'profile', 'email']
  });

  useEffect(() => {
    loadClients();
  }, []);

  const loadClients = async () => {
    const list = await api.getClients();
    setClients(list);
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      const urisArray = newClient.redirectUris
        .split(',')
        .map(u => u.trim())
        .filter(Boolean);

      await api.createClient({
        ...newClient,
        redirectUris: urisArray
      });

      onShowToast(`Client ${newClient.clientName} registered!`, 'success');
      setShowCreateModal(false);
      setNewClient({
        clientId: '',
        clientName: '',
        clientSecret: '',
        authorizationGrantTypes: ['authorization_code', 'refresh_token'],
        redirectUris: '',
        scopes: ['openid', 'profile', 'email']
      });
      loadClients();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    if (!editingClient) return;
    try {
      const urisArray = Array.isArray(editingClient.redirectUris)
        ? editingClient.redirectUris
        : editingClient.redirectUris.split(',').map(u => u.trim()).filter(Boolean);

      await api.updateClient(editingClient.id, {
        clientName: editingClient.clientName,
        authorizationGrantTypes: editingClient.authorizationGrantTypes,
        scopes: editingClient.scopes,
        redirectUris: urisArray
      });

      onShowToast(`Client ${editingClient.clientName} updated!`, 'success');
      setEditingClient(null);
      loadClients();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleDelete = async () => {
    if (!deletingClient) return;
    try {
      await api.deleteClient(deletingClient.id);
      onShowToast(`Client ${deletingClient.clientName} deleted!`, 'success');
      setDeletingClient(null);
      loadClients();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleRegenerateSecret = async (id) => {
    try {
      const newSecret = await api.regenerateClientSecret(id);
      onShowToast(`Regenerated secret: ${newSecret}`, 'success');
      loadClients();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const copyToClipboard = (text, label) => {
    navigator.clipboard.writeText(text);
    onShowToast(`Copied ${label} to clipboard!`, 'success');
  };

  const toggleRevealSecret = (id) => {
    setRevealedSecrets(prev => ({ ...prev, [id]: !prev[id] }));
  };

  const allAvailableGrants = ['authorization_code', 'refresh_token', 'client_credentials'];
  const allAvailableScopes = ['openid', 'profile', 'email', 'offline_access', 'client.create', 'audit:read'];

  return (
    <div className="view-container">
      <div className="view-header flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h2 className="view-title">{t('clientsTitle')}</h2>
          <p className="view-subtitle">{t('clientsSubtitle')}</p>
        </div>
        <button
          onClick={() => setShowCreateModal(true)}
          className="btn btn-primary flex items-center gap-2 self-start sm:self-auto"
        >
          <IconPlus size={16} />
          <span>{t('registerClient')}</span>
        </button>
      </div>

      <div className="space-y-4">
        {clients.map((client) => {
          const isSecretRevealed = !!revealedSecrets[client.id];
          return (
            <div key={client.id} className="glass-card p-6">
              <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-4 border-b border-slate-100 pb-4 mb-4">
                <div className="flex items-center gap-3">
                  <div className="p-3 bg-amber-50 border border-amber-200 text-amber-600 rounded-2xl">
                    <IconTerminal size={22} />
                  </div>
                  <div>
                    <h3 className="text-lg font-bold text-slate-900">{client.clientName}</h3>
                    <div className="flex items-center gap-2 text-xs font-mono text-slate-500 mt-0.5">
                      <span>ID: <strong>{client.clientId}</strong></span>
                      <button
                        onClick={() => copyToClipboard(client.clientId, 'Client ID')}
                        className="text-slate-400 hover:text-indigo-600"
                        title="Copy Client ID"
                      >
                        <IconCopy size={13} />
                      </button>
                    </div>
                  </div>
                </div>

                <div className="flex items-center gap-2 self-end lg:self-auto">
                  <button
                    onClick={() => handleRegenerateSecret(client.id)}
                    className="btn btn-secondary text-xs flex items-center gap-1.5"
                    title={t('regenerateSecret')}
                  >
                    <IconRefresh size={13} />
                    <span>{t('regenerateSecret')}</span>
                  </button>
                  <button
                    onClick={() => setEditingClient({
                      ...client,
                      redirectUris: Array.isArray(client.redirectUris) ? client.redirectUris.join(', ') : client.redirectUris
                    })}
                    className="btn btn-secondary text-xs flex items-center gap-1"
                  >
                    <IconEdit size={13} />
                    <span>{t('edit')}</span>
                  </button>
                  <button
                    onClick={() => setDeletingClient(client)}
                    className="btn btn-secondary text-xs text-rose-600 flex items-center gap-1 hover:border-rose-300 hover:bg-rose-50"
                  >
                    <IconTrash size={13} />
                    <span>{t('delete')}</span>
                  </button>
                </div>
              </div>

              {/* Client Specs Grid */}
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 text-xs">
                {/* Credentials */}
                <div className="p-4 bg-slate-50/80 rounded-2xl border border-slate-200/80">
                  <span className="text-slate-500 font-semibold block mb-1.5">{t('clientSecret')}</span>
                  <div className="flex items-center justify-between font-mono bg-white border border-slate-200 px-3 py-2 rounded-xl text-indigo-700 font-semibold">
                    <span>{isSecretRevealed ? client.clientSecret : '••••••••••••••••••••'}</span>
                    <div className="flex items-center gap-2">
                      <button
                        onClick={() => toggleRevealSecret(client.id)}
                        className="text-slate-400 hover:text-indigo-600"
                      >
                        {isSecretRevealed ? <IconEyeOff size={14} /> : <IconEye size={14} />}
                      </button>
                      <button
                        onClick={() => copyToClipboard(client.clientSecret, 'Client Secret')}
                        className="text-slate-400 hover:text-indigo-600"
                      >
                        <IconCopy size={14} />
                      </button>
                    </div>
                  </div>
                </div>

                {/* Grant Types */}
                <div className="p-4 bg-slate-50/80 rounded-2xl border border-slate-200/80">
                  <span className="text-slate-500 font-semibold block mb-1.5">{t('grantTypes')}</span>
                  <div className="flex flex-wrap gap-1.5">
                    {client.authorizationGrantTypes?.map(g => (
                      <Badge key={g} variant="primary" size="sm">{g}</Badge>
                    ))}
                  </div>
                </div>

                {/* Scopes */}
                <div className="p-4 bg-slate-50/80 rounded-2xl border border-slate-200/80">
                  <span className="text-slate-500 font-semibold block mb-1.5">{t('scopes')}</span>
                  <div className="flex flex-wrap gap-1.5">
                    {client.scopes?.map(s => (
                      <Badge key={s} variant="success" size="sm">{s}</Badge>
                    ))}
                  </div>
                </div>
              </div>

              {/* Redirect URIs */}
              {client.redirectUris && client.redirectUris.length > 0 && (
                <div className="mt-4 pt-3.5 border-t border-slate-100 text-xs">
                  <span className="text-slate-500 font-semibold block mb-1.5">Redirect URIs:</span>
                  <div className="flex flex-wrap gap-2 font-mono text-slate-700">
                    {(Array.isArray(client.redirectUris) ? client.redirectUris : [client.redirectUris]).map((uri, idx) => (
                      <div key={idx} className="bg-slate-100 px-3 py-1 rounded-lg border border-slate-200 text-xs font-semibold">
                        {uri}
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>
          );
        })}
      </div>

      {/* Register Client Modal */}
      <Modal
        isOpen={showCreateModal}
        onClose={() => setShowCreateModal(false)}
        title={t('registerClient')}
        maxWidth="max-w-2xl"
      >
        <form onSubmit={handleCreate} className="space-y-4">
          <div className="form-grid-2">
            <div className="form-group">
              <label className="form-label">{t('clientName')}</label>
              <input
                type="text"
                placeholder="e.g. Mobile Banking Gateway"
                value={newClient.clientName}
                onChange={(e) => setNewClient({ ...newClient, clientName: e.target.value })}
                className="form-input"
                required
              />
            </div>

            <div className="form-group">
              <label className="form-label">{t('clientId')}</label>
              <input
                type="text"
                placeholder="e.g. mobile-banking-bff"
                value={newClient.clientId}
                onChange={(e) => setNewClient({ ...newClient, clientId: e.target.value })}
                className="form-input font-mono"
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">{t('clientSecret')} (Auto-generated if empty)</label>
            <input
              type="text"
              placeholder="Leave blank to generate cryptographically strong secret"
              value={newClient.clientSecret}
              onChange={(e) => setNewClient({ ...newClient, clientSecret: e.target.value })}
              className="form-input font-mono"
            />
          </div>

          <div className="form-group">
            <label className="form-label mb-2 block">{t('grantTypes')}</label>
            <div className="flex flex-wrap gap-4 text-xs text-slate-700 font-medium">
              {allAvailableGrants.map(grant => {
                const checked = newClient.authorizationGrantTypes.includes(grant);
                return (
                  <label key={grant} className="flex items-center gap-1.5 cursor-pointer hover:text-indigo-600">
                    <input
                      type="checkbox"
                      checked={checked}
                      onChange={() => {
                        const updated = checked
                          ? newClient.authorizationGrantTypes.filter(g => g !== grant)
                          : [...newClient.authorizationGrantTypes, grant];
                        setNewClient({ ...newClient, authorizationGrantTypes: updated });
                      }}
                      className="rounded border-slate-300 text-indigo-600 focus:ring-0"
                    />
                    <span className="font-mono">{grant}</span>
                  </label>
                );
              })}
            </div>
          </div>

          <div className="form-group">
            <label className="form-label mb-2 block">{t('scopes')}</label>
            <div className="flex flex-wrap gap-4 text-xs text-slate-700 font-medium">
              {allAvailableScopes.map(scope => {
                const checked = newClient.scopes.includes(scope);
                return (
                  <label key={scope} className="flex items-center gap-1.5 cursor-pointer hover:text-indigo-600">
                    <input
                      type="checkbox"
                      checked={checked}
                      onChange={() => {
                        const updated = checked
                          ? newClient.scopes.filter(s => s !== scope)
                          : [...newClient.scopes, scope];
                        setNewClient({ ...newClient, scopes: updated });
                      }}
                      className="rounded border-slate-300 text-indigo-600 focus:ring-0"
                    />
                    <span className="font-mono">{scope}</span>
                  </label>
                );
              })}
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">{t('redirectUris')}</label>
            <input
              type="text"
              placeholder="http://localhost:9999/callback, https://app.example.com/login"
              value={newClient.redirectUris}
              onChange={(e) => setNewClient({ ...newClient, redirectUris: e.target.value })}
              className="form-input font-mono text-xs"
            />
          </div>

          <div className="flex justify-end gap-2 pt-2">
            <button type="button" onClick={() => setShowCreateModal(false)} className="btn btn-secondary">
              {t('cancel')}
            </button>
            <button type="submit" className="btn btn-primary">
              {t('registerClient')}
            </button>
          </div>
        </form>
      </Modal>

      {/* Edit Client Modal */}
      <Modal
        isOpen={!!editingClient}
        onClose={() => setEditingClient(null)}
        title={`${t('edit')} Client: ${editingClient?.clientName}`}
        maxWidth="max-w-2xl"
      >
        {editingClient && (
          <form onSubmit={handleUpdate} className="space-y-4">
            <div className="form-group">
              <label className="form-label">{t('clientName')}</label>
              <input
                type="text"
                value={editingClient.clientName}
                onChange={(e) => setEditingClient({ ...editingClient, clientName: e.target.value })}
                className="form-input"
                required
              />
            </div>

            <div className="form-group">
              <label className="form-label">{t('redirectUris')}</label>
              <input
                type="text"
                value={editingClient.redirectUris}
                onChange={(e) => setEditingClient({ ...editingClient, redirectUris: e.target.value })}
                className="form-input font-mono text-xs"
              />
            </div>

            <div className="flex justify-end gap-2 pt-2">
              <button type="button" onClick={() => setEditingClient(null)} className="btn btn-secondary">
                {t('cancel')}
              </button>
              <button type="submit" className="btn btn-primary">
                {t('save')}
              </button>
            </div>
          </form>
        )}
      </Modal>

      {/* Delete Client Modal */}
      <Modal
        isOpen={!!deletingClient}
        onClose={() => setDeletingClient(null)}
        title={t('delete')}
      >
        <div className="space-y-4">
          <p className="text-sm text-slate-600">
            Deregister client <strong>{deletingClient?.clientName}</strong> ({deletingClient?.clientId})? All access tokens and authorization codes tied to this client will be invalidated.
          </p>
          <div className="flex justify-end gap-2 pt-2">
            <button onClick={() => setDeletingClient(null)} className="btn btn-secondary">
              {t('cancel')}
            </button>
            <button onClick={handleDelete} className="btn btn-danger">
              {t('delete')}
            </button>
          </div>
        </div>
      </Modal>
    </div>
  );
};
