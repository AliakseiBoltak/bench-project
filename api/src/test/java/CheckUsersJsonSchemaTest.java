import com.google.inject.Inject;
import io.restassured.RestAssured;
import org.example.config.ConfigLoader;
import org.testng.annotations.Test;

import static constants.Constants.USER_URI;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CheckUsersJsonSchemaTest extends BaseAPITest {

    @Inject
    public CheckUsersJsonSchemaTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @Test(description = "Checks that users JSON response matches the schema")
    void checkUsersJsonSchemaTest() {
        RestAssured.given()
                .spec(baseRequestSpec)
                .when()
                .get(USER_URI)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/getUsersSchema.json"));
    }
}