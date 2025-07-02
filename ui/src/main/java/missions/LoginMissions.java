package missions;

import com.google.inject.Inject;
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

    public LoginMissions loginWithCredentials(String username, String password) {
        loginPage.enterEmail(username)
                .enterPassword(password)
                .clickOnLoginButton();
        return this;
    }
}