package testng;

import com.google.inject.Inject;
import com.microsoft.playwright.*;
import testng.github.GitHubBaseTest;
import io.qameta.allure.Allure;
import org.example.config.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.github.GitHubLoginPage;

import java.io.ByteArrayInputStream;

import static constants.PathConstants.LOGIN_PATH;

public class OfflineErrorTest extends GitHubBaseTest {

    @Inject
    public OfflineErrorTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @Override
    protected boolean useStoredGitHubSession() {
        return false;
    }

    @Test
    public void testLoginPageDoesNotLoadWhenOffline() {
        context.setOffline(true);
        try {
            GitHubLoginPage gitHubLoginPage = new GitHubLoginPage(page).navigateToLogin(gitHubUrl + LOGIN_PATH);
            Assert.assertFalse(gitHubLoginPage.isLoginPageLoaded(), "Page should not load when offline");
        } catch (PlaywrightException ex) {
            Allure.addAttachment("Login Page is not loaded when offline:",
                    new ByteArrayInputStream(page.screenshot()));
        }
    }
}