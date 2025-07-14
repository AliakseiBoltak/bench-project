Import as maven project and install artifact -
```mvn clean install -DskipTests=true```

Navigate to corresponding module with tests and run tests (before running tests in db module execute script init_test_db.sql) -
```mvn clean test```

Generate Allure report -
```mvn allure:report```

Open Allure report -
```mvn allure:serve```