package org.example.constants;

public class Constants {

    private Constants() {
    }

    public static final String ENV = System.getProperty("env") != null && !System.getProperty("env").isEmpty()
            ? System.getProperty("env") : "test";
    public static final String TEST_DATA_PATH = System.getProperty("user.dir") + "/src/test/resources/"
            + ENV + "/users.json";
}
