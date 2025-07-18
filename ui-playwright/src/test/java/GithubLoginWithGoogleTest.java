import com.google.inject.Inject;
import org.example.config.ConfigLoader;
import pages.GitHubLoginPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GithubLoginWithGoogleTest extends BaseTest {

    @Inject
    public GithubLoginWithGoogleTest(ConfigLoader configLoader) {
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
    public void testGithubLoginWithGoogle(String username, String password) {
        GitHubLoginPage loginPage = new GitHubLoginPage(page)
                .navigateToLogin(baseUrl + "/login")
                .enterUsername(username)
                .enterPassword(password)
                .clickSignIn();

        // Assert alert for incorrect login is visible
        Assert.assertTrue(loginPage.isLoginErrorVisible());

        loginPage.clickContinueWithGoogle();

        // Assert Google sign-in is visible
        Assert.assertTrue(loginPage.isGoogleSignInVisible());
    }

}