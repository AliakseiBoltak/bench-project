Import as maven project and install artifact -
```mvn clean install -DskipTests=true```

Navigate to corresponding module with tests and run tests -
```mvn clean test```

Generate Allure report -
```mvn allure:report```

Open Allure report -
```mvn allure:serve```

Note:
- Before running tests in 'db' module execute script `init_test_db.sql` to create database and tables.
- Before running tests in 'ui-playwright' execute command `mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"` to install Playwright browsers