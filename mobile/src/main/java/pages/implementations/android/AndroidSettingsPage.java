package pages.implementations.android;

import actions.DeviceActions;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.appium.AppiumSelectors;
import com.google.inject.Inject;
import org.openqa.selenium.By;
import pages.BasePage;
import pages.interfaces.SettingsPage;

import static com.codeborne.selenide.appium.SelenideAppium.$;

public class AndroidSettingsPage extends BasePage implements SettingsPage {

    private SelenideElement settingsSearchBarTitle = $(By.id("com.android.settings:id/search_bar_title"))
            .as("Settings Search Bar Title");
    private SelenideElement systemSettingsOption = $(AppiumSelectors.withText("System"))
            .as("System Settings Option");

    @Inject
    public AndroidSettingsPage(DeviceActions deviceActions) {
        super(deviceActions);
    }

    @Override
    public boolean isSearchSettingsInputVisible() {
        waitForElementVisible(settingsSearchBarTitle);
        return true;
    }

    @Override
    public boolean canSwipeToSettingFromMenuUntilItIsVisible() {
        swipeUpUntilVisible(systemSettingsOption);
        return true;
    }

}