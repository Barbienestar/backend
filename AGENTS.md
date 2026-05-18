# AGENTS.md

## Development Commands

```bash
./mvnw quarkus:dev           # Run in dev mode with live coding
./mvnw test                  # Run unit tests
./mvnw test -Dtest=ClassName # Run single test class
./mvnw package               # Build JAR
```

## Test Database

Test users are seeded via `src/test/resources/import.sql`. Each user's `provider_uuid` doubles as the Bearer token value in tests:

| Token | Role |
|---|---|
| `admin-token` | admin |
| `health-token` | health |
| `citizen-token` | citizen |

## Architecture

Layered architecture following clean architecture principles:

```
domain/models           # Domain entities (User, Report, Hospital...)
domain/repository      # Repository interfaces
application/usecase   # Business logic (CreateReportUseCase, etc.)
application/dto      # Data transfer objects
application/security # Auth context, role filters
interfaces/rest      # REST endpoints (resources)
infrastructure/    # Persistence, Firebase, mappers
```

## Layer Separation

- **Never** inject infrastructure classes into domain/application layers
- **Always use mappers** in `infrastructure/mapper/` to convert between layers
- Mappers must be reusable and stateless (static methods)
- **Layers must be completely independent** - no direct imports between domain/application and infrastructure

## Code Principles

- Prefer code legibility over abstraction
- Use SOLID principles when possible
- Avoid complex flows

## Code Placement

If unsure where to put a file, ask the user. General guidelines:

- `domain/models/` - Domain entities
- `domain/repository/` - Repository interfaces
- `application/usecase/` - Use case logic
- `application/dto/` - DTOs
- `infrastructure/persistence/entity/` - JPA entities
- `infrastructure/persistence/repository/` - Repository implementations
- `infrastructure/mapper/` - Entity<->Domain mappers
- `interfaces/rest/` - REST resources

## Testing

When creating a new useCase, create **both**:

1. **Unit test** - `src/test/java/.../usecase/*Test.java` with Mockito mocks
2. **Integration test** - `src/test/java/.../interfaces/rest/*Test.java` with `@QuarkusTest`

Unit test pattern:
```java
@BeforeEach
void setup() {
    repository = mock(Repository.class);
    useCase = new UseCase(repository);
}
```

Integration test auth — use Bearer token matching the seeded `provider_uuid`:
```java
given()
    .header("Authorization", "Bearer citizen-token")
    .when().get("/reports/me")
    .then().statusCode(200);

// No token → 401
given().when().get("/reports/me").then().statusCode(401);

// Wrong role → 403
given()
    .header("Authorization", "Bearer admin-token")
    .when().post("/reports")
    .then().statusCode(403);
```

## Auth & Roles

- `@PermitPublic` — marks endpoints/classes as public (no token required). Placed in `application/security/PermitPublic.java`
- `@RequireRoles({"admin"})` — restricts to specific roles. Placed in `application/security/RequireRoles.java`
- Endpoints with **neither** annotation require a valid token but accept any role (pass-through)
- `MockFirebaseAuthFilter` replaces `FirebaseAuthFilter` in tests (`@UnlessBuildProfile("test")`) — validates tokens by looking up `provider_uuid` in DB instead of Firebase
- `RoleAuthorizationFilter` checks `@RequireRoles` at `Priorities.AUTHORIZATION` — returns 401 if no `CurrentUser`, 403 if role mismatch
- `CurrentUser` is injected via `AuthenticatedUserContext` (`@RequestScoped`)

Request flow (test profile):
```
MockFirebaseAuthFilter → RoleAuthorizationFilter → Resource method
```

## DB

- MySQL in production
- H2 for tests (quarkus-jdbc-h2)
