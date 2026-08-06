# Dependency & Build Conventions

## Current state (follow, but see caveats)

The root `pom.xml` is a parent aggregator (`packaging=pom`, `<modules>` for all 5) that declares shared test-framework dependencies (`testng`, `allure-testng`, `cucumber-*`, `allure-cucumber7-jvm`, `awaitility`, `groovy`) directly under `<dependencies>` — **not** `<dependencyManagement>`. This means every module, including ones with no Cucumber usage, transitively receives the Cucumber/Allure-Cucumber jars. When adding a new cross-module dependency, mirror this existing pattern (add it to the root `<dependencies>` block) for consistency — but be aware this is a known architectural smell, not something to double down on. If a new dependency is genuinely module-specific (like `selenide`, `playwright`, `mysql-connector-j` currently are), declare it only in that module's own `pom.xml`, following those existing examples.

## Shared versions

Cross-module version numbers are root `<properties>`: `core.library.version`, `surefire.version`, `aspectj.version`, `allure.version`, `cucumber.version`. When bumping a shared library version, update the root property — do not hardcode the version again in a child POM's own property block.

Known inconsistency to be aware of (don't replicate): `allure-rest-assured` in `api/pom.xml` and `allure-cucumber7-jvm` in the root POM are hardcoded to `2.29.1`, diverging from the shared `allure.version=2.24.0` property. If you touch either, prefer aligning it to `${allure.version}` rather than adding a third hardcoded version elsewhere.

## `core` dependency in child modules

Each of `api`, `ui`, `ui-playwright`, `db` declares the same block to depend on `core`:

```xml
<dependency>
    <groupId>org.example</groupId>
    <artifactId>core</artifactId>
    <version>${core.library.version}</version>
</dependency>
```

Copy this exact block (with `${core.library.version}`, never a literal version) when wiring `core` into a new or existing module.

## Surefire / suite selection

Every module inherits the root `pluginManagement` surefire config, which points at `src/test/resources/suites/${suiteXmlFile}.xml`. Each module sets its own default via a `<properties><suiteXmlFile>...</suiteXmlFile></properties>` override in that module's own `pom.xml` (e.g. `api-suite`, `ui-suite`, `db-suite`, `playwright-suite`). To run a non-default suite, override at the command line: `mvn -f api/pom.xml test -DsuiteXmlFile=my-suite` — don't hardcode an alternate suite path in the POM itself.

## Allure history

Root `pluginManagement` wires `maven-antrun-plugin` executions that copy `allure-history/` into `target/allure-results/history` before tests (`process-test-resources`) and back out after report generation (`verify`), per module, to preserve Allure trend graphs across runs. Don't remove or bypass these executions when editing the surefire/allure plugin config — they're what make `mvn allure:report` show history trends.

## Generated test data

`core`'s `RandomUserGenerator` runs via `exec-maven-plugin` in the `generate-test-resources` phase during `core`'s own `mvn install`, writing `users.json`. `ui` and `ui-playwright` then copy that file into their own `src/test/resources/users/` via `maven-resources-plugin`, also bound to `generate-test-resources`. If a new module needs the generated user data, add the same `maven-resources-plugin` copy execution (copying from `core`'s output path) rather than depending on `core`'s generator running again or duplicating generation logic.
