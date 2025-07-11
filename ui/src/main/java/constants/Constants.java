package constants;

import static org.example.constants.Constants.ENV;

public class Constants {

    private Constants() {
    }

    public static final String USERS_TEST_DATA_PATH = System.getProperty("user.dir") + "/src/test/resources/users/"
            + ENV + "/users.json";
}
