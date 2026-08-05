import com.google.inject.Inject;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import model.requests.CreateUserRequest;
import model.responses.CreateUserResponse;
import org.awaitility.Awaitility;
import org.example.config.ConfigLoader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static constants.Constants.USER_URI;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

class WaitForUserCreationTest extends BaseAPITest {

    @Inject
    public WaitForUserCreationTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    private int createdUserId;

    @AfterMethod
    public void cleanUp() {
        cleaningUpCreatedUser(createdUserId);
    }

    @Test(description = "Waits for user to be created using Awaitility with 5-second timeout")
    void waitForUserCreationTest() {
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .name("Waiting User")
                .job("Test Engineer")
                .build();

        AtomicReference<CreateUserResponse> userResponse = new AtomicReference<>();

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .pollDelay(100, TimeUnit.MILLISECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    CreateUserResponse response = RestAssured.given()
                            .spec(baseRequestSpec)
                            .body(createUserRequest)
                            .when()
                            .post(USER_URI)
                            .then()
                            .statusCode(201)
                            .extract()
                            .body()
                            .as(CreateUserResponse.class);

                    userResponse.set(response);
                    assertNotNull(response.getId(), "User ID should not be null");
                });

        CreateUserResponse result = userResponse.get();
        createdUserId = result.getId();
        Allure.step("User created with ID: " + createdUserId + " after waiting up to 5 seconds");

        assertEquals(result.getName(), createUserRequest.getName(), "Name does not match");
        assertEquals(result.getJob(), createUserRequest.getJob(), "Job does not match");
        assertNotNull(result.getCreatedAt(), "Created at timestamp should not be null");
    }
}
