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
  IconShieldCheck,
  IconRefresh,
  IconLaptop,
  IconFileText,
  IconCopy,
  IconShield
} from '../common/Icons';

export const UserManagementView = ({ onShowToast }) => {
  const { t } = useI18n();
  const [users, setUsers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [roleFilter, setRoleFilter] = useState('ALL');
  const [loading, setLoading] = useState(false);

  // Modals state
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [deletingUser, setDeletingUser] = useState(null);
  const [roleAssignUser, setRoleAssignUser] = useState(null);
  const [permAssignUser, setPermAssignUser] = useState(null);
  const [resetPasswordUser, setResetPasswordUser] = useState(null);
  const [sessionUser, setSessionUser] = useState(null);
  const [userSessionsList, setUserSessionsList] = useState([]);
  const [sessionLoading, setSessionLoading] = useState(false);

  // Reset password form state
  const [tempPassword, setTempPassword] = useState('');
  const [requirePassChange, setRequirePassChange] = useState(true);

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
    setLoading(true);
    try {
      const list = await api.getUsers();
      setUsers(list || []);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateUser = async (e) => {
    e.preventDefault();
    try {
      await api.createUser({
        ...newUser,
        roles: [newUser.role]
      });
      onShowToast(t('userCreated') || 'User created successfully', 'success');
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
      onShowToast(t('userUpdated') || 'User updated successfully', 'success');
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
      onShowToast(t('userDeleted') || 'User deleted permanently', 'success');
      setDeletingUser(null);
      loadUsers();
    } catch (err) {
      onShowToast(err.message, 'error');
    }
  };

  // Open Reset Password Modal
  const openResetPasswordModal = (user) => {
    setResetPasswordUser(user);
    // Generate secure temporary password
    const gen = "Temp#" + Math.random().toString(36).substring(2, 8).toUpperCase() + "!";
    setTempPassword(gen);
    setRequirePassChange(true);
  };

  const handleAdminResetPassword = async (e) => {
    e.preventDefault();
    if (!resetPasswordUser || !tempPassword) return;
    try {
      await api.adminResetPassword(resetPasswordUser.uuid, tempPassword);
      onShowToast(`Password reset for ${resetPasswordUser.username}. Temporary credentials assigned.`, 'success');
      setResetPasswordUser(null);
      loadUsers();
    } catch (err) {
      onShowToast(err.message || 'Failed to reset password', 'error');
    }
  };

  // Open User Sessions Modal
  const openUserSessionsModal = async (user) => {
    setSessionUser(user);
    setSessionLoading(true);
    try {
      const allSessions = await api.getActiveSessions();
      const userOnly = (allSessions || []).filter(
        s => (s.username || '').toLowerCase() === (user.username || '').toLowerCase()
      );
      setUserSessionsList(userOnly);
    } catch (err) {
      setUserSessionsList([]);
    } finally {
      setSessionLoading(false);
    }
  };

  const handleTerminateUserSession = async (sessionId) => {
    try {
      const updated = await api.terminateSession(sessionId);
      onShowToast("Session terminated successfully", 'success');
      if (sessionUser) {
        const userOnly = (updated || []).filter(
          s => (s.username || '').toLowerCase() === (sessionUser.username || '').toLowerCase()
        );
        setUserSessionsList(userOnly);
      }
    } catch (err) {
      onShowToast(err.message || 'Failed to terminate session', 'error');
    }
  };

  const handleExportCSV = () => {
    const headers = "Username,Email,GivenName,FamilyName,Status,LockStatus,Roles,DirectPermissions,LastLogin\n";
    const rows = users.map(u => (
      `"${u.username}","${u.email}","${u.givenName || ''}","${u.familyName || ''}","${u.enabled ? 'ACTIVE' : 'DISABLED'}","${u.accountNonLocked ? 'UNLOCKED' : 'LOCKED'}","${(u.roles || []).join(';')}","${(u.directPermissions || []).map(p => p.name).join(';')}","${u.lastLoginAt || 'Never'}"`
    )).join("\n");

    const blob = new Blob([headers + rows], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = `iam_users_directory_${new Date().toISOString().slice(0, 10)}.csv`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    onShowToast("User directory exported to CSV", 'success');
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
      {/* View Header */}
      <div className="view-header flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h2 className="view-title">{t('userMgmtTitle')}</h2>
          <p className="view-subtitle">{t('userMgmtSubtitle')}</p>
        </div>
        <div className="flex items-center gap-2 self-start sm:self-auto flex-wrap">
          <button
            onClick={handleExportCSV}
            className="btn btn-secondary flex items-center gap-1.5 text-xs py-2 px-3.5"
            title="Export all users to CSV"
          >
            <IconFileText size={14} />
            <span>Export CSV</span>
          </button>
          <button
            onClick={() => setShowCreateModal(true)}
            className="btn btn-primary flex items-center gap-1.5 text-xs py-2 px-3.5"
          >
            <IconUserPlus size={14} />
            <span>{t('createUser')}</span>
          </button>
        </div>
      </div>

      <div className="glass-card p-5">
        {/* Filters and search toolbar */}
        <div className="flex flex-col md:flex-row gap-3 mb-5 justify-between items-stretch md:items-center">
          <div className="relative w-full md:w-80">
            <input
              type="text"
              placeholder={t('search')}
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="form-input pl-9 text-xs py-2"
            />
            <span className="absolute left-3 top-2.5 text-slate-400">
              <IconSearch size={15} />
            </span>
          </div>

          <div className="flex flex-wrap items-center gap-2">
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

            <button
              onClick={loadUsers}
              disabled={loading}
              className="p-1.5 border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-500 transition cursor-pointer"
              title="Refresh users directory"
            >
              <IconRefresh size={14} className={loading ? "animate-spin text-indigo-600" : ""} />
            </button>
          </div>
        </div>

        {/* User Table with all columns and complete actions */}
        <div className="table-wrapper border border-slate-200 rounded-xl overflow-hidden bg-white shadow-2xs">
          <table className="data-table w-full text-left border-collapse">
            <thead>
              <tr className="bg-slate-50/80 border-b border-slate-200">
                <th className="py-2.5 px-3.5 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[200px]">
                  User
                </th>
                <th className="py-2.5 px-3.5 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[110px]">
                  {t('status')}
                </th>
                <th className="py-2.5 px-3.5 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[180px]">
                  {t('userRoles')}
                </th>
                <th className="py-2.5 px-3.5 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[190px]">
                  Direct Permissions
                </th>
                <th className="py-2.5 px-3.5 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[130px]">
                  Last Login
                </th>
                <th className="py-2.5 px-3.5 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap text-right min-w-[240px]">
                  {t('actions')}
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {filteredUsers.map((u) => {
                const isLocked = !u.accountNonLocked;
                const isDisabled = !u.enabled;

                return (
                  <tr key={u.uuid} className="hover:bg-slate-50/70 transition-colors">
                    {/* User Identity */}
                    <td className="py-2.5 px-3.5 whitespace-nowrap">
                      <div className="flex items-center gap-2.5">
                        {u.pictureUrl ? (
                          <img
                            src={u.pictureUrl}
                            alt={u.username}
                            className="w-8 h-8 rounded-full object-cover border border-slate-200 shrink-0"
                          />
                        ) : (
                          <div className="w-8 h-8 rounded-full bg-indigo-50 border border-indigo-200 text-indigo-700 font-bold text-xs flex items-center justify-center shrink-0">
                            {u.givenName ? u.givenName.charAt(0).toUpperCase() : (u.username ? u.username.charAt(0).toUpperCase() : 'U')}
                          </div>
                        )}
                        <div className="min-w-0">
                          <div className="font-semibold text-slate-900 text-xs whitespace-nowrap truncate">
                            {u.givenName ? `${u.givenName} ${u.familyName}` : u.username}
                          </div>
                          <div className="text-[11px] text-slate-500 font-mono whitespace-nowrap truncate">
                            @{u.username} • {u.email}
                          </div>
                        </div>
                      </div>
                    </td>

                    {/* Status Badge */}
                    <td className="py-2.5 px-3.5 whitespace-nowrap">
                      <div className="flex items-center gap-1.5">
                        {isLocked ? (
                          <Badge variant="danger" size="sm">
                            <IconLock size={11} />
                            {t('locked')}
                          </Badge>
                        ) : isDisabled ? (
                          <Badge variant="warning" size="sm">
                            {t('disabled')}
                          </Badge>
                        ) : (
                          <Badge variant="success" size="sm">
                            <IconCheck size={11} />
                            {t('active')}
                          </Badge>
                        )}
                      </div>
                    </td>

                    {/* Roles Column (Clickable to manage) */}
                    <td className="py-2.5 px-3.5">
                      <div className="flex flex-wrap items-center gap-1">
                        {u.roles?.map(r => (
                          <span
                            key={r}
                            onClick={() => setRoleAssignUser(u)}
                            className="cursor-pointer hover:opacity-80 transition"
                            title="Click to assign / revoke roles"
                          >
                            <Badge variant={r === 'ADMIN' ? 'danger' : 'primary'} size="sm">
                              {r}
                            </Badge>
                          </span>
                        ))}
                        <button
                          type="button"
                          onClick={() => setRoleAssignUser(u)}
                          className="p-1 rounded text-slate-400 hover:text-indigo-600 hover:bg-indigo-50 transition cursor-pointer"
                          title="Assign role to user"
                        >
                          <IconKey size={13} />
                        </button>
                      </div>
                    </td>

                    {/* Direct Permissions Column (Clickable to manage) */}
                    <td className="py-2.5 px-3.5">
                      <div className="flex flex-wrap items-center gap-1">
                        {u.directPermissions && u.directPermissions.length > 0 ? (
                          u.directPermissions.slice(0, 2).map(p => (
                            <span
                              key={p.name}
                              onClick={() => setPermAssignUser(u)}
                              className="font-mono text-[10px] px-1.5 py-0.5 rounded bg-emerald-50 text-emerald-800 border border-emerald-200 cursor-pointer hover:bg-emerald-100 transition whitespace-nowrap"
                              title={`Direct permission: ${p.name} (${p.effect})`}
                            >
                              {p.name}
                            </span>
                          ))
                        ) : (
                          <span
                            onClick={() => setPermAssignUser(u)}
                            className="text-[11px] text-slate-400 italic cursor-pointer hover:text-indigo-600 transition"
                            title="Click to grant direct permissions"
                          >
                            Role-inherited
                          </span>
                        )}
                        {u.directPermissions && u.directPermissions.length > 2 && (
                          <span
                            onClick={() => setPermAssignUser(u)}
                            className="text-[10px] font-bold text-emerald-700 bg-emerald-100 px-1 py-0.5 rounded cursor-pointer"
                            title="View all direct permissions"
                          >
                            +{u.directPermissions.length - 2}
                          </span>
                        )}
                        <button
                          type="button"
                          onClick={() => setPermAssignUser(u)}
                          className="p-1 rounded text-slate-400 hover:text-emerald-600 hover:bg-emerald-50 transition cursor-pointer"
                          title="Grant direct permission"
                        >
                          <IconShieldCheck size={13} />
                        </button>
                      </div>
                    </td>

                    {/* Last Login */}
                    <td className="py-2.5 px-3.5 whitespace-nowrap text-xs text-slate-500 font-mono">
                      {u.lastLoginAt ? new Date(u.lastLoginAt).toLocaleString([], { dateStyle: 'short', timeStyle: 'short' }) : 'Never'}
                    </td>

                    {/* Full Action Suite */}
                    <td className="py-2.5 px-3.5 whitespace-nowrap text-right">
                      <div className="flex items-center justify-end gap-1">
                        {/* 1. Assign / Revoke Roles */}
                        <button
                          onClick={() => setRoleAssignUser(u)}
                          className="action-icon-btn text-indigo-600 hover:bg-indigo-50 p-1.5 rounded-lg border border-indigo-100"
                          title="User Role Assignment (Assign / Revoke Roles)"
                        >
                          <IconKey size={14} />
                        </button>

                        {/* 2. Grant / Revoke Direct Permissions */}
                        <button
                          onClick={() => setPermAssignUser(u)}
                          className="action-icon-btn text-emerald-600 hover:bg-emerald-50 p-1.5 rounded-lg border border-emerald-100"
                          title="User Permission Assignment (Grant / Revoke Permissions)"
                        >
                          <IconShieldCheck size={14} />
                        </button>

                        {/* 3. Reset User Password */}
                        <button
                          onClick={() => openResetPasswordModal(u)}
                          className="action-icon-btn text-amber-600 hover:bg-amber-50 p-1.5 rounded-lg border border-amber-100"
                          title="Reset User Password & Set Temp Password"
                        >
                          <IconRefresh size={14} />
                        </button>

                        {/* 4. Active Sessions for this user */}
                        <button
                          onClick={() => openUserSessionsModal(u)}
                          className="action-icon-btn text-sky-600 hover:bg-sky-50 p-1.5 rounded-lg border border-sky-100"
                          title="Inspect & Terminate Active User Sessions"
                        >
                          <IconLaptop size={14} />
                        </button>

                        {/* 5. Lock / Unlock toggle */}
                        <button
                          onClick={() => handleToggleLock(u)}
                          className={`action-icon-btn p-1.5 rounded-lg border ${
                            isLocked
                              ? 'text-amber-700 bg-amber-50 border-amber-200 hover:bg-amber-100'
                              : 'text-slate-500 border-slate-200 hover:text-amber-600 hover:bg-amber-50'
                          }`}
                          title={isLocked ? t('unlockUser') : t('lockUser')}
                        >
                          {isLocked ? <IconUnlock size={14} /> : <IconLock size={14} />}
                        </button>

                        {/* 6. Active / Inactive toggle */}
                        <button
                          onClick={() => handleToggleActive(u)}
                          className={`action-icon-btn p-1.5 rounded-lg border ${
                            isDisabled
                              ? 'text-emerald-700 bg-emerald-50 border-emerald-200 hover:bg-emerald-100'
                              : 'text-slate-500 border-slate-200 hover:text-rose-600 hover:bg-rose-50'
                          }`}
                          title={isDisabled ? t('activateUser') : t('deactivateUser')}
                        >
                          {isDisabled ? <IconCheck size={14} /> : <IconX size={14} />}
                        </button>

                        {/* 7. Edit User button */}
                        <button
                          onClick={() => setEditingUser({ ...u })}
                          className="action-icon-btn text-slate-500 border border-slate-200 hover:text-indigo-600 hover:bg-indigo-50 p-1.5 rounded-lg"
                          title={t('editUser')}
                        >
                          <IconEdit size={14} />
                        </button>

                        {/* 8. Delete User button */}
                        <button
                          onClick={() => setDeletingUser(u)}
                          className="action-icon-btn text-slate-500 border border-slate-200 hover:text-rose-600 hover:bg-rose-50 p-1.5 rounded-lg"
                          title={t('deleteUser')}
                        >
                          <IconTrash size={14} />
                        </button>
                      </div>
                    </td>
                  </tr>
                );
              })}

              {filteredUsers.length === 0 && (
                <tr>
                  <td colSpan="6" className="text-center py-8 text-slate-500 text-xs">
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
                className="form-input text-xs"
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">{t('familyName')}</label>
              <input
                type="text"
                value={newUser.familyName}
                onChange={(e) => setNewUser({ ...newUser, familyName: e.target.value })}
                className="form-input text-xs"
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
                className="form-input text-xs"
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">{t('email')}</label>
              <input
                type="email"
                value={newUser.email}
                onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
                className="form-input text-xs"
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
                className="form-input text-xs"
              />
            </div>
            <div className="form-group">
              <label className="form-label">Initial Role</label>
              <select
                value={newUser.role}
                onChange={(e) => setNewUser({ ...newUser, role: e.target.value })}
                className="form-input text-xs"
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
              className="btn btn-secondary text-xs"
            >
              {t('cancel')}
            </button>
            <button type="submit" className="btn btn-primary text-xs">
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
                  className="form-input text-xs"
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label">{t('familyName')}</label>
                <input
                  type="text"
                  value={editingUser.familyName || ''}
                  onChange={(e) => setEditingUser({ ...editingUser, familyName: e.target.value })}
                  className="form-input text-xs"
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
                  className="form-input text-xs"
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label">{t('phoneNumber')}</label>
                <input
                  type="tel"
                  value={editingUser.phoneNumber || ''}
                  onChange={(e) => setEditingUser({ ...editingUser, phoneNumber: e.target.value })}
                  className="form-input text-xs"
                />
              </div>
            </div>

            <div className="flex justify-end gap-2 pt-2">
              <button
                type="button"
                onClick={() => setEditingUser(null)}
                className="btn btn-secondary text-xs"
              >
                {t('cancel')}
              </button>
              <button type="submit" className="btn btn-primary text-xs">
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
          <p className="text-xs text-slate-600">
            Are you sure you want to permanently delete user <strong>{deletingUser?.username}</strong> ({deletingUser?.email})? This action cannot be undone.
          </p>
          <div className="flex justify-end gap-2 pt-2">
            <button onClick={() => setDeletingUser(null)} className="btn btn-secondary text-xs">
              {t('cancel')}
            </button>
            <button onClick={handleDelete} className="btn btn-danger text-xs">
              {t('delete')}
            </button>
          </div>
        </div>
      </Modal>

      {/* Admin Reset Password Modal */}
      <Modal
        isOpen={!!resetPasswordUser}
        onClose={() => setResetPasswordUser(null)}
        title={`Reset Password (@${resetPasswordUser?.username})`}
      >
        {resetPasswordUser && (
          <form onSubmit={handleAdminResetPassword} className="space-y-4">
            <p className="text-xs text-slate-600">
              Provide a temporary password for <strong>@{resetPasswordUser.username}</strong> or use the auto-generated secure password below.
            </p>

            <div className="form-group">
              <label className="form-label">Temporary Password</label>
              <div className="relative">
                <input
                  type="text"
                  value={tempPassword}
                  onChange={(e) => setTempPassword(e.target.value)}
                  className="form-input text-xs font-mono pr-20"
                  required
                />
                <button
                  type="button"
                  onClick={() => {
                    navigator.clipboard.writeText(tempPassword);
                    onShowToast("Password copied to clipboard!", "success");
                  }}
                  className="absolute right-2 top-1/2 -translate-y-1/2 text-xs font-semibold text-indigo-600 hover:text-indigo-800 px-2 py-0.5 rounded bg-indigo-50 border border-indigo-200"
                >
                  Copy
                </button>
              </div>
            </div>

            <div className="flex items-center gap-2">
              <input
                type="checkbox"
                id="requirePassChange"
                checked={requirePassChange}
                onChange={(e) => setRequirePassChange(e.target.checked)}
                className="w-4 h-4 text-indigo-600 rounded border-slate-300"
              />
              <label htmlFor="requirePassChange" className="text-xs text-slate-700 font-medium">
                Require password change upon next sign in
              </label>
            </div>

            <div className="flex justify-end gap-2 pt-2">
              <button
                type="button"
                onClick={() => setResetPasswordUser(null)}
                className="btn btn-secondary text-xs"
              >
                {t('cancel')}
              </button>
              <button type="submit" className="btn btn-primary text-xs">
                Confirm Password Reset
              </button>
            </div>
          </form>
        )}
      </Modal>

      {/* Active User Sessions Modal */}
      <Modal
        isOpen={!!sessionUser}
        onClose={() => setSessionUser(null)}
        title={`Active Sessions (@${sessionUser?.username})`}
      >
        {sessionUser && (
          <div className="space-y-4">
            <p className="text-xs text-slate-600">
              Active concurrent device sessions connected to <strong>@{sessionUser.username}</strong>.
            </p>

            {sessionLoading ? (
              <div className="py-6 text-center text-xs text-slate-400">Loading user sessions...</div>
            ) : userSessionsList.length > 0 ? (
              <div className="space-y-2 max-h-72 overflow-y-auto">
                {userSessionsList.map(s => (
                  <div key={s.id} className="p-3 bg-slate-50 border border-slate-200 rounded-xl flex items-center justify-between text-xs">
                    <div className="flex items-center gap-2.5">
                      <div className="p-1.5 bg-white border border-slate-200 rounded-lg text-indigo-600">
                        <IconLaptop size={16} />
                      </div>
                      <div>
                        <div className="font-bold text-slate-900">{s.deviceName} ({s.browser})</div>
                        <div className="text-[11px] text-slate-500 font-mono">
                          {s.ipAddress} • {s.location} • Active: {s.lastActive}
                        </div>
                      </div>
                    </div>
                    <button
                      onClick={() => handleTerminateUserSession(s.id)}
                      className="px-2.5 py-1 rounded-lg bg-rose-50 text-rose-600 hover:bg-rose-100 border border-rose-200 font-semibold text-xs transition"
                    >
                      Terminate
                    </button>
                  </div>
                ))}
              </div>
            ) : (
              <div className="py-6 text-center text-xs text-slate-400">
                No active remote sessions detected for this user.
              </div>
            )}

            <div className="flex justify-end pt-2">
              <button
                type="button"
                onClick={() => setSessionUser(null)}
                className="btn btn-secondary text-xs"
              >
                {t('close')}
              </button>
            </div>
          </div>
        )}
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
