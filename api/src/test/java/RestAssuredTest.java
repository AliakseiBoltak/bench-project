import io.restassured.RestAssured;
import model.requests.CreateUserRequest;
import model.responses.CreateUserResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import static constants.Constants.BASE_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestAssuredTest {

    private static final String CREATE_USER_URI = "/api/users";
    private static final String USER_NAME = "John Doe";
    private static final String USER_JOB = "Software Engineer";
    private static final Logger LOGGER = LogManager.getLogger(RestAssuredTest.class);

    @Test
    void checkCreatedUserTest() {

        LOGGER.info("Starting create a user test ...");

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

        LOGGER.info("Finishing create a user test ...");
    }
}