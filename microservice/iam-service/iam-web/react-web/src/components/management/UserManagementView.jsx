import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { Badge } from '../common/Badge';
import { Modal } from '../common/Modal';
import { UserRoleAssignModal } from './UserRoleAssignModal';
import { UserPermAssignModal } from './UserPermAssignModal';
import {
  IconUsers,
  IconUserPlus,
  IconSearch,
  IconLock,
  IconUnlock,
  IconCheck,
  IconX,
  IconTrash,
  IconEdit,
  IconKey,
  IconShieldCheck
} from '../common/Icons';

export const UserManagementView = ({ onShowToast }) => {
  const { t } = useI18n();
  const [users, setUsers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [roleFilter, setRoleFilter] = useState('ALL');

  // Modals state
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [deletingUser, setDeletingUser] = useState(null);
  const [roleAssignUser, setRoleAssignUser] = useState(null);
  const [permAssignUser, setPermAssignUser] = useState(null);

  // New user form state
  const [newUser, setNewUser] = useState({
    username: '',
    email: '',
    givenName: '',
    familyName: '',
    phoneNumber: '',
    role: 'USER'
  });

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    const list = await api.getUsers();
    setUsers(list);
  };

  const handleCreateUser = async (e) => {
    e.preventDefault();
    try {
      await api.createUser({
        ...newUser,
        roles: [newUser.role]
      });
      onShowToast(t('userCreated'), 'success');
      setShowCreateModal(false);
      setNewUser({ username: '', email: '', givenName: '', familyName: '', phoneNumber: '', role: 'USER' });
      loadUsers();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleUpdateUser = async (e) => {
    e.preventDefault();
    if (!editingUser) return;
    try {
      await api.updateUser(editingUser.uuid, editingUser);
      onShowToast(t('userUpdated'), 'success');
      setEditingUser(null);
      loadUsers();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleToggleActive = async (user) => {
    try {
      await api.toggleUserActive(user.uuid);
      onShowToast(`User ${user.username} ${user.enabled ? 'deactivated' : 'activated'}`, 'success');
      loadUsers();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleToggleLock = async (user) => {
    try {
      await api.toggleUserLock(user.uuid);
      onShowToast(`User ${user.username} ${user.accountNonLocked ? 'locked' : 'unlocked'}`, 'success');
      loadUsers();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const handleDelete = async () => {
    if (!deletingUser) return;
    try {
      await api.deleteUser(deletingUser.uuid);
      onShowToast(t('userDeleted'), 'success');
      setDeletingUser(null);
      loadUsers();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  const filteredUsers = users.filter((u) => {
    const term = searchTerm.toLowerCase();
    const matchesSearch =
      u.username.toLowerCase().includes(term) ||
      u.email.toLowerCase().includes(term) ||
      `${u.givenName} ${u.familyName}`.toLowerCase().includes(term);

    let matchesStatus = true;
    if (statusFilter === 'ACTIVE') matchesStatus = u.enabled && u.accountNonLocked;
    if (statusFilter === 'INACTIVE') matchesStatus = !u.enabled;
    if (statusFilter === 'LOCKED') matchesStatus = !u.accountNonLocked;

    const matchesRole = roleFilter === 'ALL' || u.roles?.includes(roleFilter);

    return matchesSearch && matchesStatus && matchesRole;
  });

  return (
    <div className="view-container">
      <div className="view-header flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h2 className="view-title">{t('userMgmtTitle')}</h2>
          <p className="view-subtitle">{t('userMgmtSubtitle')}</p>
        </div>
        <button
          onClick={() => setShowCreateModal(true)}
          className="btn btn-primary flex items-center gap-2 self-start sm:self-auto"
        >
          <IconUserPlus size={16} />
          <span>{t('createUser')}</span>
        </button>
      </div>

      <div className="glass-card p-6">
        {/* Filters and search */}
        <div className="flex flex-col md:flex-row gap-3 mb-6 justify-between">
          <div className="relative w-full md:w-80">
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

          <div className="flex flex-wrap gap-2">
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="form-input text-xs w-auto py-1.5"
            >
              <option value="ALL">{t('all')} Status</option>
              <option value="ACTIVE">{t('active')}</option>
              <option value="INACTIVE">{t('inactive')}</option>
              <option value="LOCKED">{t('locked')}</option>
            </select>

            <select
              value={roleFilter}
              onChange={(e) => setRoleFilter(e.target.value)}
              className="form-input text-xs w-auto py-1.5"
            >
              <option value="ALL">{t('all')} Roles</option>
              <option value="ADMIN">ADMIN</option>
              <option value="USER">USER</option>
              <option value="DEVELOPER">DEVELOPER</option>
              <option value="MANAGER">MANAGER</option>
              <option value="AUDITOR">AUDITOR</option>
            </select>
          </div>
        </div>

        {/* User Table */}
        <div className="table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>User</th>
                <th>{t('status')}</th>
                <th>{t('userRoles')}</th>
                <th>Last Login</th>
                <th className="text-right">{t('actions')}</th>
              </tr>
            </thead>
            <tbody>
              {filteredUsers.map((u) => {
                const isLocked = !u.accountNonLocked;
                const isDisabled = !u.enabled;

                return (
                  <tr key={u.uuid}>
                    <td>
                      <div className="flex items-center gap-3">
                        {u.pictureUrl ? (
                          <img
                            src={u.pictureUrl}
                            alt={u.username}
                            className="w-8 h-8 rounded-full object-cover border border-slate-200"
                          />
                        ) : (
                          <div className="w-8 h-8 rounded-full bg-slate-100 border border-slate-200 text-slate-600 font-bold text-xs flex items-center justify-center">
                            {u.givenName ? u.givenName.charAt(0).toUpperCase() : (u.username ? u.username.charAt(0).toUpperCase() : 'U')}
                          </div>
                        )}
                        <div>
                          <div className="font-semibold text-slate-900 text-sm">
                            {u.givenName ? `${u.givenName} ${u.familyName}` : u.username}
                          </div>
                          <div className="text-xs text-slate-500 font-mono">@{u.username} • {u.email}</div>
                        </div>
                      </div>
                    </td>

                    <td>
                      <div className="flex items-center gap-1.5">
                        {isLocked ? (
                          <Badge variant="danger" size="sm">
                            <IconLock size={12} />
                            {t('locked')}
                          </Badge>
                        ) : isDisabled ? (
                          <Badge variant="warning" size="sm">
                            {t('disabled')}
                          </Badge>
                        ) : (
                          <Badge variant="success" size="sm">
                            <IconCheck size={12} />
                            {t('active')}
                          </Badge>
                        )}
                      </div>
                    </td>

                    <td>
                      <div className="flex flex-wrap gap-1">
                        {u.roles?.map(r => (
                          <Badge key={r} variant={r === 'ADMIN' ? 'danger' : 'primary'} size="sm">
                            {r}
                          </Badge>
                        ))}
                      </div>
                    </td>

                    <td className="text-xs text-slate-400">
                      {u.lastLoginAt ? new Date(u.lastLoginAt).toLocaleString() : 'Never'}
                    </td>

                    <td className="text-right">
                      <div className="flex items-center justify-end gap-1">
                        {/* Lock / Unlock button */}
                        <button
                          onClick={() => handleToggleLock(u)}
                          className={`action-icon-btn ${isLocked ? 'text-amber-400 hover:bg-amber-500/10' : 'text-slate-400 hover:text-white'}`}
                          title={isLocked ? t('unlockUser') : t('lockUser')}
                        >
                          {isLocked ? <IconUnlock size={15} /> : <IconLock size={15} />}
                        </button>

                        {/* Active / Inactive toggle */}
                        <button
                          onClick={() => handleToggleActive(u)}
                          className={`action-icon-btn ${isDisabled ? 'text-emerald-400' : 'text-slate-400 hover:text-amber-400'}`}
                          title={isDisabled ? t('activateUser') : t('deactivateUser')}
                        >
                          {isDisabled ? <IconCheck size={15} /> : <IconX size={15} />}
                        </button>

                        {/* Edit User button */}
                        <button
                          onClick={() => setEditingUser({ ...u })}
                          className="action-icon-btn text-slate-400 hover:text-white"
                          title={t('editUser')}
                        >
                          <IconEdit size={15} />
                        </button>

                        {/* Delete User button */}
                        <button
                          onClick={() => setDeletingUser(u)}
                          className="action-icon-btn text-slate-400 hover:text-rose-400"
                          title={t('deleteUser')}
                        >
                          <IconTrash size={15} />
                        </button>
                      </div>
                    </td>
                  </tr>
                );
              })}

              {filteredUsers.length === 0 && (
                <tr>
                  <td colSpan="5" className="text-center py-8 text-slate-500">
                    {t('noData')}
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Create User Modal */}
      <Modal
        isOpen={showCreateModal}
        onClose={() => setShowCreateModal(false)}
        title={t('createUser')}
      >
        <form onSubmit={handleCreateUser} className="space-y-4">
          <div className="form-grid-2">
            <div className="form-group">
              <label className="form-label">{t('givenName')}</label>
              <input
                type="text"
                value={newUser.givenName}
                onChange={(e) => setNewUser({ ...newUser, givenName: e.target.value })}
                className="form-input"
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">{t('familyName')}</label>
              <input
                type="text"
                value={newUser.familyName}
                onChange={(e) => setNewUser({ ...newUser, familyName: e.target.value })}
                className="form-input"
                required
              />
            </div>
          </div>

          <div className="form-grid-2">
            <div className="form-group">
              <label className="form-label">{t('username')}</label>
              <input
                type="text"
                value={newUser.username}
                onChange={(e) => setNewUser({ ...newUser, username: e.target.value })}
                className="form-input"
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">{t('email')}</label>
              <input
                type="email"
                value={newUser.email}
                onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
                className="form-input"
                required
              />
            </div>
          </div>

          <div className="form-grid-2">
            <div className="form-group">
              <label className="form-label">{t('phoneNumber')}</label>
              <input
                type="tel"
                value={newUser.phoneNumber}
                onChange={(e) => setNewUser({ ...newUser, phoneNumber: e.target.value })}
                className="form-input"
              />
            </div>
            <div className="form-group">
              <label className="form-label">{t('userRoles')}</label>
              <select
                value={newUser.role}
                onChange={(e) => setNewUser({ ...newUser, role: e.target.value })}
                className="form-input"
              >
                <option value="USER">USER</option>
                <option value="ADMIN">ADMIN</option>
                <option value="DEVELOPER">DEVELOPER</option>
                <option value="MANAGER">MANAGER</option>
                <option value="AUDITOR">AUDITOR</option>
              </select>
            </div>
          </div>

          <div className="flex justify-end gap-2 pt-2">
            <button
              type="button"
              onClick={() => setShowCreateModal(false)}
              className="btn btn-secondary"
            >
              {t('cancel')}
            </button>
            <button type="submit" className="btn btn-primary">
              {t('create')}
            </button>
          </div>
        </form>
      </Modal>

      {/* Edit User Modal */}
      <Modal
        isOpen={!!editingUser}
        onClose={() => setEditingUser(null)}
        title={t('editUser')}
      >
        {editingUser && (
          <form onSubmit={handleUpdateUser} className="space-y-4">
            <div className="form-grid-2">
              <div className="form-group">
                <label className="form-label">{t('givenName')}</label>
                <input
                  type="text"
                  value={editingUser.givenName || ''}
                  onChange={(e) => setEditingUser({ ...editingUser, givenName: e.target.value })}
                  className="form-input"
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label">{t('familyName')}</label>
                <input
                  type="text"
                  value={editingUser.familyName || ''}
                  onChange={(e) => setEditingUser({ ...editingUser, familyName: e.target.value })}
                  className="form-input"
                  required
                />
              </div>
            </div>

            <div className="form-grid-2">
              <div className="form-group">
                <label className="form-label">{t('email')}</label>
                <input
                  type="email"
                  value={editingUser.email || ''}
                  onChange={(e) => setEditingUser({ ...editingUser, email: e.target.value })}
                  className="form-input"
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label">{t('phoneNumber')}</label>
                <input
                  type="tel"
                  value={editingUser.phoneNumber || ''}
                  onChange={(e) => setEditingUser({ ...editingUser, phoneNumber: e.target.value })}
                  className="form-input"
                />
              </div>
            </div>

            <div className="flex justify-end gap-2 pt-2">
              <button
                type="button"
                onClick={() => setEditingUser(null)}
                className="btn btn-secondary"
              >
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
        isOpen={!!deletingUser}
        onClose={() => setDeletingUser(null)}
        title={t('deleteUser')}
      >
        <div className="space-y-4">
          <p className="text-sm text-slate-300">
            Are you sure you want to permanently delete user <strong>{deletingUser?.username}</strong> ({deletingUser?.email})? This action cannot be undone.
          </p>
          <div className="flex justify-end gap-2 pt-2">
            <button onClick={() => setDeletingUser(null)} className="btn btn-secondary">
              {t('cancel')}
            </button>
            <button onClick={handleDelete} className="btn btn-danger">
              {t('delete')}
            </button>
          </div>
        </div>
      </Modal>

      {/* User Role Assignment Modal */}
      <UserRoleAssignModal
        user={roleAssignUser}
        isOpen={!!roleAssignUser}
        onClose={() => setRoleAssignUser(null)}
        onUpdated={loadUsers}
        onShowToast={onShowToast}
      />

      {/* User Permission Assignment Modal */}
      <UserPermAssignModal
        user={permAssignUser}
        isOpen={!!permAssignUser}
        onClose={() => setPermAssignUser(null)}
        onUpdated={loadUsers}
        onShowToast={onShowToast}
      />
    </div>
  );
};
