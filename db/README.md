# Database (DB) Module

This module contains database integration tests utilizing raw JDBC, TestNG, Guice for dependency injection, and Allure for reporting. It relies on the `core` module and uses `mysql-connector-j` version `9.3.0` for MySQL connections.

## 📁 Project Structure

    db/
    ├── src/main/java/
    │   ├── constants/        
    │   │   └── Constants.java                  # Defines SQL_QUERIES_PATH[cite: 16]
    │   ├── interfaces/       
    │   │   └── ResultSetMapper.java            # Functional interface for mapping result sets[cite: 15]
    │   ├── model/            
    │   │   └── OrderWithUserDataRecord.java    # Lombok POJO mapping DB results to objects[cite: 14]
    │   └── queries/          
    │       └── QueriesBank.java                # Centralized query loader[cite: 13]
    └── src/test/java/
        ├── BaseDBTest.java                     # Base class managing DB connections[cite: 12]
        ├── GetOrdersForUsersTest.java          # Concrete TestNG test class[cite: 11]
        └── resources/
            ├── scripts/
            │   ├── init_test_db.sql            # DB initialization script[cite: 20]
            │   └── select_orders_for_users_join_query.sql # Raw SQL queries[cite: 19]
            └── db-suite.xml                    # TestNG execution suite[cite: 18]

## 🏗️ Core Components and Architecture

To ensure thread safety and maintainability, the database testing architecture is split into specific responsibilities:

### 1. `BaseDBTest` (Connection Management)
Location: `src/test/java/BaseDBTest.java`
*   **Responsibility:** Manages the TestNG lifecycle (`@BeforeClass`, `@AfterClass`) and handles JDBC connections
*   **Thread Safety:** Utilizes a `ThreadLocal<Connection>` to ensure parallel test execution does not result in connection conflicts
*   **Dependency Injection:** Injects `ConfigLoader` via Guice (`@Guice(modules = {CoreModule.class})`) to fetch database credentials dynamically
*   **Execution:** Provides a generic `executeQueryAndMapResult` helper method to run SQL and map the output

### 2. `QueriesBank` (SQL Externalization)
Location: `src/main/java/queries/QueriesBank.java`
*   **Responsibility:** Prevents hardcoding large SQL queries inside test classes
*   **Implementation:** Reads raw `.sql` files from `src/test/resources/scripts/` using `FileManager.readFileAsString` and stores them as public constants

### 3. Models and Mapping
Location: `src/main/java/model/`
*   **Responsibility:** Represents database rows as Java objects.
*   **Implementation:** Uses Lombok (`@Data`, `@Builder`, `@FieldDefaults`) for boilerplate reduction. Instead of mapping data in the test, models expose a static `mapRowsFromResultSet(ResultSet rs)` factory method to map column names (e.g., `order_id`, `username`) to class fields

## 🚀 Setup and Execution

### 1. Database Initialization
Before running the tests for the first time, you must initialize the local database schema
Execute the `init_test_db.sql` script in your local MySQL instance. This script will:
*   Create a database named `test_db`
*   Create a user `test_user` with the password `test_pass` and grant the necessary privileges
*   Create the required `users` and `orders` tables with foreign key constraints
*   Insert sample data required by the tests

### 2. Running Tests
The module uses a dedicated TestNG suite named `DB Suite` configured for parallel execution at the class level (`parallel="classes"`). The suite automatically registers `AllureListener` to capture test execution steps

Run the tests via Maven:

    mvn clean test -DsuiteXmlFile=db-suite.xml

### Reporting

To generate and view the Allure report after execution (see root `README.md` for the full explanation of why `allure:report`, not `allure:serve`, is required before saving history):

    mvn antrun:run@restore-allure-history   # optional: restore trend history from previous runs
    mvn allure:report                       # generates target/site/allure-maven-plugin/ (required before saving history)
    mvn antrun:run@copy-allure-history       # optional: persist this run's history back to db/allure-history/
    mvn allure:serve                        # opens the report in your browser

If you don't need trend history, skip straight to:

    mvn allure:serve

### 3. Assertions and Logging
*   Tests leverage TestNG assertions (e.g., `assertFalse`)
*   All significant actions, such as executing queries and finding specific records, are logged directly into the report using `Allure.step()`