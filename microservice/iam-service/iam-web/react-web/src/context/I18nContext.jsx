import React, { createContext, useContext, useState, useEffect } from 'react';

const translations = {
  en: {
    // General
    appName: "ISTAD IAM Platform",
    // tagline: "Enterprise Identity & Access Management",
    welcome: "Welcome back",
    search: "Search...",
    filter: "Filter",
    all: "All",
    status: "Status",
    actions: "Actions",
    save: "Save Changes",
    cancel: "Cancel",
    confirm: "Confirm",
    delete: "Delete",
    edit: "Edit",
    create: "Create",
    add: "Add New",
    refresh: "Refresh",
    close: "Close",
    loading: "Loading...",
    noData: "No data available",
    active: "Active",
    inactive: "Inactive",
    locked: "Locked",
    disabled: "Disabled",
    enabled: "Enabled",
    success: "Success",
    failed: "Failed",
    suspicious: "Suspicious",
    connected: "Connected",
    notConnected: "Not Connected",
    currentDevice: "Current Device",
    viewDetails: "View Details",
    revoke: "Revoke",
    assign: "Assign",
    terminate: "Terminate",
    unlock: "Unlock",
    lock: "Lock",
    activate: "Activate",
    deactivate: "Deactivate",
    reset: "Reset",

    // Navigation & Tabs
    navAccount: "Account",
    navManagement: "Administration",
    tabProfile: "Profile",
    tabSecurity: "Security & Password",
    tabLinkedAccounts: "Linked Accounts",
    tabActiveSessions: "Active Sessions",
    tabLoginHistory: "Login History",
    tabAccountActivity: "Account Activity",
    tabDeactivation: "Deactivate Account",

    tabOverview: "Overview",
    tabUsers: "User Management",
    tabUserRoles: "User Role Assignment",
    tabUserPermissions: "User Permission Assignment",
    tabUserSessions: "User Sessions",
    tabUserLoginHistory: "User Login History",
    tabRoles: "Role Management",
    tabPermissions: "Permission Management",
    tabClients: "OAuth2 Clients",
    tabSecurityMgmt: "Security Management",
    tabAuditLogs: "Audit Logs",
    tabSystemConfig: "System Configuration",

    // Profile View
    profileTitle: "Personal Information",
    profileSubtitle: "Manage your personal details, identity, and contact information.",
    personalDetails: "Personal Details",
    username: "Username",
    email: "Email Address",
    phoneNumber: "Phone Number",
    givenName: "Given Name",
    familyName: "Family Name",
    nickname: "Nickname / Display Name",
    gender: "Gender",
    birthdate: "Birth Date",
    address: "Street Address",
    locality: "City / Locality",
    country: "Country",
    preferredLocale: "Preferred Language",
    avatar: "Profile Avatar",
    changeAvatar: "Change Avatar",
    profileUpdated: "Profile updated successfully!",

    // Security & Password View
    securityTitle: "Security & Password",
    securitySubtitle: "Manage your authentication credentials, password, and multi-factor security.",
    changePassword: "Change Password",
    currentPassword: "Current Password",
    newPassword: "New Password",
    confirmPassword: "Confirm Password",
    confirmNewPassword: "Confirm New Password",
    passwordStrength: "Password Strength",
    weak: "Weak",
    medium: "Medium",
    strong: "Strong",
    passReqLength: "At least 8 characters",
    passReqUpper: "Includes uppercase letter",
    passReqNumber: "Includes number",
    passReqSpecial: "Includes special character",
    mfaHeading: "Two-Factor Authentication (2FA)",
    mfaDesc: "Add an extra layer of security to your account with authenticator apps.",
    mfaEnabled: "2FA is currently active on your account.",
    mfaDisabled: "2FA is currently disabled. We strongly recommend enabling it.",
    enableMfa: "Enable 2FA",
    disableMfa: "Disable 2FA",
    passwordChangedSuccess: "Password changed successfully!",

    // Linked Accounts View
    linkedAccountsTitle: "Linked Social & OAuth2 Accounts",
    linkedAccountsSubtitle: "Connect your social accounts for fast and secure one-click sign in.",
    linkAccount: "Link Account",
    unlinkAccount: "Unlink",
    unlinkConfirm: "Are you sure you want to unlink this account?",
    unlinkSuccess: "Account unlinked successfully.",
    linkSuccess: "Account linked successfully.",

    // Active Sessions View
    activeSessionsTitle: "Active Devices & Sessions",
    activeSessionsSubtitle: "Review devices that are currently logged into your account and terminate unrecognized ones.",
    device: "Device / OS",
    browser: "Browser",
    ipAddress: "IP Address",
    location: "Location",
    lastActive: "Last Active",
    terminateSession: "Terminate Session",
    terminateAllSessions: "Log Out All Devices",
    terminateAllConfirm: "Are you sure you want to log out all other active sessions?",
    sessionTerminated: "Session terminated successfully.",

    // Login History View
    loginHistoryTitle: "Login Activity History",
    loginHistorySubtitle: "Track recent successful and failed sign-in attempts for your account.",
    time: "Timestamp",
    attemptStatus: "Status",
    failureReason: "Failure Reason",

    // Account Activity View
    activityTitle: "Security & Account Activity",
    activitySubtitle: "Chronological log of changes made to your profile, credentials, and settings.",
    activityType: "Activity Type",
    details: "Details",

    // Deactivation View
    deactivationTitle: "Request Account Deactivation",
    deactivationSubtitle: "Temporarily pause or request permanent deactivation of your IAM identity.",
    deactivationWarning: "Warning: Deactivating your account will instantly terminate all active sessions, invalidate active OAuth2 tokens, and restrict access to all connected corporate applications.",
    reasonLabel: "Reason for Deactivation",
    reasonPlaceholder: "Please select a reason...",
    reasonBreak: "Taking a temporary break",
    reasonPrivacy: "Privacy or data security concerns",
    reasonDuplicate: "I have another account",
    reasonOther: "Other reason",
    additionalNotes: "Additional feedback (Optional)",
    confirmDeactivationText: "To confirm, enter your current password:",
    submitDeactivation: "Request Deactivation",
    deactivationSubmitted: "Deactivation request submitted. An email confirmation has been dispatched.",

    // Administration - Overview
    overviewTitle: "Identity Platform Overview",
    overviewSubtitle: "Real-time key performance metrics, directory health, and security telemetry.",
    metricTotalUsers: "Total Users",
    metricActiveUsers: "Active Users",
    metricTotalRoles: "Configured Roles",
    metricTotalPerms: "Permissions",
    metricClients: "OAuth2 Clients",
    metricActiveSessions: "Live Sessions",
    recentSystemActivity: "Recent System Activity",
    quickActions: "Quick Actions",
    loginTraffic: "Daily Authentication Traffic",

    // User Management
    userMgmtTitle: "User Directory Management",
    userMgmtSubtitle: "Provision, manage, inspect, lock, unlock, and assign privileges to directory users.",
    createUser: "Create New User",
    editUser: "Edit User",
    userRoles: "Roles",
    userPermissions: "Permissions",
    lockUser: "Lock Account",
    unlockUser: "Unlock Account",
    activateUser: "Activate",
    deactivateUser: "Deactivate",
    deleteUser: "Delete User",
    assignRoles: "Assign Roles",
    grantPermissions: "Grant Permissions",
    userCreated: "User created successfully!",
    userUpdated: "User updated successfully!",
    userDeleted: "User deleted successfully!",

    // Role & Permission Assignment Modals
    roleAssignmentTitle: "Assign & Manage User Roles",
    permAssignmentTitle: "Direct User Permission Grants",
    expiresAt: "Expires At (Optional)",
    assignmentReason: "Reason / Justification",
    effect: "Effect",
    allow: "ALLOW",
    deny: "DENY",

    // User Sessions (Admin)
    adminSessionsTitle: "Global Active User Sessions",
    adminSessionsSubtitle: "Inspect and remotely terminate concurrent sessions across all enterprise users.",
    terminateAllUserSessions: "Terminate All Sessions for User",

    // User Login History (Admin)
    adminLoginHistoryTitle: "Global Login Telemetry & Audit",
    adminLoginHistorySubtitle: "Monitor sign-in traffic, failed authentications, and brute-force anomalies.",
    riskLevel: "Risk Level",
    riskLow: "Low",
    riskMedium: "Medium",
    riskHigh: "High / Suspicious",

    // Role Management
    rolesTitle: "Role Administration",
    rolesSubtitle: "Define RBAC roles, assign granular permissions, and control authorization hierarchies.",
    createRole: "Create Role",
    roleName: "Role Identifier",
    roleDesc: "Description",
    assignedPerms: "Assigned Permissions",
    systemRole: "System Role",

    // Permission Management
    permTitle: "Permission & Scope Registry",
    permSubtitle: "Register resource-based access control rules (Resource:Action schema).",
    createPermission: "Create Permission",
    resource: "Resource",
    action: "Action",
    permName: "Permission Name",

    // OAuth2 Client Management
    clientsTitle: "OAuth2 / OIDC Applications",
    clientsSubtitle: "Register and configure OAuth2.1 clients, redirect URIs, scopes, and grant flows.",
    registerClient: "Register OAuth2 Client",
    clientId: "Client ID",
    clientName: "Application Name",
    clientSecret: "Client Secret",
    grantTypes: "Grant Types",
    redirectUris: "Redirect URIs (comma-separated)",
    scopes: "Allowed Scopes",
    copySecret: "Copy Secret",
    regenerateSecret: "Regenerate Secret",

    // Security Management (Admin)
    secMgmtTitle: "Account Security & Credentials",
    secMgmtSubtitle: "Administrative credential overrides, account unlocking, and security incident response.",
    resetUserPassword: "Reset User Password",
    temporaryPassword: "New Temporary Password",
    forceChangeOnNextLogin: "Require password change upon next sign in",
    unlockAccountNow: "Instant Unlock Account",

    // Audit Logs
    auditTitle: "Enterprise Audit Trail",
    auditSubtitle: "Immutable system activity log recording identity changes, role grants, and authentications.",
    actor: "Actor",
    event: "Event Action",
    target: "Target Resource",
    exportLogs: "Export CSV",

    // System Configuration
    sysConfigTitle: "System Security Policies",
    sysConfigSubtitle: "Configure global password rules, lockout thresholds, session timeouts, and token lifetimes.",
    authPolicy: "Authentication Policies",
    maxFailedAttempts: "Max Failed Login Attempts Before Lockout",
    lockoutDuration: "Account Lockout Duration (Minutes)",
    require2faAdmin: "Enforce 2FA for Administrators",
    passPolicy: "Password Complexity Policy",
    minPassLength: "Minimum Password Length",
    passExpirationDays: "Password Validity / Expiry (Days)",
    sessionPolicy: "Session & Token Settings",
    sessionTimeout: "Inactivity Session Timeout (Minutes)",
    accessTokenTtl: "OAuth2 Access Token TTL (Seconds)",
    refreshTokenTtl: "OAuth2 Refresh Token TTL (Hours)",
    configSaved: "System configuration saved successfully!",
    resetDefaults: "Reset to System Defaults",

    // Auth Pages
    registerTitle: "Sign Up",
    'register.title': "Sign Up",
    createAccount: "Create an Account",
    dontHaveAccount: "Don't have an account?",
    alreadyHaveAccount: "Already have an account?",
    signUp: "Sign Up",
    signIn: "Sign In",
    orContinueWith: "Or continue with social provider",
    quickDemoLogin: "Quick Login (Demo)",
    loginAsAdmin: "Login as Admin",
    loginAsUser: "Login as User",
  },

  km: {
    // General
    appName: "ថ្នាល ISTAD IAM",
    tagline: "ប្រព័ន្ធគ្រប់គ្រងអត្តសញ្ញាណ និងសិទ្ធិចូលប្រើប្រាស់កម្រិតសហគ្រាស",
    welcome: "សូមស្វាគមន៍មកវិញ",
    search: "ស្វែងរក...",
    filter: "ចម្រាញ់",
    all: "ទាំងអស់",
    status: "ស្ថានភាព",
    actions: "សកម្មភាព",
    save: "រក្សាទុកការផ្លាស់ប្តូរ",
    cancel: "បោះបង់",
    confirm: "បញ្ជាក់",
    delete: "លុប",
    edit: "កែប្រែ",
    create: "បង្កើតថ្មី",
    add: "បន្ថែមថ្មី",
    refresh: "ផ្ទុកឡើងវិញ",
    close: "បិទ",
    loading: "កំពុងផ្ទុក...",
    noData: "មិនមានទិន្នន័យទេ",
    active: "សកម្ម",
    inactive: "អសកម្ម",
    locked: "ត្រូវបានចាក់សោ",
    disabled: "ត្រូវបានបិទ",
    enabled: "ត្រូវបានបើក",
    success: "ជោគជ័យ",
    failed: "បរាជ័យ",
    suspicious: "គួរឱ្យសង្ស័យ",
    connected: "បានភ្ជាប់",
    notConnected: "មិនទាន់ភ្ជាប់",
    currentDevice: "ឧបករណ៍បច្ចុប្បន្ន",
    viewDetails: "មើលព័ត៌មានលម្អិត",
    revoke: "ដកហូត",
    assign: "ចាត់តាំង",
    terminate: "បញ្ចប់សម័យ",
    unlock: "ដោះសោ",
    lock: "ចាក់សោ",
    activate: "បើកដំណើរការ",
    deactivate: "បិទដំណើរការ",
    reset: "កំណត់ឡើងវិញ",

    // Navigation & Tabs
    navAccount: "គណនីផ្ទាល់ខ្លួន",
    navManagement: "ការគ្រប់គ្រងប្រព័ន្ធ",
    tabProfile: "ព័ត៌មានផ្ទាល់ខ្លួន",
    tabSecurity: "សុវត្ថិភាព & ពាក្យសម្ងាត់",
    tabLinkedAccounts: "គណនីដែលបានភ្ជាប់",
    tabActiveSessions: "សម័យសកម្ម",
    tabLoginHistory: "ប្រវត្តិការចូល",
    tabAccountActivity: "សកម្មភាពគណនី",
    tabDeactivation: "បិទដំណើរការគណនី",

    tabOverview: "ផ្ទាំងទូទៅ",
    tabUsers: "គ្រប់គ្រងអ្នកប្រើប្រាស់",
    tabUserRoles: "ការចាត់តាំងតួនាទី",
    tabUserPermissions: "ការចាត់តាំងសិទ្ធិ",
    tabUserSessions: "សម័យអ្នកប្រើប្រាស់",
    tabUserLoginHistory: "ប្រវត្តិការចូលរបស់អ្នកប្រើប្រាស់",
    tabRoles: "គ្រប់គ្រងតួនាទី",
    tabPermissions: "គ្រប់គ្រងសិទ្ធិ",
    tabClients: "អតិថិជន OAuth2",
    tabSecurityMgmt: "គ្រប់គ្រងសុវត្ថិភាព",
    tabAuditLogs: "កំណត់ហេតុសវនកម្ម",
    tabSystemConfig: "ការកំណត់ប្រព័ន្ធ",

    // Profile View
    profileTitle: "ព័ត៌មានផ្ទាល់ខ្លួន",
    profileSubtitle: "គ្រប់គ្រងព័ត៌មានផ្ទាល់ខ្លួន អត្តសញ្ញាណ និងព័ត៌មានទំនាក់ទំនងរបស់អ្នក។",
    personalDetails: "សេចក្តីលម្អិតអំពីបុគ្គល",
    username: "ឈ្មោះគណនី",
    email: "អាសយដ្ឋានអ៊ីមែល",
    phoneNumber: "លេខទូរស័ព្ទ",
    givenName: "នាមខ្លួន",
    familyName: "នាមត្រកូល",
    nickname: "ឈ្មោះហៅក្រៅ / ឈ្មោះបង្ហាញ",
    gender: "ភេទ",
    birthdate: "ថ្ងៃខែឆ្នាំកំណើត",
    address: "អាសយដ្ឋានផ្លូវ",
    locality: "ទីក្រុង / រាជធានី",
    country: "ប្រទេស",
    preferredLocale: "ភាសាពេញចិត្ត",
    avatar: "រូបភាពប្រវត្តិរូប",
    changeAvatar: "ផ្លាស់ប្តូររូបភាព",
    profileUpdated: "បានកែប្រែព័ត៌មានផ្ទាល់ខ្លួនដោយជោគជ័យ!",

    // Security & Password View
    securityTitle: "សុវត្ថិភាព និងពាក្យសម្ងាត់",
    securitySubtitle: "គ្រប់គ្រងពាក្យសម្ងាត់ ការផ្ទៀងផ្ទាត់ និងសុវត្ថិភាពច្រើនកម្រិត។",
    changePassword: "ផ្លាស់ប្តូរពាក្យសម្ងាត់",
    currentPassword: "ពាក្យសម្ងាត់បច្ចុប្បន្ន",
    newPassword: "ពាក្យសម្ងាត់ថ្មី",
    confirmPassword: "បញ្ជាក់ពាក្យសម្ងាត់",
    confirmNewPassword: "បញ្ជាក់ពាក្យសម្ងាត់ថ្មី",
    passwordStrength: "កម្រិតភាពរឹងមាំនៃពាក្យសម្ងាត់",
    weak: "ខ្សោយ",
    medium: "មធ្យម",
    strong: "រឹងមាំ",
    passReqLength: "យ៉ាងតិច ៨ តួអក្សរ",
    passReqUpper: "មានអក្សរធំ",
    passReqNumber: "មានលេខ",
    passReqSpecial: "មានសញ្ញាពិសេស",
    mfaHeading: "ការផ្ទៀងផ្ទាត់ពីរកត្តា (2FA)",
    mfaDesc: "បន្ថែមស្រទាប់សុវត្ថិភាពបន្ថែមលើគណនីរបស់អ្នកជាមួយកម្មវិធី Authenticator។",
    mfaEnabled: "2FA កំពុងដំណើរការលើគណនីរបស់អ្នក។",
    mfaDisabled: "2FA មិនទាន់បានបើកដំណើរការទេ។ យើងសូមណែនាំឱ្យបើកវា។",
    enableMfa: "បើកដំណើរការ 2FA",
    disableMfa: "បិទ 2FA",
    passwordChangedSuccess: "បានផ្លាស់ប្តូរពាក្យសម្ងាត់ដោយជោគជ័យ!",

    // Linked Accounts View
    linkedAccountsTitle: "គណនីសង្គម & OAuth2 ដែលបានភ្ជាប់",
    linkedAccountsSubtitle: "ភ្ជាប់គណនីបណ្តាញសង្គមរបស់អ្នកសម្រាប់ការចូលប្រើប្រាស់បានលឿន និងមានសុវត្ថិភាព។",
    linkAccount: "ភ្ជាប់គណនី",
    unlinkAccount: "ផ្តាច់ការភ្ជាប់",
    unlinkConfirm: "តើអ្នកប្រាកដថាចង់ផ្តាច់ការភ្ជាប់គណនីនេះមែនទេ?",
    unlinkSuccess: "បានផ្តាច់ការភ្ជាប់គណនីដោយជោគជ័យ។",
    linkSuccess: "បានភ្ជាប់គណនីដោយជោគជ័យ។",

    // Active Sessions View
    activeSessionsTitle: "ឧបករណ៍ និងសម័យសកម្ម",
    activeSessionsSubtitle: "ពិនិត្យមើលឧបករណ៍ដែលកំពុងចូលប្រើគណនីរបស់អ្នក ហើយបញ្ចប់សម័យដែលមិនស្គាល់។",
    device: "ឧបករណ៍ / ប្រព័ន្ធប្រតិបត្តិការ",
    browser: "កម្មវិធីរុករក (Browser)",
    ipAddress: "អាសយដ្ឋាន IP",
    location: "ទីតាំង",
    lastActive: "សកម្មភាពចុងក្រោយ",
    terminateSession: "បញ្ចប់សម័យនេះ",
    terminateAllSessions: "ចាកចេញពីគ្រប់ឧបករណ៍ទាំងអស់",
    terminateAllConfirm: "តើអ្នកប្រាកដថាចង់បញ្ចប់សម័យទាំងអស់នៅលើឧបករណ៍ផ្សេងទៀតមែនទេ?",
    sessionTerminated: "បានបញ្ចប់សម័យដោយជោគជ័យ។",

    // Login History View
    loginHistoryTitle: "ប្រវត្តិការចូលគណនី",
    loginHistorySubtitle: "តាមដានរាល់ការចូលគណនីជោគជ័យ និងបរាជ័យចុងក្រោយរបស់អ្នក។",
    time: "កាលបរិច្ឆេទ & ពេលវេលា",
    attemptStatus: "ស្ថានភាព",
    failureReason: "មូលហេតុបរាជ័យ",

    // Account Activity View
    activityTitle: "សកម្មភាពសុវត្ថិភាព និងគណនី",
    activitySubtitle: "កំណត់ត្រាតាមលំដាប់លំដោយនៃការផ្លាស់ប្តូរលើគណនី ពាក្យសម្ងាត់ និងការកំណត់របស់អ្នក។",
    activityType: "ប្រភេទសកម្មភាព",
    details: "ព័ត៌មានលម្អិត",

    // Deactivation View
    deactivationTitle: "ស្នើសុំបិទដំណើរការគណនី",
    deactivationSubtitle: "ផ្អាកបណ្តោះអាសន្ន ឬស្នើសុំបិទគណនីអត្តសញ្ញាណ IAM របស់អ្នកជាអចិន្ត្រៃយ៍។",
    deactivationWarning: "ការព្រមាន៖ ការបិទដំណើរការគណនីរបស់អ្នក នឹងបញ្ចប់រាល់សម័យសកម្មភ្លាមៗ មោឃភាពកូដ OAuth2 Tokens និងរារាំងសិទ្ធិចូលប្រើប្រាស់កម្មវិធីទាំងអស់របស់សហគ្រាស។",
    reasonLabel: "មូលហេតុនៃការបិទដំណើរការ",
    reasonPlaceholder: "សូមជ្រើសរើសមូលហេតុ...",
    reasonBreak: "ចង់ឈប់សម្រាកបណ្តោះអាសន្ន",
    reasonPrivacy: "កង្វល់អំពីឯកជនភាព ឬសុវត្ថិភាពទិន្នន័យ",
    reasonDuplicate: "ខ្ញុំមានគណនីមួយផ្សេងទៀត",
    reasonOther: "មូលហេតុផ្សេងទៀត",
    additionalNotes: "មតិយោបល់បន្ថែម (មិនបង្ខំ)",
    confirmDeactivationText: "ដើម្បីបញ្ជាក់ សូមបញ្ចូលពាក្យសម្ងាត់បច្ចុប្បន្នរបស់អ្នក៖",
    submitDeactivation: "ស្នើសុំបិទគណនី",
    deactivationSubmitted: "សំណើបិទដំណើរការត្រូវបានបញ្ជូន។ អ៊ីមែលបញ្ជាក់ត្រូវបានផ្ញើជូនរួចរាល់។",

    // Administration - Overview
    overviewTitle: "ទិដ្ឋភាពទូទៅនៃថ្នាលអត្តសញ្ញាណ",
    overviewSubtitle: "ទិន្នន័យគន្លឹះ សុខភាពប្រព័ន្ធ និងស្ថានភាពសុវត្ថិភាពតាមពេលវេលាជាក់ស្តែង។",
    metricTotalUsers: "អ្នកប្រើប្រាស់សរុប",
    metricActiveUsers: "អ្នកប្រើប្រាស់សកម្ម",
    metricTotalRoles: "តួនាទីដែលបានកំណត់",
    metricTotalPerms: "សិទ្ធិអនុញ្ញាត",
    metricClients: "អតិថិជន OAuth2",
    metricActiveSessions: "សម័យកំពុងដំណើរការ",
    recentSystemActivity: "សកម្មភាពប្រព័ន្ធចុងក្រោយ",
    quickActions: "សកម្មភាពរហ័ស",
    loginTraffic: "ចរាចរណ៍ការផ្ទៀងផ្ទាត់ប្រចាំថ្ងៃ",

    // User Management
    userMgmtTitle: "ការគ្រប់គ្រងបញ្ជីអ្នកប្រើប្រាស់",
    userMgmtSubtitle: "បង្កើត ពិនិត្យ ចាក់សោ ដោះសោ និងចាត់តាំងសិទ្ធិដល់អ្នកប្រើប្រាស់ក្នុងប្រព័ន្ធ។",
    createUser: "បង្កើតអ្នកប្រើប្រាស់ថ្មី",
    editUser: "កែប្រែអ្នកប្រើប្រាស់",
    userRoles: "តួនាទី",
    userPermissions: "សិទ្ធិអនុញ្ញាត",
    lockUser: "ចាក់សោគណនី",
    unlockUser: "ដោះសោគណនី",
    activateUser: "បើកដំណើរការ",
    deactivateUser: "បិទដំណើរការ",
    deleteUser: "លុបអ្នកប្រើប្រាស់",
    assignRoles: "ចាត់តាំងតួនាទី",
    grantPermissions: "ផ្តល់សិទ្ធិផ្ទាល់",
    userCreated: "បានបង្កើតអ្នកប្រើប្រាស់ដោយជោគជ័យ!",
    userUpdated: "បានកែប្រែព័ត៌មានអ្នកប្រើប្រាស់ដោយជោគជ័យ!",
    userDeleted: "បានលុបអ្នកប្រើប្រាស់ដោយជោគជ័យ!",

    // Role & Permission Assignment Modals
    roleAssignmentTitle: "ចាត់តាំង & គ្រប់គ្រងតួនាទីរបស់អ្នកប្រើប្រាស់",
    permAssignmentTitle: "ការផ្តល់សិទ្ធិផ្ទាល់ដល់អ្នកប្រើប្រាស់",
    expiresAt: "ផុតកំណត់នៅ (ជម្រើស)",
    assignmentReason: "មូលហេតុ / ហេតុផល",
    effect: "ឥទ្ធិពល",
    allow: "អនុញ្ញាត (ALLOW)",
    deny: "បដិសេធ (DENY)",

    // User Sessions (Admin)
    adminSessionsTitle: "សម័យសកម្មរបស់អ្នកប្រើប្រាស់ទាំងអស់",
    adminSessionsSubtitle: "ពិនិត្យ និងបញ្ចប់សម័យដំណើរការពីចម្ងាយរបស់អ្នកប្រើប្រាស់ក្នុងសហគ្រាស។",
    terminateAllUserSessions: "បញ្ចប់គ្រប់សម័យទាំងអស់របស់អ្នកប្រើប្រាស់នេះ",

    // User Login History (Admin)
    adminLoginHistoryTitle: "ប្រវត្តិការចូលប្រព័ន្ធសរុប & សវនកម្ម",
    adminLoginHistorySubtitle: "ត្រួតពិនិត្យចរាចរណ៍ការចូល ការចូលបរាជ័យ និងសកម្មភាពវាយប្រហារបន្លំ។",
    riskLevel: "កម្រិតហានិភ័យ",
    riskLow: "ទាប",
    riskMedium: "មធ្យម",
    riskHigh: "ខ្ពស់ / គួរឱ្យសង្ស័យ",

    // Role Management
    rolesTitle: "ការគ្រប់គ្រងតួនាទី (Roles)",
    rolesSubtitle: "កំណត់តួនាទី RBAC ចាត់ចែងសិទ្ធិអនុញ្ញាត និងរៀបចំឋានានុក្រមសិទ្ធិ។",
    createRole: "បង្កើតតួនាទីថ្មី",
    roleName: "ឈ្មោះសម្គាល់តួនាទី",
    roleDesc: "ការពិពណ៌នា",
    assignedPerms: "សិទ្ធិដែលបានចាត់តាំង",
    systemRole: "តួនាទីប្រព័ន្ធ",

    // Permission Management
    permTitle: "បញ្ជីសិទ្ធិអនុញ្ញាត & Scopes",
    permSubtitle: "ចុះបញ្ជីវិធានត្រួតពិនិត្យការចូលប្រើប្រាស់ធនធាន (ទម្រង់ Resource:Action)។",
    createPermission: "បង្កើតសិទ្ធិថ្មី",
    resource: "ធនធាន (Resource)",
    action: "សកម្មភាព (Action)",
    permName: "ឈ្មោះសិទ្ធិ",

    // OAuth2 Client Management
    clientsTitle: "កម្មវិធីអតិថិជន OAuth2 / OIDC",
    clientsSubtitle: "ចុះឈ្មោះ និងកំណត់រចនាសម្ព័ន្ធអតិថិជន OAuth2.1, Redirect URIs, Scopes និង Grant Types។",
    registerClient: "ចុះឈ្មោះអតិថិជន OAuth2",
    clientId: "Client ID",
    clientName: "ឈ្មោះកម្មវិធី",
    clientSecret: "Client Secret",
    grantTypes: "Grant Types",
    redirectUris: "Redirect URIs (បំបែកដោយក្បៀស)",
    scopes: "Scopes ដែលអនុញ្ញាត",
    copySecret: "ចម្លង Secret",
    regenerateSecret: "បង្កើត Secret ថ្មី",

    // Security Management (Admin)
    secMgmtTitle: "សុវត្ថិភាពគណនី & អត្តសញ្ញាណ",
    secMgmtSubtitle: "ការកំណត់ពាក្យសម្ងាត់ឡើងវិញដោយអ្នកគ្រប់គ្រង ការដោះសោគណនី និងការឆ្លើយតបសុវត្ថិភាព។",
    resetUserPassword: "កំណត់ពាក្យសម្ងាត់អ្នកប្រើប្រាស់ឡើងវិញ",
    temporaryPassword: "ពាក្យសម្ងាត់បណ្តោះអាសន្នថ្មី",
    forceChangeOnNextLogin: "តម្រូវឱ្យប្តូរពាក្យសម្ងាត់នៅពេលចូលបន្ទាប់",
    unlockAccountNow: "ដោះសោគណនីភ្លាមៗ",

    // Audit Logs
    auditTitle: "កំណត់ហេតុសវនកម្មសហគ្រាស",
    auditSubtitle: "កំណត់ត្រាសកម្មភាពប្រព័ន្ធដែលមិនអាចកែប្រែបាន រួមមានការកែប្រែអត្តសញ្ញាណ ការផ្តល់តួនាទី និងការចូលប្រព័ន្ធ។",
    actor: "អ្នកអនុវត្ត (Actor)",
    event: "សកម្មភាព (Event)",
    target: "ធនធានគោលដៅ",
    exportLogs: "ទាញយក CSV",

    // System Configuration
    sysConfigTitle: "គោលនយោបាយសុវត្ថិភាពប្រព័ន្ធ",
    sysConfigSubtitle: "កំណត់វិធានពាក្យសម្ងាត់ ចំនួនលើកចាក់សោ រយៈពេលផុតកំណត់សម័យ និងអាយុកាល Token។",
    authPolicy: "គោលនយោបាយការផ្ទៀងផ្ទាត់",
    maxFailedAttempts: "ចំនួនលើកចូលខុសអតិបរមាមុនពេលចាក់សោ",
    lockoutDuration: "រយៈពេលចាក់សោគណនី (នាទី)",
    require2faAdmin: "តម្រូវឱ្យបើក 2FA សម្រាប់អ្នកគ្រប់គ្រង",
    passPolicy: "គោលនយោបាយភាពស្មុគស្មាញនៃពាក្យសម្ងាត់",
    minPassLength: "ប្រវែងពាក្យសម្ងាត់អប្បបរមា",
    passExpirationDays: "សុពលភាពពាក្យសម្ងាត់ / ផុតកំណត់ (ថ្ងៃ)",
    sessionPolicy: "ការកំណត់សម័យ និង Token",
    sessionTimeout: "រយៈពេលផុតកំណត់សម័យពេលអសកម្ម (នាទី)",
    accessTokenTtl: "អាយុកាល OAuth2 Access Token (វិនាទី)",
    refreshTokenTtl: "អាយុកាល OAuth2 Refresh Token (ម៉ោង)",
    configSaved: "បានរក្សាទុកការកំណត់ប្រព័ន្ធដោយជោគជ័យ!",
    resetDefaults: "កំណត់ទៅតម្លៃលំនាំដើម",

    // Auth Pages
    registerTitle: "ចុះឈ្មោះ",
    'register.title': "ចុះឈ្មោះ",
    createAccount: "បង្កើតគណនី",
    signInPrompt: "ចូលប្រើប្រាស់ជាមួយគណនីស្ថាប័នរបស់អ្នក",
    dontHaveAccount: "មិនទាន់មានគណនីមែនទេ?",
    alreadyHaveAccount: "មានគណនីរួចហើយ?",
    signUp: "ចុះឈ្មោះ",
    signIn: "ចូលគណនី",
    orContinueWith: "ឬបន្តជាមួយគណនីបណ្តាញសង្គម",
    quickDemoLogin: "ចូលរហ័ស (សាកល្បង)",
    loginAsAdmin: "ចូលជា Admin",
    loginAsUser: "ចូលជា User ធម្មតា",
  }
};

const I18nContext = createContext(null);

export const I18nProvider = ({ children }) => {
  const [locale, setLocale] = useState(() => {
    return localStorage.getItem('iam_locale') || 'en';
  });

  useEffect(() => {
    localStorage.setItem('iam_locale', locale);
    document.documentElement.lang = locale;
  }, [locale]);

  const t = (key, params = {}) => {
    let str = (translations[locale] && translations[locale][key]) || (translations['en'] && translations['en'][key]) || key;
    if (params && typeof params === 'object') {
      Object.keys(params).forEach(k => {
        str = str.replace(`{${k}}`, params[k]);
      });
    }
    return str;
  };

  return (
    <I18nContext.Provider value={{ locale, setLocale, t }}>
      {children}
    </I18nContext.Provider>
  );
};

export const useI18n = () => {
  const context = useContext(I18nContext);
  if (!context) {
    throw new Error('useI18n must be used within an I18nProvider');
  }
  return context;
};
