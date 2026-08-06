# Models & Constants

## Request/response/data models (Lombok)

Model classes (`api/model/requests`, `api/model/responses`, `db/model`, `core/org.example.model`) use Lombok, not hand-written getters/setters/builders:

```java
package model.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {
    String name;
    String job;
}
```

- Standard annotation stack for a plain data/DTO model: `@Data @Builder @NoArgsConstructor @AllArgsConstructor @FieldDefaults(level = AccessLevel.PRIVATE)`.
- Fields are declared without an explicit `private` modifier — `@FieldDefaults` supplies it. Don't write `private String name;` redundantly in these classes.
- Build instances via the Lombok builder (`CreateUserRequest.builder().name(...).job(...).build()`), not `new` + setters, in test code.
- `db`'s row-mapping models (e.g. `OrderWithUserDataRecord`) additionally expose a static `mapRowsFromResultSet(ResultSet)`-style factory method used as a method reference with the JDBC helper — keep mapping logic on the model class itself, not in the test or DAO.

## Constants

Each module keeps its own `Constants` class (`api.constants.Constants`, `db.constants.Constants`, `core.org.example.config.Constants`, `ui-playwright.constants.PathConstants`) as a non-instantiable utility holder:

```java
package constants;

public class Constants {
    private Constants() {
    }

    public static final String USER_URI = "/api/users";
    public static final String X_API_KEY_HEADER = "x-api-key";
    public static final String X_API_KEY_VALUE = "reqres-free-v1";
}
```

- Private no-arg constructor to prevent instantiation.
- `public static final` fields only, `SCREAMING_SNAKE_CASE` names.
- Module-specific constants (API URIs/headers, DB paths, UI path fragments) stay in that module's own `Constants`/`PathConstants` — don't centralize unrelated modules' constants into `core`. `core`'s `Constants` is reserved for cross-module file-path constants (`USERS_TEST_DATA_PATH`, generator output paths).

## SQL

`db` centralizes raw SQL strings as `public static final String` constants in `queries.QueriesBank`, referenced by name (`SELECT_ORDERS_FOR_USERS_JOIN_QUERY`) — don't inline SQL literals in test classes.
