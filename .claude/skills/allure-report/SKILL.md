---
name: allure-report
description: Generates and serves Allure test reports per module in this Maven multi-module test framework, and understands the allure-results/allure-history plumbing wired into the root POM's pluginManagement — including a known gap where the history trend-graph copy never actually runs because no module declares maven-antrun-plugin in its own <build><plugins>. Trigger on "generate allure report", "show me the allure report", "serve allure", "allure history isn't working", "why is the trend graph empty".
---

# Allure Report

## When to use

Whenever the user wants to see, generate, or debug an Allure report for `api`, `api-apache-http`, `ui`, `ui-playwright`, or `db` — including questions about why history/trend graphs aren't showing up.

## Prerequisites

Tests must already have run for the target module (`mvn -f <module>/pom.xml test`), so that `<module>/target/allure-results/` contains result JSON/attachment files. **Do not run `mvn clean` between the test run and report generation** — `clean` deletes `target/`, including `allure-results/`, wiping out the very data the report reads.

If `target/allure-results/` is empty or missing, that's not a report-generation bug — go back and (re-)run the tests for that module first (see the `run-tests` skill).

## Generating a report

Per module, from the repo root:

```sh
mvn -f <module>/pom.xml allure:report      # static HTML report
mvn -f <module>/pom.xml allure:serve       # generates + opens in default browser; blocks the terminal until closed
```

Both goals resolve their version/config from the root `pom.xml`'s `<pluginManagement>` entry for `io.qameta.allure:allure-maven` (currently version `2.15.2`, `reportVersion=${allure.version}=2.24.0`) automatically — no per-module plugin declaration is needed to invoke them directly by goal name.

### Where output lands

- Raw results (input): `<module>/target/allure-results/`
- Static report (output of `allure:report`): `<module>/target/site/allure-maven-plugin/index.html`
- `allure:serve` does **not** reuse that directory — it builds into a temp location and serves it over a local HTTP server; stopping the command (Ctrl+C) or closing the terminal ends the server.

### `db` module caveat

`db`'s tests require a running local MySQL instance (`localhost:3306`) and are excluded from CI (`.github/workflows/maven.yml`) — its Allure report can only ever be generated locally, never pulled from a CI run.

## History / trend graphs — known limitation, verify before promising it works

The root `pom.xml` defines, under `<pluginManagement>`, two `maven-antrun-plugin` executions intended to make trend graphs (pass/fail over time) work across runs:

- **`restore-allure-history`** (phase `process-test-resources`) — copies `<module>/allure-history/` into `target/allure-results/history` *before* tests run, seeding the next report with prior runs' data.
- **`copy-allure-history`** (phase `verify`) — copies the freshly-built `target/site/allure-maven-plugin/history` back out to `<module>/allure-history/` *after* the report is generated, so it's available for the *next* run.

**`pluginManagement` only pre-configures a plugin — it does not add the plugin to the build.** A plugin bound to a non-default-lifecycle phase (like `maven-antrun-plugin` here) only actually executes if the module's own POM also declares it under `<build><plugins>`. As of this writing, **none of `api`, `api-apache-http`, `core`, `db`, `ui`, `ui-playwright` declare `maven-antrun-plugin`** — confirm this is still true with `grep -l maven-antrun-plugin */pom.xml` before relying on it, since it's the kind of thing that can get fixed later. If it's still absent, the history round-trip is currently dead: `ui-playwright/allure-history/` exists on disk (and is untracked in git — not covered by `.gitignore`'s root-anchored `/allure-history/` pattern) but nothing in the Maven build is writing new data into it or reading it back out.

### How to make trend graphs actually work for a module

1. Add an explicit `<plugin>` entry for `maven-antrun-plugin` (no extra `<configuration>` needed — it inherits everything from the root `pluginManagement`) to that module's own `pom.xml` under `<build><plugins>`.
2. Chain the phases correctly in one invocation — `allure:report` is a directly-invoked goal, not bound to any lifecycle phase, so it will **not** trigger the `verify`-phase `copy-allure-history` execution on its own:
   ```sh
   mvn -f <module>/pom.xml clean verify        # runs tests, restores history before, copies history back after report... 
   ```
   — but note `verify` alone doesn't generate the *report*, only runs the `antrun` copy-back if something upstream already built `target/site/allure-maven-plugin/history`. In practice, the working sequence once `maven-antrun-plugin` is declared is:
   ```sh
   mvn -f <module>/pom.xml clean test                 # process-test-resources restores history into allure-results
   mvn -f <module>/pom.xml allure:report               # builds target/site/allure-maven-plugin (with restored history)
   mvn -f <module>/pom.xml verify -DskipTests          # verify phase runs copy-allure-history, writing the new history back to allure-history/
   ```
   Simplify this for the user only after confirming it behaves as expected locally — don't assert it works from documentation alone, since this plumbing has clearly drifted from what's declared vs. what's wired up.

## Troubleshooting checklist

- **Empty or missing report** → check `<module>/target/allure-results/` has files; if not, tests weren't run or `clean` ran after them.
- **No trend graph despite repeated runs** → expected given the limitation above; don't treat it as a report-generation bug. Explain the `maven-antrun-plugin` gap rather than trying to "fix" `allure:report` itself.
- **CI has no Allure output at all** → `.github/workflows/maven.yml` never calls `allure:report` or uploads `target/allure-results`/`target/site` as a build artifact. Reports must be generated locally; if the user wants CI-visible reports, that requires adding an `actions/upload-artifact` step (or a GitHub Pages publish step) — treat that as a separate, explicit ask, not something this skill does automatically.
- **`@Step`/Allure annotations not showing detail** → check the surefire `argLine` javaagent for `aspectjweaver` is present (it's set in root `pluginManagement`, inherited by every module by default since `maven-surefire-plugin` is a default-lifecycle-bound plugin) — this is almost never the actual cause, but rule it out before assuming a code issue.
