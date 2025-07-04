Import as maven project, navigate to core module and deploy -
```mvn deploy```

Navigate to corresponding module with tests and run tests (before running db tests execute sql script from resources folder) -
```mvn clean test```

Generate Allure report -
```mvn allure:report```