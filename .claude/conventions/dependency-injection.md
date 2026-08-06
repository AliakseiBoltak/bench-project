# Dependency Injection (Guice)

Every test class is wired through Google Guice, driven by TestNG's `@Guice` annotation — not constructed manually and not via Spring.

## Base test classes

Each module's abstract base test class:

```java
@Guice(modules = {CoreModule.class})
public abstract class BaseXxxTest {

    protected final ConfigLoader configLoader;
    protected final <derivedField> <name>;   // e.g. baseUri, baseUrl, gitHubUrl

    @Inject
    public BaseXxxTest(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        this.<name> = configLoader.get<Something>();
    }
}
```

- `@Guice(modules = {CoreModule.class})` goes on the **base class**, not on each concrete test.
- The base class constructor takes `ConfigLoader` and immediately derives the one field the module actually needs (`baseUri` for `api`, `baseUrl` for `ui`, `gitHubUrl` for `ui-playwright`); `db`'s base class keeps `configLoader` itself since it needs multiple `db.*` values.
- `GitHubBaseTest` (ui-playwright) is the one exception that is not `abstract` — new Playwright base classes should still follow the abstract pattern unless there's a specific reason not to.

## Concrete test classes

Every concrete test class must declare its own `@Inject` constructor, even when it takes no extra dependencies beyond what the base class needs — call `super(configLoader)` explicitly:

```java
class CreateUserTest extends BaseAPITest {

    @Inject
    public CreateUserTest(ConfigLoader configLoader) {
        super(configLoader);
    }
    ...
}
```

When a test needs additional injected collaborators (services, mission/workflow helpers), add them as extra constructor parameters, assign to `private final` fields, and pass `configLoader` through to `super(...)`:

```java
@Inject
public LoginWithInvalidCredsTest(UserDataService userDataService, LoginMissions loginMissions,
                                 ConfigLoader configLoader) {
    super(configLoader);
    this.userDataService = userDataService;
    this.loginMissions = loginMissions;
}
```

Do not use field injection (`@Inject` on fields) — constructor injection only.

## `CoreModule`

`core`'s `CoreModule` (`org.example.guice.CoreModule`) is the single Guice module used everywhere. It:
- provides `ConfigLoader` via a `@Provides @Singleton` method (not a `bind()`), since `ConfigLoader` reads `env.conf` at construction time and should only do so once per JVM;
- binds interface → implementation pairs via `bind(X.class).to(Y.class)` in `configure()` for everything else (`DataLoader→JSONDataLoader`, `UserDataDao→UserDataJsonDao`, `UserDataService→UserDataJsonService`).

When adding a new injectable component to `core`, follow the same split: `@Provides @Singleton` for anything with expensive/stateful construction (I/O, config parsing), `bind().to()` for stateless interface implementations, all inside `CoreModule` — don't create a second Guice module unless a module-specific binding genuinely doesn't belong in `core`.
