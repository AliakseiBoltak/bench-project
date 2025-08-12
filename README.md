## How to Build and Run Tests

### 1. Run Tests

```sh
mvn clean test
```

---

### 2. Restore Allure History from Previous Runs

To enable test trend statistics (history) in Allure reports, restore the history from your previous report using:

```sh
mvn antrun:run@restore-allure-history
```

---

### 3. Generate Allure Report

```sh
mvn allure:report
```

---

### 4. Open Allure Report

```sh
mvn allure:serve
```

---

### 5. Save Allure History for Future Runs

After generating and serving the report, save the current run's Allure history so trends will persist between runs:

```sh
mvn antrun:run@copy-allure-history
```

> **Note:**  
> Allure history steps (restore/save) are required only if you want to see trends/statistics across runs.  
> If you don’t need trends, you can skip steps 2 and 5.

---

## Notes

- **Database Tests:**  
  Before running tests in `db` module, execute the script `init_test_db.sql` to create the database and tables.

- **UI Playwright Tests:**  
  Before running tests in `ui-playwright` module, you must install Playwright browsers by running:
  ```sh
  mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
   ```
  To run Playwright tests that require GitHub authentication (such as tests that use a stored session for GitHub),  
  you must update your `env.conf` file with valid GitHub credentials.

  After running your tests, you may have a Playwright trace file (for example, `github-login-trace.zip`).  
  To view and analyze this traced session in your browser, run the following command:

  ```sh
  mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace traces/github-login-trace.zip"
  ```
  This will open the Playwright Trace Viewer, allowing you to inspect every step of your test, including screenshots, network, console, and more.

- **Adjust Browser/Headless Mode:**  
  To change the default browser or headless mode for UI tests, pass JVM parameters when running tests. For example:
  ```sh
  mvn clean test -Dheadless=false -Dbrowser=firefox
  ```
  This will run the UI tests in headed mode using Firefox instead of the default headless Chromium.

- **Set Test Environment:**  
  You can also modify the environment used for all tests by passing the `env` parameter. For example:
  ```sh
  mvn clean test -Denv=test
  ```
  By default, `env` is set to `'default'`. Setting `-Denv=test` will switch tests to use the `test` environment configuration.
