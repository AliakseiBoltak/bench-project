import com.google.inject.Inject;
import io.qameta.allure.Allure;
import missions.LoginMissions;
import org.example.config.ConfigLoader;

import org.example.model.User;
import org.example.service.UserDataService;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;

public class LoginWithInvalidCredsTest extends BaseUiTest {

    @Inject
    public LoginWithInvalidCredsTest(UserDataService userDataJsonService, LoginMissions loginMissions,
                                     ConfigLoader configLoader) {
        super(configLoader);
        this.userDataJsonService = userDataJsonService;
        this.loginMissions = loginMissions;
    }

    private final UserDataService userDataJsonService;
    private final LoginMissions loginMissions;

    @Test(dataProvider = "allUserTypes")
    public void testGithubLoginWithInvalidCreds(String userType) {
        User user = userDataJsonService.getUserByType(userType);
        Allure.step("Perform login with user: " + userType);
        HomePage homePage = loginMissions
                .navigateToLoginPage(baseUrl)
                .loginWithCredentials(
                        user.getUsername(),
                        user.getPassword());
        Assert.assertFalse(homePage.checkIfHomePageLoaded(),
                "Login succeeded with invalid creds for user: " + userType);
    }
}