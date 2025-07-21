import com.google.inject.Inject;
import io.qameta.allure.Allure;
import org.example.config.ConfigLoader;
import org.example.model.User;
import org.example.service.UserDataService;
import pages.GitHubLoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GitHubLoginTest extends BaseTest {

    @Inject
    public GitHubLoginTest(ConfigLoader configLoader, UserDataService userDataService) {
        super(configLoader);
        this.userDataService = userDataService;
    }

    private final UserDataService userDataService;

    @Test(dataProvider = "allUserTypes")
    public void testInvalidGithubLoginShowsError(String userType) {
        User user = userDataService.getUserByType(userType);
        Allure.step("Perform login with user: " + userType);
        GitHubLoginPage loginPage = new GitHubLoginPage(page)
                .navigateToLogin(baseUrl + "/login")
                .enterUsername(user.getUsername())
                .enterPassword(user.getPassword())
                .clickSignIn();

        Assert.assertTrue(loginPage.isLoginErrorVisible(),
                "Login error alert should be visible for invalid credentials.");
    }

    @Test
    public void testContinueWithGoogleShowsGoogleSignIn() {
        GitHubLoginPage loginPage = new GitHubLoginPage(page)
                .navigateToLogin(baseUrl + "/login")
                .clickContinueWithGoogle();        Assert.assertTrue(loginPage.isGoogleSignInVisible(),
                "Google sign-in form should be visible.");
    }

}