// Mock data seeded to match ISTAD IAM platform specs exactly

export const initialCurrentUser = {
  uuid: "00000000-0000-0000-0000-000000000001",
  username: "admin",
  email: "admin@istad.edu.kh",
  emailVerified: true,
  givenName: "Admin",
  familyName: "User",
  nickname: "Admin",
  phoneNumber: "",
  phoneNumberVerified: false,
  gender: "MALE",
  birthdate: "2000-01-01",
  pictureUrl: null,
  addressStreetAddress: "",
  addressLocality: "",
  addressCountry: "KH",
  locale: "en",
  enabled: true,
  accountNonLocked: true,
  mfaEnabled: false,
  lastLoginAt: new Date().toISOString(),
  passwordChangedAt: new Date().toISOString(),
  roles: ["ADMIN", "USER"],
  effectivePermissions: [
    "user:create", "user:read", "user:update", "user:delete", "user:assign",
    "role:create", "role:read", "role:update", "role:delete", "role:assign",
    "permission:create", "permission:read", "permission:update", "permission:delete",
    "client:create", "client:read", "client:update", "client:delete",
    "audit:read", "config:read", "config:update", "session:read", "session:terminate"
  ]
};

export const initialLinkedAccounts = [
  {
    uuid: "ident-1",
    provider: "Google",
    providerSubject: "109823471092837",
    email: "mingyeak.istad@gmail.com",
    displayName: "Houng Mingyeak (Google)",
    linkedAt: "2026-02-10T08:30:00Z",
    connected: true,
  },
  {
    uuid: "ident-2",
    provider: "GitHub",
    providerSubject: "gh_7482910",
    email: "mingyeak-dev@github.com",
    displayName: "houngmingyeak",
    linkedAt: "2026-03-01T14:20:00Z",
    connected: true,
  },
  {
    uuid: "ident-3",
    provider: "Facebook",
    providerSubject: "fb_893247923",
    email: "mingyeak.fb@istad.edu.kh",
    displayName: "Mingyeak H.",
    linkedAt: "2026-05-18T10:15:00Z",
    connected: false,
  },
  {
    uuid: "ident-4",
    provider: "Telegram",
    providerSubject: "tg_9923841",
    email: null,
    displayName: "@mingyeak_it",
    linkedAt: "2026-07-22T19:00:00Z",
    connected: true,
  }
];

export const initialActiveSessions = [
  {
    id: "sess-01",
    userId: "00000000-0000-0000-0000-000000000001",
    username: "houngmingyeak",
    deviceType: "Desktop",
    deviceName: "Windows 11 Workstation",
    browser: "Chrome 128.0",
    ipAddress: "116.212.155.82",
    location: "Phnom Penh, Cambodia",
    startedAt: new Date(Date.now() - 1000 * 60 * 60 * 3).toISOString(),
    lastActive: "Just now",
    isCurrent: true
  },
  {
    id: "sess-02",
    userId: "00000000-0000-0000-0000-000000000001",
    username: "houngmingyeak",
    deviceType: "Mobile",
    deviceName: "iPhone 15 Pro",
    browser: "Mobile Safari 17.4",
    ipAddress: "103.243.140.21",
    location: "Phnom Penh, Cambodia",
    startedAt: new Date(Date.now() - 1000 * 60 * 60 * 24).toISOString(),
    lastActive: "2 hours ago",
    isCurrent: false
  },
  {
    id: "sess-03",
    userId: "00000000-0000-0000-0000-000000000001",
    username: "houngmingyeak",
    deviceType: "Laptop",
    deviceName: "MacBook Pro M3 Max",
    browser: "Safari 17.5",
    ipAddress: "175.100.12.94",
    location: "Siem Reap, Cambodia",
    startedAt: new Date(Date.now() - 1000 * 60 * 60 * 48).toISOString(),
    lastActive: "Yesterday",
    isCurrent: false
  }
];

export const initialLoginHistory = [
  {
    id: "log-1",
    timestamp: new Date(Date.now() - 1000 * 60 * 12).toISOString(),
    username: "houngmingyeak",
    ipAddress: "116.212.155.82",
    device: "Windows 11 / Chrome 128",
    location: "Phnom Penh, Cambodia",
    status: "SUCCESS",
    reason: "OAuth2 Password / MFA verified"
  },
  {
    id: "log-2",
    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 5).toISOString(),
    username: "houngmingyeak",
    ipAddress: "103.243.140.21",
    device: "iOS 17.4 / Mobile Safari",
    location: "Phnom Penh, Cambodia",
    status: "SUCCESS",
    reason: "Google OAuth2 SSO"
  },
  {
    id: "log-3",
    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 24).toISOString(),
    username: "houngmingyeak",
    ipAddress: "185.220.101.5",
    device: "Linux / Firefox 120",
    location: "Frankfurt, Germany",
    status: "FAILED",
    reason: "Invalid password attempt (Blocked by geo-fence)"
  },
  {
    id: "log-4",
    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 36).toISOString(),
    username: "houngmingyeak",
    ipAddress: "116.212.155.82",
    device: "Windows 11 / Chrome 128",
    location: "Phnom Penh, Cambodia",
    status: "SUCCESS",
    reason: "Session refresh"
  },
  {
    id: "log-5",
    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 72).toISOString(),
    username: "houngmingyeak",
    ipAddress: "175.100.12.94",
    device: "macOS 14.2 / Safari",
    location: "Siem Reap, Cambodia",
    status: "SUCCESS",
    reason: "Standard Authorization Code Flow"
  }
];

export const initialAccountActivities = [
  {
    id: "act-1",
    timestamp: new Date(Date.now() - 1000 * 60 * 30).toISOString(),
    action: "PROFILE_UPDATE",
    description: "Updated telephone number and locale preference to Khmer",
    actor: "houngmingyeak",
    ip: "116.212.155.82"
  },
  {
    id: "act-2",
    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 14).toISOString(),
    action: "SECURITY_2FA_ENABLED",
    description: "Enabled TOTP Authenticator App for two-factor authentication",
    actor: "houngmingyeak",
    ip: "116.212.155.82"
  },
  {
    id: "act-3",
    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 48).toISOString(),
    action: "IDENTITY_LINKED",
    description: "Linked Telegram social account (@mingyeak_it)",
    actor: "houngmingyeak",
    ip: "103.243.140.21"
  },
  {
    id: "act-4",
    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 24 * 15).toISOString(),
    action: "PASSWORD_CHANGED",
    description: "User successfully modified self-service password",
    actor: "houngmingyeak",
    ip: "116.212.155.82"
  }
];

export const initialUsers = [];

export const initialRoles = [
  {
    uuid: "role-01",
    name: "ADMIN",
    description: "Super Administrator with full unrestricted directory and policy privileges",
    isSystem: true,
    isActive: true,
    userCount: 1,
    permissions: [
      "user:create", "user:read", "user:update", "user:delete", "user:assign",
      "role:create", "role:read", "role:update", "role:delete", "role:assign",
      "permission:create", "permission:read", "permission:update", "permission:delete",
      "client:create", "client:read", "client:update", "client:delete",
      "audit:read", "config:read", "config:update", "session:read", "session:terminate"
    ]
  },
  {
    uuid: "role-02",
    name: "USER",
    description: "Standard end user entitled to self-service profile and resource access",
    isSystem: true,
    isActive: true,
    userCount: 3,
    permissions: ["user:read", "profile:read", "profile:update", "session:read"]
  },
  {
    uuid: "role-03",
    name: "DEVELOPER",
    description: "Engineering staff with OAuth2 client management & API inspection access",
    isSystem: false,
    isActive: true,
    userCount: 1,
    permissions: ["client:create", "client:read", "client:update", "user:read", "permission:read"]
  },
  {
    uuid: "role-04",
    name: "MANAGER",
    description: "Operations supervisor entitled to view directory and manage user states",
    isSystem: false,
    isActive: true,
    userCount: 1,
    permissions: ["user:read", "user:update", "role:read", "session:read"]
  },
  {
    uuid: "role-05",
    name: "AUDITOR",
    description: "Compliance officer with read-only access to audit logs and security trails",
    isSystem: false,
    isActive: false,
    userCount: 1,
    permissions: ["audit:read", "user:read", "role:read", "config:read"]
  }
];

export const initialPermissions = [
  { uuid: "p-01", name: "user:create", resource: "user", action: "create", description: "Register and provision new user accounts", isActive: true },
  { uuid: "p-02", name: "user:read", resource: "user", action: "read", description: "View user directory and profiles", isActive: true },
  { uuid: "p-03", name: "user:update", resource: "user", action: "update", description: "Modify user accounts and status flags", isActive: true },
  { uuid: "p-04", name: "user:delete", resource: "user", action: "delete", description: "Permanently delete user records", isActive: true },
  { uuid: "p-05", name: "user:assign", resource: "user", action: "assign", description: "Assign roles and permissions to users", isActive: true },

  { uuid: "p-06", name: "role:create", resource: "role", action: "create", description: "Create RBAC roles", isActive: true },
  { uuid: "p-07", name: "role:read", resource: "role", action: "read", description: "Inspect defined roles and hierarchies", isActive: true },
  { uuid: "p-08", name: "role:update", resource: "role", action: "update", description: "Modify role scopes and permission links", isActive: true },
  { uuid: "p-09", name: "role:delete", resource: "role", action: "delete", description: "Remove roles from directory", isActive: true },
  { uuid: "p-10", name: "role:assign", resource: "role", action: "assign", description: "Grant permissions to role bundles", isActive: true },

  { uuid: "p-11", name: "permission:create", resource: "permission", action: "create", description: "Define new system permissions", isActive: true },
  { uuid: "p-12", name: "permission:read", resource: "permission", action: "read", description: "Browse system permissions catalog", isActive: true },
  { uuid: "p-13", name: "permission:update", resource: "permission", action: "update", description: "Modify permission properties", isActive: true },
  { uuid: "p-14", name: "permission:delete", resource: "permission", action: "delete", description: "Remove system permissions", isActive: true },

  { uuid: "p-15", name: "client:create", resource: "client", action: "create", description: "Register new OAuth2/OIDC clients", isActive: true },
  { uuid: "p-16", name: "client:read", resource: "client", action: "read", description: "View registered OAuth2 applications", isActive: true },
  { uuid: "p-17", name: "client:update", resource: "client", action: "update", description: "Modify client secrets, URIs and scopes", isActive: true },
  { uuid: "p-18", name: "client:delete", resource: "client", action: "delete", description: "Deregister OAuth2 applications", isActive: true },

  { uuid: "p-19", name: "audit:read", resource: "audit", action: "read", description: "Query and filter security audit logs", isActive: true },
  { uuid: "p-20", name: "config:read", resource: "config", action: "read", description: "Inspect IAM authentication and token policies", isActive: true },
  { uuid: "p-21", name: "config:update", resource: "config", action: "update", description: "Configure system security parameters", isActive: true },
  { uuid: "p-22", name: "session:read", resource: "session", action: "read", description: "View active user sessions across the cluster", isActive: true },
  { uuid: "p-23", name: "session:terminate", resource: "session", action: "terminate", description: "Forcibly terminate user login sessions", isActive: true }
];

export const initialClients = [
  {
    id: "client-01",
    clientId: "front-bff-client",
    clientName: "Spring Cloud Gateway BFF",
    clientSecret: "secret",
    authorizationGrantTypes: ["authorization_code", "refresh_token", "client_credentials"],
    redirectUris: ["http://localhost:9999/login/oauth2/code/front-bff-client", "http://localhost:3000/callback"],
    postLogoutRedirectUris: ["http://localhost:9999/login?logout"],
    scopes: ["openid", "profile", "email"],
    clientAuthenticationMethods: ["client_secret_basic", "client_secret_post"],
    createdAt: "2026-01-01T00:00:00Z",
    isActive: true
  },
  {
    id: "client-02",
    clientId: "dcr-admin-client",
    clientName: "Dynamic Client Registration Admin",
    clientSecret: "dcr-secret",
    authorizationGrantTypes: ["client_credentials"],
    redirectUris: [],
    postLogoutRedirectUris: [],
    scopes: ["client.create", "client.read"],
    clientAuthenticationMethods: ["client_secret_basic"],
    createdAt: "2026-01-01T00:00:00Z",
    isActive: true
  },
  {
    id: "client-03",
    clientId: "mobile-banking-app",
    clientName: "ISTAD Mobile App (Flutter)",
    clientSecret: "mob-secret-994821",
    authorizationGrantTypes: ["authorization_code", "refresh_token"],
    redirectUris: ["kh.edu.istad.mobile://oauth2callback"],
    postLogoutRedirectUris: ["kh.edu.istad.mobile://logout"],
    scopes: ["openid", "profile", "email", "offline_access"],
    clientAuthenticationMethods: ["none"], // PKCE Public client
    createdAt: "2026-02-10T11:00:00Z",
    isActive: true
  },
  {
    id: "client-04",
    clientId: "partner-analytics-portal",
    clientName: "Partner Reporting & Analytics",
    clientSecret: "partner-sec-883192",
    authorizationGrantTypes: ["authorization_code", "client_credentials"],
    redirectUris: ["https://analytics.partner-istad.com/auth/callback"],
    postLogoutRedirectUris: ["https://analytics.partner-istad.com/logged-out"],
    scopes: ["openid", "profile", "audit:read"],
    clientAuthenticationMethods: ["client_secret_basic"],
    createdAt: "2026-03-05T15:20:00Z",
    isActive: true
  }
];

export const initialAuditLogs = [
  {
    id: "aud-01",
    timestamp: new Date(Date.now() - 1000 * 60 * 8).toISOString(),
    actor: "houngmingyeak (Admin)",
    event: "ROLE_ASSIGNED",
    target: "john_doe (User)",
    ipAddress: "116.212.155.82",
    status: "SUCCESS",
    details: "Granted role 'MANAGER' to user john_doe with reason: 'Promotion to team lead'"
  },
  {
    id: "aud-02",
    timestamp: new Date(Date.now() - 1000 * 60 * 25).toISOString(),
    actor: "houngmingyeak (Admin)",
    event: "USER_LOCKED",
    target: "dararith_ops (User)",
    ipAddress: "116.212.155.82",
    status: "SUCCESS",
    details: "Account locked after 5 consecutive failed login attempts detected"
  },
  {
    id: "aud-03",
    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 1).toISOString(),
    actor: "system_auth_service",
    event: "TOKEN_ISSUED",
    target: "front-bff-client (Client)",
    ipAddress: "127.0.0.1",
    status: "SUCCESS",
    details: "Issued Authorization Code token bundle with scopes: openid profile email"
  },
  {
    id: "aud-04",
    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 3).toISOString(),
    actor: "houngmingyeak (Admin)",
    event: "CLIENT_REGISTERED",
    target: "partner-analytics-portal",
    ipAddress: "116.212.155.82",
    status: "SUCCESS",
    details: "Registered OAuth2 Client ID 'partner-analytics-portal' with redirect URIs"
  },
  {
    id: "aud-05",
    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 6).toISOString(),
    actor: "john_doe (User)",
    event: "PASSWORD_CHANGED",
    target: "john_doe (User)",
    ipAddress: "103.243.140.21",
    status: "SUCCESS",
    details: "Self-service password update completed"
  },
  {
    id: "aud-06",
    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 24).toISOString(),
    actor: "dararith_ops",
    event: "AUTHENTICATION_FAILED",
    target: "dararith_ops (User)",
    ipAddress: "185.220.101.5",
    status: "FAILURE",
    details: "Bad credentials entered. Attempt count: 5"
  }
];

export const initialSystemConfig = {
  // Authentication policies
  maxFailedLoginAttempts: 5,
  lockoutDurationMinutes: 30,
  require2faForAdmins: true,
  allowPublicRegistration: true,

  // Password policies
  minPasswordLength: 8,
  requireUppercase: true,
  requireLowercase: true,
  requireNumbers: true,
  requireSpecialChars: true,
  passwordExpirationDays: 90,
  preventPasswordReuseCount: 3,

  // Session & Token policies
  sessionTimeoutMinutes: 60,
  maxConcurrentSessionsPerUser: 5,
  accessTokenTtlSeconds: 3600, // 1 hour
  refreshTokenTtlHours: 720,   // 30 days
  authorizationCodeTtlSeconds: 300
};

// Data persistence service
class DataStore {
  constructor() {
    this.init();
  }

  init() {
    // Purge old mock users so only real database users are used
    const existingUsers = localStorage.getItem('iam_users');
    if (!existingUsers || existingUsers.includes('houngmingyeak') || existingUsers.includes('john_doe')) {
      localStorage.setItem('iam_users', JSON.stringify([]));
    }
    const existingCurrent = localStorage.getItem('iam_current_user');
    if (existingCurrent && (existingCurrent.includes('houngmingyeak') || existingCurrent.includes('john_doe'))) {
      localStorage.removeItem('iam_current_user');
    }
    if (!localStorage.getItem('iam_roles')) {
      localStorage.setItem('iam_roles', JSON.stringify(initialRoles));
    }
    if (!localStorage.getItem('iam_permissions')) {
      localStorage.setItem('iam_permissions', JSON.stringify(initialPermissions));
    }
    if (!localStorage.getItem('iam_clients')) {
      localStorage.setItem('iam_clients', JSON.stringify(initialClients));
    }
    if (!localStorage.getItem('iam_active_sessions')) {
      localStorage.setItem('iam_active_sessions', JSON.stringify(initialActiveSessions));
    }
    if (!localStorage.getItem('iam_login_history')) {
      localStorage.setItem('iam_login_history', JSON.stringify(initialLoginHistory));
    }
    if (!localStorage.getItem('iam_linked_accounts')) {
      localStorage.setItem('iam_linked_accounts', JSON.stringify(initialLinkedAccounts));
    }
    if (!localStorage.getItem('iam_account_activities')) {
      localStorage.setItem('iam_account_activities', JSON.stringify(initialAccountActivities));
    }
    if (!localStorage.getItem('iam_audit_logs')) {
      localStorage.setItem('iam_audit_logs', JSON.stringify(initialAuditLogs));
    }
    if (!localStorage.getItem('iam_system_config')) {
      localStorage.setItem('iam_system_config', JSON.stringify(initialSystemConfig));
    }
  }

  get(key) {
    const raw = localStorage.getItem(`iam_${key}`);
    return raw ? JSON.parse(raw) : null;
  }

  set(key, data) {
    localStorage.setItem(`iam_${key}`, JSON.stringify(data));
  }
}

export const store = new DataStore();
