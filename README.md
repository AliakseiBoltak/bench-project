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
> - **To adjust the default browser or headless mode for UI tests, pass JVM parameters when running tests. For example:**
    >   ```sh
>   mvn clean test -Dheadless=false -Dbrowser=firefox
>   ```