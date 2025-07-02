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

    public LoginMissions loginWithCredentials(String url, String username, String password) {
        open(url);
        loginPage.enterEmail(username)
                .enterPassword(password)
                .clickOnLoginButton();
        return this;
    }
}