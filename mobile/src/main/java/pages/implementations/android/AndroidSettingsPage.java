package pages.implementations.android;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.appium.AppiumSelectors;
import com.google.inject.Inject;
import factory.DriverProvider;
import org.openqa.selenium.By;
import pages.BasePage;
import pages.interfaces.SettingsPage;

import static com.codeborne.selenide.Selenide.$;

public class AndroidSettingsPage extends BasePage implements SettingsPage {

    private SelenideElement settingsSearchBarTitle = $(By.id("com.android.settings:id/search_bar_title"))
            .as("Settings Search Bar Title");
    private SelenideElement settingsSearchBarText = $(AppiumSelectors.withText("Search Settings"))
            .as("Settings Search Bar Text");
    private SelenideElement systemSettingsOption = $(AppiumSelectors.withText("System"))
            .as("System Settings Option");



    @Inject
    public AndroidSettingsPage(DriverProvider driverProvider) {
        super(driverProvider);
    }

    @Override
    public boolean isSearchSettingsVisible() {
        waitForElementVisible(settingsSearchBarTitle);
        waitForElementVisible(settingsSearchBarText);
        return true;
    }

    @Override
    public void swipeToSystemSettings() {
        swipeUpUntilVisible(systemSettingsOption);
    }

    @Override
    public boolean isSystemSettingsVisible() {
        return isElementVisible(systemSettingsOption);
    }

}