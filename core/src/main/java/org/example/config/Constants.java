package org.example.config;

public class Constants {

    private Constants() {
    }

    public static final String USERS_TEST_DATA_PATH = System.getProperty("user.dir") +
            "/src/test/resources/users/users.json";

    public static final String GENERATED_USERS_PATH = System.getProperty("user.dir") +
            "/src/main/resources/users/users.json";
}
