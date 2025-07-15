import com.google.inject.Inject;
import constants.UserTypes;
import io.qameta.allure.Allure;
import missions.LoginMissions;
import org.example.config.ConfigLoader;
import org.example.guice.CoreModule;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import pages.HomePage;
import service.UserDataService;
import model.User;

import java.util.Arrays;

import static constants.Constants.USERS_TEST_DATA_PATH;

@Guice(modules = {CoreModule.class})
public class LoginTest extends BaseUiTest {

    @Inject
    public LoginTest(UserDataService userDataService, LoginMissions loginMissions, ConfigLoader configLoader) {
        super(configLoader);
        this.userDataService = userDataService;
        this.loginMissions = loginMissions;
    }

    private final UserDataService userDataService;
    private final LoginMissions loginMissions;

    @DataProvider
    public Object[][] allUserTypes() {
        return Arrays.stream(UserTypes.values())
                .map(type -> new Object[]{type.name().toLowerCase()})
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "allUserTypes",
            description = "Test login functionality with different user types")
    public void loginTest(String userType) {
        User user = userDataService.getUserByTypeFromJson(userType, USERS_TEST_DATA_PATH);
        Allure.step("Perform login with user: " + userType);
        HomePage homePage = loginMissions
                .navigateToLoginPage(baseUrl)
                .loginWithCredentials(
                        user.getUsername(),
                        user.getPassword());
        Assert.assertTrue(homePage.checkIfHomePageLoaded(), "Login failed!");
    }
}