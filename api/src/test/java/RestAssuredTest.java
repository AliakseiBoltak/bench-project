import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import model.requests.CreateUserRequest;
import model.responses.CreateUserResponse;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static constants.Constants.BASE_URI;

class RestAssuredTest {

    private static final String CREATE_USER_URI = "/api/users";
    private static final String USER_NAME = "John Doe";
    private static final String USER_JOB = "Software Engineer";

    @Test
    void checkCreatedUserTest() {

        Allure.step("Starting create a user test ...");

        CreateUserRequest createUserRequest = new CreateUserRequest(USER_NAME, USER_JOB);

        CreateUserResponse createUserResponse = RestAssured
                .given()
                .baseUri(BASE_URI)
                .log().everything()
                .contentType("application/json")
                .header("x-api-key", "reqres-free-v1")
                .body(createUserRequest)
                .when()
                .post(CREATE_USER_URI)
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(CreateUserResponse.class);

        assertEquals(createUserResponse.getJob(), createUserRequest.getJob());
        assertEquals(createUserResponse.getName(), createUserRequest.getName());
        assertNotNull(createUserResponse.getId());
        assertNotNull(createUserResponse.getCreatedAt());

        Allure.step("Finishing create a user test ...");
    }
}