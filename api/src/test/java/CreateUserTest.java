import com.google.inject.Inject;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import model.requests.CreateUserRequest;
import model.responses.CreateUserResponse;
import org.example.config.ConfigLoader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static constants.Constants.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

class CreateUserTest extends BaseAPITest {

    @Inject
    public CreateUserTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @DataProvider
    public Object[][] userDataProvider() {
        return new Object[][]{
                {"John Doe", "Software Engineer"},
                {"Jane Smith", "QA Engineer"}
        };
    }

    @AfterMethod
    public void cleanUp() {
        cleaningUpCreatedUser(createdUserId);
    }

    private int createdUserId;

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
                .statusCode(201)
                .extract()
                .body()
                .as(CreateUserResponse.class);

        createdUserId = createUserResponse.getId();
        Allure.step("Created user ID: " + createdUserId);

        assertEquals(createUserResponse.getJob(), createUserRequest.getJob(), "Job does not match");
        assertEquals(createUserResponse.getName(), createUserRequest.getName(), "Name does not match");
        assertNotNull(createUserResponse.getCreatedAt(), "Created at timestamp should not be null");
    }
}