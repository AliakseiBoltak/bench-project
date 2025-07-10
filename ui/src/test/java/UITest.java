import com.google.inject.Inject;
import io.qameta.allure.Allure;
import missions.LoginMissions;
import org.example.guice.TestModule;
import org.example.model.User;
import org.example.service.UserDataService;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import pages.HomePage;

import static org.example.constants.Constants.TEST_DATA_PATH;

@Guice(modules = {TestModule.class})
public class UITest extends BaseUiTest {

    @Inject
    public UITest(final UserDataService userDataService, final LoginMissions loginMissions,
                  final HomePage homePage) {
        this.userDataService = userDataService;
        this.loginMissions = loginMissions;
        this.homePage = homePage;
    }

    private User user;
    private final UserDataService userDataService;
    private final LoginMissions loginMissions;
    private final HomePage homePage;
    private static final String TEST_USER_TYPE = "suspended";

    @BeforeClass
    public void loginTestSetUp() {
        Allure.step("Get User For Testing: " + TEST_USER_TYPE);
        user = userDataService.getTestUserByType(TEST_USER_TYPE, TEST_DATA_PATH);
    }

    @Test
    public void loginTest() {
        loginMissions
                .navigateToLoginPage(BaseUiTest.BASE_URL + "/login")
                .loginWithCredentials(
                        user.getUsername(),
                        user.getPassword());
        Assert.assertTrue(homePage.checkIfHomePageLoaded(), "Login failed!");
    }
}