import com.google.inject.Inject;
import io.qameta.allure.Allure;
import missions.LoginMissions;
import org.example.config.ConfigLoader;

import org.example.model.User;
import org.example.service.UserDataService;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;

public class LoginTest extends BaseUiTest {

    @Inject
    public LoginTest(UserDataService userDataService, LoginMissions loginMissions, ConfigLoader configLoader) {
        super(configLoader);
        this.userDataService = userDataService;
        this.loginMissions = loginMissions;
    }

    private final UserDataService userDataService;
    private final LoginMissions loginMissions;

    @Test(dataProvider = "allUserTypes",
            description = "Test login functionality with different user types")
    public void loginTest(String userType) {
        User user = userDataService.getUserByType(userType);
        Allure.step("Perform login with user: " + userType);
        HomePage homePage = loginMissions
                .navigateToLoginPage(baseUrl)
                .loginWithCredentials(
                        user.getUsername(),
                        user.getPassword());
        Assert.assertTrue(homePage.checkIfHomePageLoaded(), "Login failed with user: " + userType);
    }
}