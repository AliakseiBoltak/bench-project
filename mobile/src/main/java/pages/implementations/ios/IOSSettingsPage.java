package pages.implementations.ios;

import actions.DeviceActions;
import com.codeborne.selenide.SelenideElement;
import com.google.inject.Inject;
import org.openqa.selenium.By;
import pages.BasePage;
import pages.interfaces.SettingsPage;

import static com.codeborne.selenide.appium.SelenideAppium.$;

public class IOSSettingsPage extends BasePage implements SettingsPage {

    private final SelenideElement settingsSearchBarTitle = $(By.className("XCUIElementTypeSearchField"))
            .as("Settings Search Bar Title");
    private final SelenideElement developerSettingsOption = $(By.id("Developer"))
            .as("Developer Settings Option");

    @Inject
    public IOSSettingsPage(DeviceActions deviceActions) {
        super(deviceActions);
    }

    @Override
    public boolean isSearchSettingsInputVisible() {
        waitForElementVisible(settingsSearchBarTitle);
        return true;
    }

    @Override
    public boolean canSwipeToSettingFromMenuUntilItIsVisible() {
        swipeUpUntilVisible(developerSettingsOption);
        return true;
    }

}
