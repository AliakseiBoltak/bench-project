package constants;

import org.example.config.Env;

public class Constants {

    private Constants() {
    }

    public static final String USERS_TEST_DATA_PATH = System.getProperty("user.dir") + "/src/test/resources/users/"
            + Env.getInstance().value() + "/users.json";

    public static final int WAIT_FOR_ELEMENT_MILLISECONDS_TIMEOUT = 5000;
}
