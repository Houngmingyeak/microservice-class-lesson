import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { Badge } from '../common/Badge';
import { Modal } from '../common/Modal';
import { IconShieldCheck, IconPlus, IconEdit, IconTrash, IconSearch, IconCheck } from '../common/Icons';

export const PermissionManagementView = ({ onShowToast }) => {
  const { t } = useI18n();
  const [permissions, setPermissions] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [resourceFilter, setResourceFilter] = useState('ALL');

  // Modals state
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [editingPerm, setEditingPerm] = useState(null);
  const [deletingPerm, setDeletingPerm] = useState(null);

  // New perm form state
  const [newPerm, setNewPerm] = useState({
    resource: 'user',
    action: 'read',
    description: ''
  });

  useEffect(() => {
    loadPermissions();
  }, []);

  const loadPermissions = async () => {
    const list = await api.getPermissions();
    setPermissions(list);
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await api.createPermission(newPerm);
      onShowToast(`Permission ${newPerm.resource}:${newPerm.action} created!`, 'success');
      setShowCreateModal(false);
      setNewPerm({ resource: 'user', action: 'read', description: '' });
      loadPermissions();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    if (!editingPerm) return;
    try {
      await api.updatePermission(editingPerm.uuid, { description: editingPerm.description });
      onShowToast(`Permission updated!`, 'success');
      setEditingPerm(null);
      loadPermissions();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleDelete = async () => {
    if (!deletingPerm) return;
    try {
      await api.deletePermission(deletingPerm.uuid);
      onShowToast(`Permission ${deletingPerm.name} removed!`, 'success');
      setDeletingPerm(null);
      loadPermissions();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  // Distinct resources
  const resources = Array.from(new Set(permissions.map(p => p.resource)));

  const filtered = permissions.filter(p => {
    const matchesSearch =
      p.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      p.description.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesResource = resourceFilter === 'ALL' || p.resource === resourceFilter;
    return matchesSearch && matchesResource;
  });

  return (
    <div className="view-container">
      <div className="view-header flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h2 className="view-title">{t('permTitle')}</h2>
          <p className="view-subtitle">{t('permSubtitle')}</p>
        </div>
        <button
          onClick={() => setShowCreateModal(true)}
          className="btn btn-primary flex items-center gap-2 self-start sm:self-auto"
        >
          <IconPlus size={16} />
          <span>{t('createPermission')}</span>
        </button>
      </div>

      <div className="glass-card p-6">
        <div className="flex flex-col sm:flex-row gap-3 mb-6 justify-between">
          <div className="relative w-full sm:w-80">
            <input
              type="text"
              placeholder={t('search')}
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="form-input pl-9"
            />
            <span className="absolute left-3 top-2.5 text-slate-400">
              <IconSearch size={16} />
            </span>
          </div>

          <div className="flex gap-2">
            <select
              value={resourceFilter}
              onChange={(e) => setResourceFilter(e.target.value)}
              className="form-input text-xs w-auto py-1.5"
            >
              <option value="ALL">{t('all')} Resources</option>
              {resources.map(res => (
                <option key={res} value={res}>{res.toUpperCase()}</option>
              ))}
            </select>
          </div>
        </div>

        <div className="table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>{t('permName')}</th>
                <th>{t('resource')}</th>
                <th>{t('action')}</th>
                <th>Description</th>
                <th>{t('status')}</th>
                <th className="text-right">{t('actions')}</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((p) => (
                <tr key={p.uuid}>
                  <td>
                    <div className="flex items-center gap-2">
                      <IconShieldCheck size={16} className="text-emerald-600" />
                      <span className="font-mono text-xs font-bold text-slate-900">{p.name}</span>
                    </div>
                  </td>
                  <td>
                    <Badge variant="primary" size="sm">{p.resource}</Badge>
                  </td>
                  <td>
                    <Badge variant="default" size="sm">{p.action}</Badge>
                  </td>
                  <td className="text-sm text-slate-600">{p.description}</td>
                  <td>
                    <Badge variant="success" size="sm">
                      <IconCheck size={11} />
                      <span>Active</span>
                    </Badge>
                  </td>
                  <td className="text-right">
                    <div className="flex items-center justify-end gap-1">
                      <button
                        onClick={() => setEditingPerm({ ...p })}
                        className="action-icon-btn text-slate-400 hover:text-indigo-600 hover:bg-indigo-50"
                        title={t('edit')}
                      >
                        <IconEdit size={15} />
                      </button>
                      <button
                        onClick={() => setDeletingPerm(p)}
                        className="action-icon-btn text-slate-400 hover:text-rose-600 hover:bg-rose-50"
                        title={t('delete')}
                      >
                        <IconTrash size={15} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Create Permission Modal */}
      <Modal
        isOpen={showCreateModal}
        onClose={() => setShowCreateModal(false)}
        title={t('createPermission')}
      >
        <form onSubmit={handleCreate} className="space-y-4">
          <div className="form-grid-2">
            <div className="form-group">
              <label className="form-label">{t('resource')}</label>
              <input
                type="text"
                placeholder="e.g. invoice, report"
                value={newPerm.resource}
                onChange={(e) => setNewPerm({ ...newPerm, resource: e.target.value.toLowerCase() })}
                className="form-input lowercase"
                required
              />
            </div>

            <div className="form-group">
              <label className="form-label">{t('action')}</label>
              <input
                type="text"
                placeholder="e.g. read, export, sign"
                value={newPerm.action}
                onChange={(e) => setNewPerm({ ...newPerm, action: e.target.value.toLowerCase() })}
                className="form-input lowercase"
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Full Identifier Preview</label>
            <div className="p-3 bg-slate-50 font-mono text-xs text-indigo-700 font-semibold rounded-xl border border-slate-200">
              {newPerm.resource || 'resource'}:{newPerm.action || 'action'}
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Description</label>
            <textarea
              rows="2"
              placeholder="Authorization scope details..."
              value={newPerm.description}
              onChange={(e) => setNewPerm({ ...newPerm, description: e.target.value })}
              className="form-input resize-none"
              required
            />
          </div>

          <div className="flex justify-end gap-2 pt-2">
            <button type="button" onClick={() => setShowCreateModal(false)} className="btn btn-secondary">
              {t('cancel')}
            </button>
            <button type="submit" className="btn btn-primary">
              {t('create')}
            </button>
          </div>
        </form>
      </Modal>

      {/* Edit Permission Modal */}
      <Modal
        isOpen={!!editingPerm}
        onClose={() => setEditingPerm(null)}
        title={`${t('edit')} ${editingPerm?.name}`}
      >
        {editingPerm && (
          <form onSubmit={handleUpdate} className="space-y-4">
            <div className="form-group">
              <label className="form-label">Description</label>
              <textarea
                rows="3"
                value={editingPerm.description}
                onChange={(e) => setEditingPerm({ ...editingPerm, description: e.target.value })}
                className="form-input resize-none"
                required
              />
            </div>

            <div className="flex justify-end gap-2 pt-2">
              <button type="button" onClick={() => setEditingPerm(null)} className="btn btn-secondary">
                {t('cancel')}
              </button>
              <button type="submit" className="btn btn-primary">
                {t('save')}
              </button>
            </div>
          </form>
        )}
      </Modal>

      {/* Delete Permission Modal */}
      <Modal
        isOpen={!!deletingPerm}
        onClose={() => setDeletingPerm(null)}
        title={t('delete')}
      >
        <div className="space-y-4">
          <p className="text-sm text-slate-600">
            Remove permission <strong>{deletingPerm?.name}</strong> from directory catalog? Roles holding this permission will lose associated access.
          </p>
          <div className="flex justify-end gap-2 pt-2">
            <button onClick={() => setDeletingPerm(null)} className="btn btn-secondary">
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
