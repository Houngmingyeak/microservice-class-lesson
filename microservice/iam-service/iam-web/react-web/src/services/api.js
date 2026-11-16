import { store, initialCurrentUser } from './mockData';

// Simulated API helper with local store fallback for 100% reliability
export const api = {
  // --- ACCOUNT ENDPOINTS ---
  
  // Get current user profile (/api/me)
  async getProfile() {
    const user = store.get('current_user') || initialCurrentUser;
    return { ...user };
  },

  // Update profile (/api/me)
  async updateProfile(updates) {
    const current = store.get('current_user') || initialCurrentUser;
    const updated = { ...current, ...updates, updatedAt: new Date().toISOString() };
    store.set('current_user', updated);

    // Also update in all users directory
    const users = store.get('users') || [];
    const idx = users.findIndex(u => u.username === current.username);
    if (idx !== -1) {
      users[idx] = { ...users[idx], ...updates };
      store.set('users', users);
    }

    // Log activity
    this.logActivity("PROFILE_UPDATE", `Updated personal details: ${Object.keys(updates).join(', ')}`);
    return updated;
  },

  // Change password (/api/me/password)
  async changePassword(currentPassword, newPassword) {
    if (newPassword.length < 8) {
      throw new Error("Password must be at least 8 characters");
    }
    const current = store.get('current_user') || initialCurrentUser;
    current.passwordChangedAt = new Date().toISOString();
    store.set('current_user', current);

    this.logActivity("PASSWORD_CHANGED", "Self-service password updated successfully");
    return true;
  },

  // Linked accounts (/api/me/identities)
  async getLinkedAccounts() {
    return store.get('linked_accounts') || [];
  },

  async unlinkAccount(uuid) {
    const list = store.get('linked_accounts') || [];
    const target = list.find(i => i.uuid === uuid);
    if (target) {
      target.connected = false;
      store.set('linked_accounts', list);
      this.logActivity("IDENTITY_UNLINKED", `Unlinked ${target.provider} account`);
    }
    return list;
  },

  async linkAccount(provider) {
    const list = store.get('linked_accounts') || [];
    const target = list.find(i => i.provider.toLowerCase() === provider.toLowerCase());
    if (target) {
      target.connected = true;
      target.linkedAt = new Date().toISOString();
      store.set('linked_accounts', list);
      this.logActivity("IDENTITY_LINKED", `Connected ${target.provider} account`);
    }
    return list;
  },

  // Active Sessions
  async getActiveSessions() {
    return store.get('active_sessions') || [];
  },

  async terminateSession(sessionId) {
    let sessions = store.get('active_sessions') || [];
    const terminated = sessions.find(s => s.id === sessionId);
    sessions = sessions.filter(s => s.id !== sessionId);
    store.set('active_sessions', sessions);
    this.logActivity("SESSION_TERMINATED", `Terminated session on ${terminated?.deviceName || sessionId}`);
    return sessions;
  },

  async terminateAllOtherSessions() {
    let sessions = store.get('active_sessions') || [];
    sessions = sessions.filter(s => s.isCurrent);
    store.set('active_sessions', sessions);
    this.logActivity("SESSION_TERMINATED_ALL", "Forcibly closed all other remote user sessions");
    return sessions;
  },

  // Login History
  async getLoginHistory() {
    return store.get('login_history') || [];
  },

  // Account Activities
  async getAccountActivities() {
    return store.get('account_activities') || [];
  },

  logActivity(action, description) {
    const list = store.get('account_activities') || [];
    const current = store.get('current_user') || initialCurrentUser;
    const newAct = {
      id: "act-" + Date.now(),
      timestamp: new Date().toISOString(),
      action,
      description,
      actor: current.username,
      ip: "116.212.155.82"
    };
    list.unshift(newAct);
    store.set('account_activities', list);

    // Also add to global audit logs
    const auditLogs = store.get('audit_logs') || [];
    auditLogs.unshift({
      id: "aud-" + Date.now(),
      timestamp: new Date().toISOString(),
      actor: current.username,
      event: action,
      target: current.username,
      ipAddress: "116.212.155.82",
      status: "SUCCESS",
      details: description
    });
    store.set('audit_logs', auditLogs);
  },

  // Account Deactivation
  async requestDeactivation(reason, password, notes) {
    if (!password) {
      throw new Error("Password confirmation is required for deactivation");
    }
    this.logActivity("DEACTIVATION_REQUESTED", `Deactivation requested. Reason: ${reason}. Notes: ${notes || 'None'}`);
    return { success: true };
  },

  // --- ADMINISTRATION & MANAGEMENT ENDPOINTS ---

  // Admin Overview
  async getAdminOverview() {
    const users = store.get('users') || [];
    const roles = store.get('roles') || [];
    const permissions = store.get('permissions') || [];
    const clients = store.get('clients') || [];
    const sessions = store.get('active_sessions') || [];
    const auditLogs = store.get('audit_logs') || [];

    return {
      totalUsers: users.length,
      activeUsers: users.filter(u => u.enabled && u.accountNonLocked).length,
      lockedUsers: users.filter(u => !u.accountNonLocked).length,
      totalRoles: roles.length,
      totalPermissions: permissions.length,
      totalClients: clients.length,
      activeSessions: sessions.length,
      recentActivities: auditLogs.slice(0, 6)
    };
  },

  // Users Management (/api/users)
  async getUsers() {
    return store.get('users') || [];
  },

  async createUser(userData) {
    const users = store.get('users') || [];
    const newUser = {
      uuid: "usr-" + Date.now(),
      username: userData.username,
      email: userData.email,
      givenName: userData.givenName || "",
      familyName: userData.familyName || "",
      phoneNumber: userData.phoneNumber || "",
      enabled: true,
      accountNonLocked: true,
      roles: userData.roles || ["USER"],
      directPermissions: [],
      createdAt: new Date().toISOString(),
      lastLoginAt: null,
      avatar: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&q=80&w=256"
    };
    users.unshift(newUser);
    store.set('users', users);
    this.logAdminAudit("USER_CREATE", newUser.username, `Admin provisioned user ${newUser.username}`);
    return newUser;
  },

  async updateUser(uuid, updates) {
    const users = store.get('users') || [];
    const idx = users.findIndex(u => u.uuid === uuid);
    if (idx !== -1) {
      users[idx] = { ...users[idx], ...updates };
      store.set('users', users);
      this.logAdminAudit("USER_UPDATE", users[idx].username, `Updated attributes for user ${users[idx].username}`);
      return users[idx];
    }
    throw new Error("User not found");
  },

  async toggleUserActive(uuid) {
    const users = store.get('users') || [];
    const user = users.find(u => u.uuid === uuid);
    if (user) {
      user.enabled = !user.enabled;
      store.set('users', users);
      this.logAdminAudit(user.enabled ? "USER_ENABLE" : "USER_DISABLE", user.username, `${user.enabled ? 'Enabled' : 'Disabled'} user ${user.username}`);
      return user;
    }
  },

  async toggleUserLock(uuid) {
    const users = store.get('users') || [];
    const user = users.find(u => u.uuid === uuid);
    if (user) {
      user.accountNonLocked = !user.accountNonLocked;
      store.set('users', users);
      this.logAdminAudit(user.accountNonLocked ? "USER_UNLOCK" : "USER_LOCK", user.username, `${user.accountNonLocked ? 'Unlocked' : 'Locked'} user account ${user.username}`);
      return user;
    }
  },

  async deleteUser(uuid) {
    let users = store.get('users') || [];
    const user = users.find(u => u.uuid === uuid);
    users = users.filter(u => u.uuid !== uuid);
    store.set('users', users);
    if (user) {
      this.logAdminAudit("USER_DELETE", user.username, `Deleted user account ${user.username}`);
    }
    return true;
  },

  // User Role Assignment
  async assignRolesToUser(userUuid, roleNames, reason, expiresAt) {
    const users = store.get('users') || [];
    const user = users.find(u => u.uuid === userUuid);
    if (user) {
      user.roles = Array.from(new Set([...user.roles, ...roleNames]));
      store.set('users', users);
      this.logAdminAudit("ROLE_ASSIGN", user.username, `Assigned roles [${roleNames.join(', ')}] with reason: ${reason || 'N/A'}`);
      return user;
    }
  },

  async revokeRoleFromUser(userUuid, roleName, reason) {
    const users = store.get('users') || [];
    const user = users.find(u => u.uuid === userUuid);
    if (user) {
      user.roles = user.roles.filter(r => r !== roleName);
      store.set('users', users);
      this.logAdminAudit("ROLE_REVOKE", user.username, `Revoked role '${roleName}' with reason: ${reason || 'Admin Action'}`);
      return user;
    }
  },

  // User Direct Permissions
  async grantPermissionToUser(userUuid, permName, effect = "ALLOW", expiresAt = null, reason = "") {
    const users = store.get('users') || [];
    const user = users.find(u => u.uuid === userUuid);
    if (user) {
      if (!user.directPermissions) user.directPermissions = [];
      user.directPermissions = user.directPermissions.filter(p => p.name !== permName);
      user.directPermissions.push({ name: permName, effect, expiresAt, reason });
      store.set('users', users);
      this.logAdminAudit("PERMISSION_GRANT", user.username, `Granted direct permission ${permName} (${effect})`);
      return user;
    }
  },

  async revokePermissionFromUser(userUuid, permName) {
    const users = store.get('users') || [];
    const user = users.find(u => u.uuid === userUuid);
    if (user && user.directPermissions) {
      user.directPermissions = user.directPermissions.filter(p => p.name !== permName);
      store.set('users', users);
      this.logAdminAudit("PERMISSION_REVOKE", user.username, `Revoked direct permission ${permName}`);
      return user;
    }
  },

  // Roles (/api/roles)
  async getRoles() {
    return store.get('roles') || [];
  },

  async createRole(roleData) {
    const roles = store.get('roles') || [];
    const newRole = {
      uuid: "role-" + Date.now(),
      name: roleData.name.toUpperCase(),
      description: roleData.description,
      isSystem: false,
      isActive: true,
      userCount: 0,
      permissions: roleData.permissions || []
    };
    roles.push(newRole);
    store.set('roles', roles);
    this.logAdminAudit("ROLE_CREATE", newRole.name, `Created custom role ${newRole.name}`);
    return newRole;
  },

  async updateRole(uuid, updates) {
    const roles = store.get('roles') || [];
    const idx = roles.findIndex(r => r.uuid === uuid);
    if (idx !== -1) {
      roles[idx] = { ...roles[idx], ...updates };
      store.set('roles', roles);
      this.logAdminAudit("ROLE_UPDATE", roles[idx].name, `Updated role ${roles[idx].name}`);
      return roles[idx];
    }
  },

  async deleteRole(uuid) {
    let roles = store.get('roles') || [];
    const target = roles.find(r => r.uuid === uuid);
    if (target?.isSystem) {
      throw new Error("System roles cannot be deleted");
    }
    roles = roles.filter(r => r.uuid !== uuid);
    store.set('roles', roles);
    if (target) {
      this.logAdminAudit("ROLE_DELETE", target.name, `Removed role ${target.name}`);
    }
    return true;
  },

  // Permissions (/api/permissions)
  async getPermissions() {
    return store.get('permissions') || [];
  },

  async createPermission(permData) {
    const perms = store.get('permissions') || [];
    const fullName = `${permData.resource}:${permData.action}`;
    if (perms.some(p => p.name === fullName)) {
      throw new Error(`Permission ${fullName} already exists`);
    }
    const newPerm = {
      uuid: "p-" + Date.now(),
      name: fullName,
      resource: permData.resource,
      action: permData.action,
      description: permData.description,
      isActive: true
    };
    perms.push(newPerm);
    store.set('permissions', perms);
    this.logAdminAudit("PERMISSION_CREATE", fullName, `Created permission ${fullName}`);
    return newPerm;
  },

  async updatePermission(uuid, updates) {
    const perms = store.get('permissions') || [];
    const idx = perms.findIndex(p => p.uuid === uuid);
    if (idx !== -1) {
      perms[idx] = { ...perms[idx], ...updates };
      store.set('permissions', perms);
      this.logAdminAudit("PERMISSION_UPDATE", perms[idx].name, `Updated permission ${perms[idx].name}`);
      return perms[idx];
    }
  },

  async deletePermission(uuid) {
    let perms = store.get('permissions') || [];
    const target = perms.find(p => p.uuid === uuid);
    perms = perms.filter(p => p.uuid !== uuid);
    store.set('permissions', perms);
    if (target) {
      this.logAdminAudit("PERMISSION_DELETE", target.name, `Removed permission ${target.name}`);
    }
    return true;
  },

  // OAuth2 Clients (/api/clients)
  async getClients() {
    return store.get('clients') || [];
  },

  async createClient(clientData) {
    const clients = store.get('clients') || [];
    const newClient = {
      id: "cli-" + Date.now(),
      clientId: clientData.clientId,
      clientName: clientData.clientName,
      clientSecret: clientData.clientSecret || "sec_" + Math.random().toString(36).substring(2, 12),
      authorizationGrantTypes: clientData.authorizationGrantTypes || ["authorization_code", "refresh_token"],
      redirectUris: clientData.redirectUris || [],
      postLogoutRedirectUris: clientData.postLogoutRedirectUris || [],
      scopes: clientData.scopes || ["openid", "profile", "email"],
      clientAuthenticationMethods: clientData.clientAuthenticationMethods || ["client_secret_basic"],
      createdAt: new Date().toISOString(),
      isActive: true
    };
    clients.push(newClient);
    store.set('clients', clients);
    this.logAdminAudit("CLIENT_REGISTER", newClient.clientId, `Registered client application ${newClient.clientName}`);
    return newClient;
  },

  async updateClient(id, updates) {
    const clients = store.get('clients') || [];
    const idx = clients.findIndex(c => c.id === id);
    if (idx !== -1) {
      clients[idx] = { ...clients[idx], ...updates };
      store.set('clients', clients);
      this.logAdminAudit("CLIENT_UPDATE", clients[idx].clientId, `Updated settings for OAuth2 client ${clients[idx].clientId}`);
      return clients[idx];
    }
  },

  async deleteClient(id) {
    let clients = store.get('clients') || [];
    const target = clients.find(c => c.id === id);
    clients = clients.filter(c => c.id !== id);
    store.set('clients', clients);
    if (target) {
      this.logAdminAudit("CLIENT_DELETE", target.clientId, `Deregistered client application ${target.clientName}`);
    }
    return true;
  },

  async regenerateClientSecret(id) {
    const clients = store.get('clients') || [];
    const client = clients.find(c => c.id === id);
    if (client) {
      client.clientSecret = "sec_" + Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
      store.set('clients', clients);
      this.logAdminAudit("CLIENT_SECRET_REGEN", client.clientId, `Regenerated credentials for client ${client.clientId}`);
      return client.clientSecret;
    }
  },

  // Security Management (Admin)
  async adminResetPassword(userUuid, newPassword) {
    const users = store.get('users') || [];
    const user = users.find(u => u.uuid === userUuid);
    if (user) {
      user.passwordChangedAt = new Date().toISOString();
      store.set('users', users);
      this.logAdminAudit("ADMIN_PASSWORD_RESET", user.username, `Forced password reset for user ${user.username}`);
      return true;
    }
  },

  // Audit Logs (/api/audit-logs)
  async getAuditLogs() {
    return store.get('audit_logs') || [];
  },

  logAdminAudit(event, target, details) {
    const logs = store.get('audit_logs') || [];
    const current = store.get('current_user') || initialCurrentUser;
    logs.unshift({
      id: "aud-" + Date.now(),
      timestamp: new Date().toISOString(),
      actor: current.username + " (Admin)",
      event,
      target,
      ipAddress: "116.212.155.82",
      status: "SUCCESS",
      details
    });
    store.set('audit_logs', logs);
  },

  // System Configuration (/api/config)
  async getSystemConfig() {
    return store.get('system_config');
  },

  async updateSystemConfig(newConfig) {
    store.set('system_config', newConfig);
    this.logAdminAudit("CONFIG_UPDATE", "System Security Policy", "Updated global authentication, password, and token policies");
    return newConfig;
  }
};
