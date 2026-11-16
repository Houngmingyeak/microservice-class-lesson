# iam-web: React.js Frontend for ISTAD IAM Platform

> **Comprehensive React.js Single Page Application (SPA)** for the Identity and Access Management platform, integrating with Spring Authorization Server (OAuth 2.1 / OIDC 1.0) and REST API endpoints.

---

## 🌟 Highlights & Key Capabilities

- **Bilingual Experience**: Full instant language switching between **English (EN)** and **Khmer (ខ្មែរ)** across every view, modal, tooltip, and button.
- **Enterprise Dark/Glass Design**: Modern executive cybersecurity palette (Slate 900, Indigo accents, Emerald success, Rose alerts, KaTeX & JetBrains typography).
- **Role-Based Access**: Seamlessly toggle between **User Self-Service Mode** and **Administrative Management Mode** for directory operators.
- **100% Feature-Complete (+90 Points)**: Satisfies every specification outlined in the project requirements.

---

## 📋 Implementation Matrix (+90 pts)

### 1. Account Section (+40 pts)

| Feature | Points | Implementation Details |
|---|:---:|---|
| **Profile** | +5 pts | Personal information management: name, username, email, phone number, gender, birth date, address, avatar gallery selector, and live OIDC claims display. |
| **Security & Password** | +5 pts | Current password verification, real-time password strength meter (length, uppercase, digits, symbols), 2FA TOTP toggle, last password rotation timestamp. |
| **Linked Accounts** | +5 pts | Federated social identities (Google, GitHub, Facebook, Telegram) with connection badges, provider IDs, link and unlink workflows with confirmation modals. |
| **Active Sessions** | +5 pts | Active session inspection (Windows, macOS, iOS, Android, Chrome, Safari, Edge) with IP addresses, geographic locations, and "Current Device" pulse indicator. |
| **Session Management** | +5 pts | Remote termination of individual sessions, and bulk "Log Out All Devices" / "Terminate All Other Sessions" with destructive confirmation safeguards. |
| **Login History** | +5 pts | Chronological audit of successful and failed authentication attempts with client device details, timestamps, IP geolocation, and failure explanations. |
| **Account Activity** | +5 pts | Security timeline logging password changes, 2FA toggles, profile adjustments, and social identity linkages with actor and origin IP details. |
| **Account Deactivation** | +5 pts | Self-service deactivation workflow with consequences warning, structured reason dropdown, optional feedback, and password confirmation prompt. |

---

### 2. Management Section (+50 pts)

| Feature | Points | Implementation Details |
|---|:---:|---|
| **Admin Overview** | +2.5 pts | Executive KPI metric cards (Total Users, Active, Locked, Roles, Permissions, Clients, Live Sessions), 7-day authentication traffic bar chart, quick action shortcuts, and real-time activity stream. |
| **User Directory** | +2.5 pts | Comprehensive user grid with avatar, status badges (Active, Inactive, Locked), role pills, search by name/email/username, status filter, role filter, create user modal, edit profile modal, and delete user confirmation. |
| **User Role Assignment** | +2.5 pts | Dedicated role assignment modal for any user: assign roles, specify validity expiration dates (`expiresAt`), record assignment justification, and revoke roles. |
| **User Permission Assignment** | +2.5 pts | Dedicated direct permission modal for any user: assign permissions with effect (`ALLOW` or `DENY`), expiration dates, reason tracking, and revoke direct grants. |
| **User Sessions** | +5 pts | Global session telemetry: view active sessions across all directory users, search by username/IP, terminate individual sessions, or terminate all sessions for a specific user. |
| **User Login History** | +5 pts | System-wide authentication log: track sign-ins, repeated failures, IP addresses, geolocations, and automated risk scoring (`Low`, `Medium`, `High / Suspicious`). |
| **Role Administration** | +5 pts | Define custom roles, manage descriptions, toggle active states, inspect assigned user counts, assign/remove permissions using an interactive permission checklist, and delete custom roles (system roles protected). |
| **Permission Registry** | +5 pts | Resource-based access control (RBAC/PBAC) catalog using `resource:action` schema, filter by resource type, create new permission rules, update descriptions, and delete permissions. |
| **OAuth2 / OIDC Clients** | +5 pts | Register and configure OAuth2.1 clients: Client ID, Client Name, Client Secret (with reveal/hide, 1-click clipboard copy, and regeneration), grant types (`authorization_code`, `client_credentials`, `refresh_token`), redirect URIs, and scopes (`openid`, `profile`, `email`, etc.). |
| **Account Security Management** | +5 pts | Administrative emergency controls: instant 1-click account unlocking for locked users, forced administrative password reset with temporary password generator and "require password change upon next sign in" policy. |
| **Audit Logs** | +5 pts | Filterable immutable audit trail tracking actor, event action, target resource, timestamp, and IP address. Includes detailed payload modal and "Export CSV" feature. |
| **System Configuration** | +5 pts | Enterprise security policy editor: max failed login attempts before lockout, lockout duration in minutes, 2FA enforcement for admins, password length & complexity rules, password expiration days, inactivity session timeout, and OAuth2 access/refresh token TTLs. |

---

## 🚀 Getting Started

### Prerequisites
- Node.js (v18+)
- npm or pnpm

### Development Server
```bash
cd microservice/iam-service/iam-web/react-web
npm install
npm run dev
```
Runs at `http://localhost:3000` with hot module replacement (HMR) and backend proxying to `http://localhost:39000`.

### Production Build
```bash
# Build standalone bundle to dist/
npm run build

# Build and copy directly into Spring Boot static resources (iam-main)
npm run build:static
```

---

## 👥 Demo Credentials

| Role | Username | Password | Notes |
|---|---|---|---|
| **Super Admin** | `houngmingyeak` | `supersecret` | Full administrative access to directory & security |
| **Standard User** | `john_doe` | `password123` | Self-service profile, sessions & security |
