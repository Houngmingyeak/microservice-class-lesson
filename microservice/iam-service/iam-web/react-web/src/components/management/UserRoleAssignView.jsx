import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { Badge } from '../common/Badge';
import { UserRoleAssignModal } from './UserRoleAssignModal';
import {
  IconKey,
  IconSearch,
  IconPlus,
  IconTrash,
  IconShieldCheck,
  IconUserCheck,
  IconRefresh
} from '../common/Icons';

export const UserRoleAssignView = ({ onShowToast }) => {
  const { t } = useI18n();
  const [users, setUsers] = useState([]);
  const [roles, setRoles] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [roleFilter, setRoleFilter] = useState('ALL');
  const [loading, setLoading] = useState(true);
  const [selectedUser, setSelectedUser] = useState(null);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const [uList, rList] = await Promise.all([
        api.getUsers(),
        api.getRoles()
      ]);
      setUsers(uList || []);
      setRoles(rList || []);
    } catch (err) {
      onShowToast(err.message || 'Failed to load user role assignments', 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleRevokeRole = async (user, roleName) => {
    try {
      await api.revokeRoleFromUser(user.uuid, roleName, 'Revoked via User Role Assignment view');
      onShowToast(`Role ${roleName} revoked from @${user.username}`, 'success');
      loadData();
    } catch (err) {
      onShowToast(err.message || 'Failed to revoke role', 'error');
    }
  };

  const filtered = users.filter((u) => {
    const term = searchTerm.toLowerCase().trim();
    const matchesSearch =
      u.username.toLowerCase().includes(term) ||
      u.email.toLowerCase().includes(term) ||
      `${u.givenName} ${u.familyName}`.toLowerCase().includes(term);

    const matchesRole = roleFilter === 'ALL' || u.roles?.includes(roleFilter);
    return matchesSearch && matchesRole;
  });

  const totalUsersWithRoles = users.filter(u => u.roles && u.roles.length > 0).length;
  const adminUsersCount = users.filter(u => u.roles?.includes('ADMIN')).length;
  const standardUsersCount = users.filter(u => u.roles?.includes('USER')).length;

  return (
    <div className="view-container">
      {/* View Header */}
      <div className="view-header flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
        <div>
          <h2 className="view-title text-lg font-bold text-slate-900 tracking-tight">
            {t('tabUserRoles') || 'User Role Assignment'}
          </h2>
          <p className="view-subtitle text-slate-500 text-xs mt-0.5">
            Assign, modify, and revoke role memberships from directory users with reasons and expiration.
          </p>
        </div>
        <div className="flex items-center gap-2">
          <button
            onClick={loadData}
            disabled={loading}
            className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg border border-slate-200 bg-white hover:bg-slate-50 text-slate-700 font-semibold text-xs shadow-2xs transition disabled:opacity-60 cursor-pointer whitespace-nowrap"
            title="Refresh assignments"
          >
            <IconRefresh size={14} className={loading ? "animate-spin text-indigo-600" : "text-slate-500"} />
            <span>{loading ? 'Refreshing...' : 'Refresh'}</span>
          </button>
        </div>
      </div>

      {/* Top Stat Summary Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-5">
        <div className="stat-card p-4 rounded-xl bg-white border border-slate-200/80 shadow-xs flex items-center justify-between">
          <div className="min-w-0">
            <p className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-0.5 whitespace-nowrap truncate">
              Assigned Directory Users
            </p>
            <h3 className="text-xl font-bold text-slate-900">{totalUsersWithRoles}</h3>
            <span className="text-[11px] text-slate-500 font-medium mt-0.5 inline-block whitespace-nowrap">
              Users with at least 1 role
            </span>
          </div>
          <div className="w-10 h-10 rounded-xl bg-indigo-50 border border-indigo-100 flex items-center justify-center text-indigo-600 shrink-0 ml-3">
            <IconUserCheck size={18} />
          </div>
        </div>

        <div className="stat-card p-4 rounded-xl bg-white border border-slate-200/80 shadow-xs flex items-center justify-between">
          <div className="min-w-0">
            <p className="text-xs font-bold uppercase tracking-wider text-rose-600 mb-0.5 whitespace-nowrap truncate">
              Admin Privilege Holders
            </p>
            <h3 className="text-xl font-bold text-rose-700">{adminUsersCount}</h3>
            <span className="text-[11px] text-rose-600 font-medium mt-0.5 inline-block whitespace-nowrap">
              Full administrative authority
            </span>
          </div>
          <div className="w-10 h-10 rounded-xl bg-rose-50 border border-rose-100 flex items-center justify-center text-rose-600 shrink-0 ml-3">
            <IconKey size={18} />
          </div>
        </div>

        <div className="stat-card p-4 rounded-xl bg-white border border-slate-200/80 shadow-xs flex items-center justify-between">
          <div className="min-w-0">
            <p className="text-xs font-bold uppercase tracking-wider text-emerald-600 mb-0.5 whitespace-nowrap truncate">
              Standard User Accounts
            </p>
            <h3 className="text-xl font-bold text-emerald-700">{standardUsersCount}</h3>
            <span className="text-[11px] text-emerald-600 font-medium mt-0.5 inline-block whitespace-nowrap">
              Default authenticated users
            </span>
          </div>
          <div className="w-10 h-10 rounded-xl bg-emerald-50 border border-emerald-100 flex items-center justify-center text-emerald-600 shrink-0 ml-3">
            <IconShieldCheck size={18} />
          </div>
        </div>
      </div>

      {/* Main Table Card */}
      <div className="glass-card p-4 sm:p-5 bg-white rounded-xl border border-slate-200/80 shadow-xs">
        {/* Search & Filter Toolbar */}
        <div className="flex flex-col sm:flex-row gap-3 mb-4 justify-between items-stretch sm:items-center">
          <div className="relative w-full sm:w-80">
            <span className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none">
              <IconSearch size={15} />
            </span>
            <input
              type="text"
              placeholder="Search user by name, username, or email..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="form-input w-full pl-9 pr-4 py-1.5 rounded-lg border border-slate-200 bg-white text-slate-800 text-xs focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition shadow-2xs"
            />
          </div>

          <div className="flex items-center gap-2">
            <select
              value={roleFilter}
              onChange={(e) => setRoleFilter(e.target.value)}
              className="form-input text-xs py-1.5 rounded-lg border-slate-200"
            >
              <option value="ALL">All Roles ({roles.length})</option>
              {roles.map(r => (
                <option key={r.uuid || r.name} value={r.name}>{r.name}</option>
              ))}
            </select>
          </div>
        </div>

        {/* User Role Assignment Table */}
        <div className="table-wrapper border border-slate-200 rounded-xl overflow-hidden bg-white shadow-2xs">
          <table className="data-table w-full text-left border-collapse">
            <thead>
              <tr className="bg-slate-50/80 border-b border-slate-200">
                <th className="py-2.5 px-3.5 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[200px]">
                  Target User
                </th>
                <th className="py-2.5 px-3.5 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[260px]">
                  Assigned Roles (Click to Revoke)
                </th>
                <th className="py-2.5 px-3.5 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[120px]">
                  Account Status
                </th>
                <th className="py-2.5 px-3.5 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap min-w-[120px]">
                  Role Count
                </th>
                <th className="py-2.5 px-3.5 text-xs font-bold uppercase tracking-wider text-slate-500 whitespace-nowrap text-right min-w-[140px]">
                  Action
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {filtered.map((u) => (
                <tr key={u.uuid} className="hover:bg-slate-50/70 transition-colors">
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
                          {u.givenName ? u.givenName.charAt(0).toUpperCase() : u.username.charAt(0).toUpperCase()}
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

                  <td className="py-2.5 px-3.5">
                    <div className="flex flex-wrap items-center gap-1.5">
                      {u.roles?.map((r) => (
                        <span
                          key={r}
                          className={`inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-[11px] font-semibold border ${
                            r === 'ADMIN'
                              ? 'bg-rose-50 text-rose-700 border-rose-200'
                              : 'bg-indigo-50 text-indigo-700 border-indigo-200'
                          }`}
                        >
                          <IconKey size={11} />
                          <span>{r}</span>
                          <button
                            type="button"
                            onClick={() => handleRevokeRole(u, r)}
                            className="text-slate-400 hover:text-rose-600 transition ml-0.5 cursor-pointer"
                            title={`Revoke role ${r}`}
                          >
                            <IconTrash size={10} />
                          </button>
                        </span>
                      ))}
                      {(!u.roles || u.roles.length === 0) && (
                        <span className="text-[11px] text-slate-400 italic">No roles assigned</span>
                      )}
                    </div>
                  </td>

                  <td className="py-2.5 px-3.5 whitespace-nowrap">
                    <Badge variant={u.enabled ? 'success' : 'danger'} size="sm">
                      {u.enabled ? 'Enabled' : 'Disabled'}
                    </Badge>
                  </td>

                  <td className="py-2.5 px-3.5 whitespace-nowrap font-mono text-xs text-slate-600">
                    {u.roles?.length || 0} role(s)
                  </td>

                  <td className="py-2.5 px-3.5 whitespace-nowrap text-right">
                    <button
                      onClick={() => setSelectedUser(u)}
                      className="inline-flex items-center gap-1 px-2.5 py-1 rounded-lg bg-indigo-50 hover:bg-indigo-100 text-indigo-700 border border-indigo-200 text-xs font-semibold transition cursor-pointer"
                    >
                      <IconPlus size={12} />
                      <span>Assign / Modify</span>
                    </button>
                  </td>
                </tr>
              ))}

              {filtered.length === 0 && (
                <tr>
                  <td colSpan="5" className="text-center py-8 text-slate-400 text-xs">
                    No users matching search filters
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Role Assignment Modal */}
      <UserRoleAssignModal
        user={selectedUser}
        isOpen={!!selectedUser}
        onClose={() => setSelectedUser(null)}
        onUpdated={loadData}
        onShowToast={onShowToast}
      />
    </div>
  );
};
