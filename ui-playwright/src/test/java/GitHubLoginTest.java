import com.google.inject.Inject;
import org.example.config.ConfigLoader;
import pages.GitHubLoginPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GitHubLoginTest extends BaseTest {

    @Inject
    public GitHubLoginTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @DataProvider(name = "loginCredentials")
    public Object[][] loginCredentials() {
        return new Object[][]{
                {"testuser", "wrongpass"},
                {"user123", "pass123"},
        };
    }

    @Test(dataProvider = "loginCredentials")
    public void testInvalidGithubLoginShowsError(String username, String password) {
        GitHubLoginPage loginPage = new GitHubLoginPage(page)
                .navigateToLogin(baseUrl + "/login")
                .enterUsername(username)
                .enterPassword(password)
                .clickSignIn();

        Assert.assertTrue(loginPage.isLoginErrorVisible(),
                "Login error alert should be visible for invalid credentials.");
    }

    @Test
    public void testContinueWithGoogleShowsGoogleSignIn() {
        GitHubLoginPage loginPage = new GitHubLoginPage(page)
                .navigateToLogin(baseUrl + "/login")
                .clickContinueWithGoogle();
        Assert.assertTrue(loginPage.isGoogleSignInVisible(),
                "Google sign-in form should be visible.");
    }

}