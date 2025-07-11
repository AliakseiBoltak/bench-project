import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import model.requests.CreateUserRequest;
import model.responses.CreateUserResponse;
import org.testng.annotations.Test;

import static constants.Constants.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

class CreateUserTest {

    private static final String USER_NAME = "John Doe";
    private static final String USER_JOB = "Software Engineer";

    @Test
    void checkUserCreatedWithExpectedNameAndJobTest() {

        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .name(USER_NAME)
                .job(USER_JOB)
                .build();

        CreateUserResponse createUserResponse = RestAssured
                .given()
                .baseUri(BASE_URI)
                .log().all()
                .contentType(ContentType.JSON)
                .header(X_API_KEY_HEADER, X_API_KEY_VALUE)
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

    @Test
    void checkUsersJsonSchemaTest() {

        RestAssured.given()
                .baseUri(BASE_URI)
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(USER_URI)
                .then()
                .log().all()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/getUsersSchema.json"));

    }
}