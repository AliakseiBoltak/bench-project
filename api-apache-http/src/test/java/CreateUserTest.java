import com.google.inject.Inject;
import io.qameta.allure.Allure;
import model.requests.CreateUserRequest;
import model.responses.CreateUserResponse;
import org.example.config.ConfigLoader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

import static constants.Constants.USER_URI;
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
    public void cleanUp() throws IOException {
        cleaningUpCreatedUser(createdUserId);
    }

    private int createdUserId;

    @Test(dataProvider = "userDataProvider", description = "Checks user creation returns expected name/job")
    void checkUserCreatedWithExpectedNameAndJobTest(String name, String job) throws IOException {
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .name(name)
                .job(job)
                .build();

        CreateUserResponse createUserResponse = post(USER_URI, createUserRequest, 201, CreateUserResponse.class);

        createdUserId = createUserResponse.getId();
        Allure.step("Created user ID: " + createdUserId);

        assertEquals(createUserResponse.getJob(), createUserRequest.getJob(), "Job does not match");
        assertEquals(createUserResponse.getName(), createUserRequest.getName(), "Name does not match");
        assertNotNull(createUserResponse.getCreatedAt(), "Created at timestamp should not be null");
    }
}
