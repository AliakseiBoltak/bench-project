# Project Conventions

Observed conventions extracted from the existing codebase (not aspirational best practices) — follow these when writing or modifying code in this repo so new code matches what's already here.

- [module-structure.md](module-structure.md) — module layout, package naming, `env.conf` profiles, TestNG suite XML.
- [dependency-injection.md](dependency-injection.md) — Guice wiring pattern for base test classes, concrete tests, and `CoreModule`.
- [test-conventions.md](test-conventions.md) — test/method naming, TestNG usage, assertions, Allure step reporting, imports.
- [page-object-model.md](page-object-model.md) — Selenide (`ui`) vs Playwright (`ui-playwright`) page object idioms.
- [models-and-constants.md](models-and-constants.md) — Lombok model annotation stack, `Constants` class shape, SQL constant handling.
- [dependency-management.md](dependency-management.md) — root vs child POM version handling, `core` dependency block, suite selection, Allure history plumbing.

These reflect current practice, including a few known inconsistencies (called out inline where relevant) — where a file notes something as a smell rather than a rule to copy, prefer fixing it going forward rather than replicating it further.
