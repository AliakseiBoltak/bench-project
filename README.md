Import as maven project and install artifact -
```mvn clean install -DskipTests=true```

Navigate to corresponding module with tests and run tests -
```mvn clean test```

Generate Allure report -
```mvn allure:report```

Open Allure report -
```mvn allure:serve```

> **Note:**
> - Before running tests in the `db` module, execute script `init_test_db.sql` to create the database and tables.
> - Before running tests in the `ui-playwright` module, execute the following command to install Playwright browsers:
    >   ```sh
>   mvn exec:java -e \
>     -D exec.mainClass=com.microsoft.playwright.CLI \
>     -D exec.args="install"
>   ```
> - **To adjust the default browser or headless mode for UI tests, you can pass JVM parameters when running tests. For example:**
    >   ```sh
>   mvn clean test -Dheadless=false -Dbrowser=firefox
>   ```
    >   This will run the UI tests in headed mode using Firefox instead of the default headless Chromium.
> - **You can also modify the environment used for all tests by passing the `env` parameter. For example:**
    >   ```sh
>   mvn clean test -Denv=test
>   ```
    >   By default, `env` is set to `'default'`. Setting `-Denv=test` will switch tests to use the `test` environment configuration.