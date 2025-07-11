import io.restassured.RestAssured;
import model.requests.CreateUserRequest;
import model.responses.CreateUserResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static constants.Constants.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

class CreateUserTest extends BaseTest {

    @DataProvider
    public Object[][] userDataProvider() {
        return new Object[][] {
                {"John Doe", "Software Engineer"},
                {"Jane Smith", "QA Engineer"}
        };
    }

    @Test(dataProvider = "userDataProvider", description = "Checks user creation returns expected name/job")
    void checkUserCreatedWithExpectedNameAndJobTest(String name, String job) {

        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .name(name)
                .job(job)
                .build();

        CreateUserResponse createUserResponse = RestAssured.given()
                .spec(baseRequestSpec)
                .body(createUserRequest)
                .when()
                .post(USER_URI)
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .body()
                .as(CreateUserResponse.class);

        assertEquals(createUserResponse.getJob(), createUserRequest.getJob());
        assertEquals(createUserResponse.getName(), createUserRequest.getName());
        assertNotNull(createUserResponse.getId());
        assertNotNull(createUserResponse.getCreatedAt());
    }

    @Test(description = "Checks that users JSON response matches the schema")
    void checkUsersJsonSchemaTest() {

        RestAssured.given()
                .spec(baseRequestSpec)
                .when()
                .get(USER_URI)
                .then()
                .log().all()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/getUsersSchema.json"));

    }
}