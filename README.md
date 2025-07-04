Import as maven project, navigate to core module and deploy -
```mvn deploy```

Navigate to corresponding module with tests and run tests (before running db tests execute init_test_db.sql script) -
```mvn clean test```

Generate Allure report -
```mvn allure:report```