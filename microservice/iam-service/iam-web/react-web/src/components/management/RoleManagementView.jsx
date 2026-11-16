import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { Badge } from '../common/Badge';
import { Modal } from '../common/Modal';
import { IconKey, IconPlus, IconEdit, IconTrash, IconCheck, IconX, IconShieldCheck, IconSearch } from '../common/Icons';

export const RoleManagementView = ({ onShowToast }) => {
  const { t } = useI18n();
  const [roles, setRoles] = useState([]);
  const [allPermissions, setAllPermissions] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');

  // Modals state
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [editingRole, setEditingRole] = useState(null);
  const [deletingRole, setDeletingRole] = useState(null);

  // New role form state
  const [newRole, setNewRole] = useState({
    name: '',
    description: '',
    permissions: []
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    const rList = await api.getRoles();
    const pList = await api.getPermissions();
    setRoles(rList);
    setAllPermissions(pList);
  };

  const handleCreateRole = async (e) => {
    e.preventDefault();
    if (!newRole.name) return;
    try {
      await api.createRole(newRole);
      onShowToast(`Role ${newRole.name} created successfully!`, 'success');
      setShowCreateModal(false);
      setNewRole({ name: '', description: '', permissions: [] });
      loadData();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleUpdateRole = async (e) => {
    e.preventDefault();
    if (!editingRole) return;
    try {
      await api.updateRole(editingRole.uuid, editingRole);
      onShowToast(`Role ${editingRole.name} updated successfully!`, 'success');
      setEditingRole(null);
      loadData();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleDeleteRole = async () => {
    if (!deletingRole) return;
    try {
      await api.deleteRole(deletingRole.uuid);
      onShowToast(`Role ${deletingRole.name} removed successfully!`, 'success');
      setDeletingRole(null);
      loadData();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleTogglePerm = (permName, isEditing = false) => {
    if (isEditing) {
      const current = editingRole.permissions || [];
      const updated = current.includes(permName)
        ? current.filter(p => p !== permName)
        : [...current, permName];
      setEditingRole({ ...editingRole, permissions: updated });
    } else {
      const current = newRole.permissions || [];
      const updated = current.includes(permName)
        ? current.filter(p => p !== permName)
        : [...current, permName];
      setNewRole({ ...newRole, permissions: updated });
    }
  };

  const filteredRoles = roles.filter(r =>
    r.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    r.description.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="view-container">
      <div className="view-header flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h2 className="view-title">{t('rolesTitle')}</h2>
          <p className="view-subtitle">{t('rolesSubtitle')}</p>
        </div>
        <button
          onClick={() => setShowCreateModal(true)}
          className="btn btn-primary flex items-center gap-2 self-start sm:self-auto"
        >
          <IconPlus size={16} />
          <span>{t('createRole')}</span>
        </button>
      </div>

      <div className="glass-card p-6">
        <div className="flex gap-3 mb-6">
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
        </div>

        <div className="table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>{t('roleName')}</th>
                <th>{t('roleDesc')}</th>
                <th>Type</th>
                <th>Users</th>
                <th>Permissions Count</th>
                <th className="text-right">{t('actions')}</th>
              </tr>
            </thead>
            <tbody>
              {filteredRoles.map((role) => (
                <tr key={role.uuid}>
                  <td>
                    <div className="flex items-center gap-2">
                      <div className="p-1.5 bg-indigo-500/10 rounded border border-indigo-500/30 text-indigo-400">
                        <IconKey size={14} />
                      </div>
                      <span className="font-bold text-white text-sm">{role.name}</span>
                    </div>
                  </td>

                  <td className="text-sm text-slate-300 max-w-xs">{role.description}</td>

                  <td>
                    {role.isSystem ? (
                      <Badge variant="purple" size="sm">System Built-in</Badge>
                    ) : (
                      <Badge variant="default" size="sm">Custom</Badge>
                    )}
                  </td>

                  <td className="text-sm text-slate-300">{role.userCount || 0} users</td>

                  <td>
                    <span className="font-mono text-xs text-indigo-400 bg-indigo-500/10 px-2 py-0.5 rounded">
                      {role.permissions?.length || 0} permissions
                    </span>
                  </td>

                  <td className="text-right">
                    <div className="flex items-center justify-end gap-1">
                      <button
                        onClick={() => setEditingRole({ ...role })}
                        className="action-icon-btn text-slate-400 hover:text-white"
                        title={t('edit')}
                      >
                        <IconEdit size={15} />
                      </button>

                      {!role.isSystem && (
                        <button
                          onClick={() => setDeletingRole(role)}
                          className="action-icon-btn text-slate-400 hover:text-rose-400"
                          title={t('delete')}
                        >
                          <IconTrash size={15} />
                        </button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Create Role Modal */}
      <Modal
        isOpen={showCreateModal}
        onClose={() => setShowCreateModal(false)}
        title={t('createRole')}
        maxWidth="max-w-2xl"
      >
        <form onSubmit={handleCreateRole} className="space-y-4">
          <div className="form-group">
            <label className="form-label">{t('roleName')}</label>
            <input
              type="text"
              placeholder="e.g. COMPLIANCE_OFFICER"
              value={newRole.name}
              onChange={(e) => setNewRole({ ...newRole, name: e.target.value.toUpperCase() })}
              className="form-input uppercase"
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label">{t('roleDesc')}</label>
            <textarea
              rows="2"
              placeholder="Describe authorization scope..."
              value={newRole.description}
              onChange={(e) => setNewRole({ ...newRole, description: e.target.value })}
              className="form-input resize-none"
              required
            />
          </div>

          <div>
            <label className="form-label mb-2 block">{t('assignedPerms')}</label>
            <div className="max-h-48 overflow-y-auto p-3 bg-slate-950 rounded-lg border border-slate-800 grid grid-cols-2 gap-2 text-xs">
              {allPermissions.map((p) => {
                const checked = newRole.permissions?.includes(p.name);
                return (
                  <label key={p.uuid} className="flex items-center gap-2 cursor-pointer hover:text-white text-slate-300">
                    <input
                      type="checkbox"
                      checked={checked}
                      onChange={() => handleTogglePerm(p.name, false)}
                      className="rounded border-slate-700 text-indigo-600 focus:ring-0"
                    />
                    <span className="font-mono">{p.name}</span>
                  </label>
                );
              })}
            </div>
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

      {/* Edit Role Modal */}
      <Modal
        isOpen={!!editingRole}
        onClose={() => setEditingRole(null)}
        title={`${t('edit')} Role: ${editingRole?.name}`}
        maxWidth="max-w-2xl"
      >
        {editingRole && (
          <form onSubmit={handleUpdateRole} className="space-y-4">
            <div className="form-group">
              <label className="form-label">{t('roleDesc')}</label>
              <textarea
                rows="2"
                value={editingRole.description}
                onChange={(e) => setEditingRole({ ...editingRole, description: e.target.value })}
                className="form-input resize-none"
                required
              />
            </div>

            <div>
              <label className="form-label mb-2 block">{t('assignedPerms')}</label>
              <div className="max-h-60 overflow-y-auto p-3 bg-slate-950 rounded-lg border border-slate-800 grid grid-cols-2 gap-2 text-xs">
                {allPermissions.map((p) => {
                  const checked = editingRole.permissions?.includes(p.name);
                  return (
                    <label key={p.uuid} className="flex items-center gap-2 cursor-pointer hover:text-white text-slate-300">
                      <input
                        type="checkbox"
                        checked={checked}
                        onChange={() => handleTogglePerm(p.name, true)}
                        className="rounded border-slate-700 text-indigo-600 focus:ring-0"
                      />
                      <span className="font-mono">{p.name}</span>
                    </label>
                  );
                })}
              </div>
            </div>

            <div className="flex justify-end gap-2 pt-2">
              <button type="button" onClick={() => setEditingRole(null)} className="btn btn-secondary">
                {t('cancel')}
              </button>
              <button type="submit" className="btn btn-primary">
                {t('save')}
              </button>
            </div>
          </form>
        )}
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal
        isOpen={!!deletingRole}
        onClose={() => setDeletingRole(null)}
        title={t('delete')}
      >
        <div className="space-y-4">
          <p className="text-sm text-slate-300">
            Permanently delete role <strong>{deletingRole?.name}</strong>? All privileges associated with this role will be revoked from users.
          </p>
          <div className="flex justify-end gap-2 pt-2">
            <button onClick={() => setDeletingRole(null)} className="btn btn-secondary">
              {t('cancel')}
            </button>
            <button onClick={handleDeleteRole} className="btn btn-danger">
              {t('delete')}
            </button>
          </div>
        </div>
      </Modal>
    </div>
  );
};
