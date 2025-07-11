package missions;

import com.google.inject.Inject;
import pages.HomePage;
import pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class LoginMissions {

    private final LoginPage loginPage;

    @Inject
    public LoginMissions(final LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    public LoginMissions navigateToLoginPage(String url) {
        open(url);
        return this;
    }

    public HomePage loginWithCredentials(String username, String password) {
        return loginPage.enterEmail(username)
                .enterPassword(password)
                .clickOnLoginButton();
    }
}