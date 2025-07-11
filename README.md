Import as maven project, navigate to core module and install artifact -
```mvn clean install```

Navigate to corresponding module with tests and run tests (before running tests in db module execute script init_test_db.sql) -
```mvn clean test -Denv=default```

Generate Allure report -
```mvn allure:report```

Open Allure report -
```mvn allure:serve```