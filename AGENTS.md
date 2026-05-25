# AGENTS.md

## Development Commands

```bash
./mvnw quarkus:dev           # Run in dev mode with live coding
./mvnw test                  # Run unit tests
./mvnw test -Dtest=ClassName # Run single test class
./mvnw package               # Build JAR
```

## Githooks

Pre-commit hook at `.githooks/pre-commit` runs `./mvnw clean compile` then `./mvnw clean test`. Every contributor must run once after cloning:

```bash
git config core.hooksPath .githooks
```

- The `.githooks/` directory is committed to the repo
- **Never bypass the hook** with `--no-verify` — it catches compilation errors and test failures before they land

## Test Database

Seeded via `src/test/resources/import.sql`. Each user's `provider_uuid` doubles as the Bearer token:

| Token | Role |
|---|---|
| `admin-token` | admin |
| `health-token` | health |
| `citizen-token` | citizen |

## Architecture

Layered architecture — no direct imports between domain/application and infrastructure. Mappers in `infrastructure/mapper/` convert between layers.

```
domain/models           # Domain entities
domain/repository       # Repository interfaces
application/usecase     # Business logic
application/dto         # DTOs
application/security    # Auth context, role filters
interfaces/rest         # REST endpoints (resources)
infrastructure/         # Persistence, Firebase, mappers
```

## Auth & Roles

Three auth modes determined by annotation presence:

| Annotation | Behavior |
|---|---|
| `@PermitPublic` | Public — no token required; `tryOptionalAuth()` populates `CurrentUser` if a valid token is present, otherwise proceeds anonymously |
| `@RequireRoles({"admin"})` | Restricted to listed roles — returns 401 if no token, 403 if role mismatch |
| neither | Requires valid token but accepts any role |

### tryOptionalAuth

Both `FirebaseAuthFilter` (production) and `MockFirebaseAuthFilter` (test) implement `tryOptionalAuth()` for `@PermitPublic` endpoints. It extracts a Bearer token if present, validates it, and sets `CurrentUser` via `AuthenticatedUserContext`. If no token or invalid token, the request continues anonymously. This lets public endpoints detect the current user when available (e.g., for personalized responses) without requiring authentication.

### Request flow (test profile)

```
MockFirebaseAuthFilter (AUTHENTICATION)
  → checks @PermitPublic → if yes: tryOptionalAuth()
  → validates Bearer token against provider_uuid in DB
RoleAuthorizationFilter (AUTHORIZATION)
  → checks @RequireRoles → 401/403 on mismatch
Resource method
```

### Key classes

| Class | Location |
|---|---|
| `PermitPublic` | `application/security/PermitPublic.java` |
| `RequireRoles` | `application/security/RequireRoles.java` |
| `AuthenticatedUserContext` | `application/security/AuthenticatedUserContext.java` — `@RequestScoped` holder for `CurrentUser` |
| `RoleAuthorizationFilter` | `application/security/RoleAuthorizationFilter.java` |
| `FirebaseAuthFilter` | `infrastructure/security/FirebaseAuthFilter.java` — production, `@UnlessBuildProfile("test")` |
| `MockFirebaseAuthFilter` | `test/.../infrastructure/security/MockFirebaseAuthFilter.java` — test replacement, validates `provider_uuid` directly |

## Testing

Create **both** for each use case:
1. **Unit test** — `src/test/java/.../usecase/*Test.java` with Mockito mocks
2. **Integration test** — `src/test/java/.../interfaces/rest/*Test.java` with `@QuarkusTest`

### Unit test gotcha

`new CurrentUser(user)` calls `user.getRole().getName()` internally. If user has no role, the NPE crashes inside `when().thenReturn()`, producing a cryptic "UnfinishedStubbing" Mockito error. **Always set a role:**

```java
User user = new User();
user.setRole(new Role((byte) 1, "citizen")); // required: CurrentUser constructor calls getRole().getName()
```

### Integration test auth

```java
// Valid token
given().header("Authorization", "Bearer citizen-token").when().get("/reports/me").then().statusCode(200);
// No token → 401
given().when().get("/reports/me").then().statusCode(401);
// Wrong role → 403
given().header("Authorization", "Bearer admin-token").when().post("/reports").then().statusCode(403);
```

## DB

- MySQL (production) via env vars: `DB_KIND`, `DB_USERNAME`, `DB_PASSWORD`, `DB_JDBC_URL`
- H2 in-memory (tests) configured at `%test` profile in `application.properties`
- Schema strategy: `validate` (prod), `drop-and-create` (test)

## Stack

- Quarkus 3.34.3, Java 21, Maven wrapper (./mvnw)
- Jakarta REST (quarkus-rest), Hibernate ORM, Firebase Admin SDK
- Lombok, Mockito, REST Assured
