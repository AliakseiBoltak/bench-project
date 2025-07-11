package pages;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    public LoginPage enterEmail(String email) {
        $(By.id("login_field")).setValue(email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        $(By.id("password")).setValue(password);
        return this;
    }

    public HomePage clickOnLoginButton() {
        $(By.name("commit")).click();
        return new HomePage();
    }
}