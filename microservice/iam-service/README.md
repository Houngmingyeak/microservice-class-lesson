# ISTAD IAM Service & Spring Authorization Server

Identity and Access Management (IAM) service built on **Spring Authorization Server (OAuth 2.1 / OpenID Connect 1.0)** and Clean Hexagonal/Ports & Adapters Multi-Module Architecture.

---

## 🏛️ Architecture Overview

The system strictly adheres to Clean Multi-Module Architecture with dependency inversion:

```
iam-service/
├── iam-domain/
│   ├── iam-domain-core/           # Plain Java aggregates (User, Role, Permission, Identity, assignments)
│   └── iam-application-service/   # Single-responsibility use cases, command/result DTOs, output ports
├── iam-persistence/               # Spring Data JPA entities, repositories, adapters, Liquibase migrations
├── iam-infrastructure/            # Spring Authorization Server, TokenCustomizer, DCR, UserDetailsService
├── iam-rest-api/                  # REST Controllers (/api/**), RFC 9457 ProblemDetail exception handler
├── iam-web/                       # JTE SSR templates (login, register, consent, profile), i18n (en/km)
└── iam-main/                      # Spring Boot bootstrap, configuration wiring, database initialization
```

### Dependency Rules:
- `iam-domain-core`: Zero framework dependencies (no Spring, no JPA, no Web).
- `iam-application-service`: Depends only on `iam-domain-core`. Contains 1 class per use case.
- `iam-persistence`, `iam-infrastructure`, `iam-rest-api`, `iam-web`: Adapters depending on `iam-application-service`.
- `iam-main`: Composes all modules.

---

## 🔐 Core Capabilities

### 1. Spring Authorization Server & OIDC 1.0
- **OAuth 2.1 Endpoints**:
  - Authorization Endpoint: `/oauth2/authorize`
  - Token Endpoint: `/oauth2/token` (Authorization Code with PKCE, Refresh Token, Client Credentials)
  - JWKS Endpoint: `/oauth2/jwks`
  - OIDC Discovery: `/.well-known/openid-configuration`
- **Dynamic Client Registration (DCR)**:
  - Registered at `/connect/register` using Initial Access Token (scope `client.create`).
  - Pre-seeded DCR admin client: `dcr-admin-client` / `dcr-secret`.
- **Token Customization (`TokenConfig`)**:
  - Dynamically adds standard OIDC claims: `sub` (User UUID), `preferred_username`, `email`, `given_name`, `family_name`, `picture`, `locale`, etc.
  - Adds user permissions and roles into the JWT Access Token: `roles` (`["ADMIN", "USER"]`) and `authorities` (`["user:read", "user:update", ...]`).

### 2. Backend For Frontend (BFF) & Gateway Server Integration
- Pre-seeded confidential client for Spring Cloud Gateway:
  - `client_id`: `front-bff-client`
  - `client_secret`: `secret`
  - `redirect_uris`: `http://localhost:9999/login/oauth2/code/front-bff-client`
- Gateway relays incoming authenticated tokens to microservices using `TokenRelay` default filter.

### 3. REST API & RBAC Security (Base path `/api`)
All endpoints are secured via JWT bearer tokens and verified with Method Security `@PreAuthorize`:
- **Users**: `/api/users/**`
  - `POST /api/auth/register` (Public)
  - `POST /api/users` (`user:create`)
  - `GET /api/users` (`user:read`)
  - `GET /api/users/{uuid}` (`user:read`)
  - `PATCH /api/users/{uuid}` (`user:update`)
  - `DELETE /api/users/{uuid}` (`user:delete`)
  - `POST /api/users/{uuid}/enable` / `disable` (`user:update`)
  - `POST /api/users/{uuid}/lock` / `unlock` (`user:update`)
  - `PUT /api/users/{uuid}/password` (`user:update`)
  - `GET /api/users/{uuid}/authorities` (`user:read`)
  - `POST /api/users/{uuid}/roles` (`user:assign`)
  - `DELETE /api/users/{uuid}/roles/{roleUuid}` (`user:assign`)
  - `POST /api/users/{uuid}/permissions` (`user:assign`)
  - `DELETE /api/users/{uuid}/permissions/{permissionUuid}` (`user:assign`)
  - `GET /api/me` (Authenticated)
  - `PATCH /api/me` (Authenticated)
  - `PUT /api/me/password` (Authenticated)
- **Roles**: `/api/roles/**` (`role:create`, `role:read`, `role:update`, `role:delete`, `role:assign`)
- **Permissions**: `/api/permissions/**` (`permission:create`, `permission:read`, `permission:update`, `permission:delete`)
- **Error Handling**: RFC 9457 `ProblemDetail` with `type`, `title`, `status`, and `errors`.

### 4. JTE Web Pages & Internationalization (i18n)
Server-side rendered templates located in `iam-web/src/main/jte`:
- `login.jte`: Sign-in form, social login buttons (Google, GitHub, Facebook, Telegram), error alerts.
- `register.jte`: Public account registration with validation.
- `consent.jte`: OAuth 2.1 scope authorization consent.
- `index.jte`: User profile, OIDC claims, assigned roles & permissions.
- `profile-edit.jte`: Self-service profile update.
- `change-password.jte`: Password change form.
- `linked-accounts.jte`: Manage federated social identities.
- `connected-apps.jte`: Authorized client applications.
- **Language Switcher**: Toggle via `?lang=en` or `?lang=km` across all views with externalized message bundles.

---

## 👥 Seeded Credentials

| Entity | Identifier / Username | Secret / Password | Role / Scope |
|---|---|---|---|
| **Admin User** | `houngmingyeak` | `supersecret` | `ADMIN` (All resource permissions) |
| **Gateway BFF Client** | `front-bff-client` | `secret` | `openid`, `profile`, `email` |
| **DCR Admin Client** | `dcr-admin-client` | `dcr-secret` | `client.create` |

---

## 🚀 Running the Project

### 1. Start Database
PostgreSQL on port `9543`:
- Database: `db_oauth2`
- User: `iteuser`
- Password: `postgre`

### 2. Start Services
1. Run `iam-service`:
   ```bash
   ./gradlew :iam-main:bootRun
   ```
   Available at `http://localhost:9000`
2. Run `gateway-server`:
   ```bash
   ./gradlew bootRun
   ```
   Available at `http://localhost:9999`

### 3. Verify End-to-End
Open an Incognito browser window to:
```text
http://localhost:9999/business-service/api/businesses/flux
```
Log in using `houngmingyeak` / `supersecret`. The Gateway will exchange tokens at `POST http://localhost:9000/oauth2/token` and relay them to `business-service`.

---

## 📬 Postman Collection
Import `postman/IAM_Service.postman_collection.json` into Postman to test all endpoints.

