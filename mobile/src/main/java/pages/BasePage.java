package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.google.inject.Inject;
import factory.DriverProvider;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.appmanagement.ApplicationState;

public class BasePage {

    protected DriverProvider driverProvider;

    @Inject
    public BasePage(DriverProvider driverProvider) {
        this.driverProvider = driverProvider;
    }

    protected AppiumDriver getDriver() {
        return driverProvider.getDriver();
    }

    protected boolean isAppInForeground (String appIdentifier){
        InteractsWithApps appDriver = (InteractsWithApps) getDriver();
        return appDriver.queryAppState(appIdentifier) == ApplicationState.RUNNING_IN_FOREGROUND;
    }

    protected boolean isElementVisible(SelenideElement element) {
        try {
            element.shouldBe(Condition.visible);
            return true;
        } catch (Error | Exception e) {
            return false;
        }
    }

    protected void waitForElementVisible(SelenideElement element) {
        element.shouldBe(Condition.visible);
    }

    protected void clickElement(SelenideElement element) {
        element.shouldBe(Condition.visible, Condition.enabled).click();
    }

    protected void typeText(SelenideElement element, String text) {
        element.shouldBe(Condition.visible).setValue(text);
    }
}