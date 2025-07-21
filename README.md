## How to Build and Run Tests

### 1. Import as Maven Project and Install Artifact

```sh
mvn clean install -DskipTests=true
```

---

### 2. Navigate to the Corresponding Module and Run Tests

```sh
mvn clean test
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

## Notes

- **Database Tests:**  
  Before running tests in `db` module, execute the script `init_test_db.sql` to create the database and tables.

- **UI Playwright Tests:**  
  Before running tests in `ui-playwright` module, you must install Playwright browsers by running:
  ```sh
  mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
   ```
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