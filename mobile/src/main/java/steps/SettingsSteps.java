package steps;

import com.codeborne.selenide.Condition;
import com.google.inject.Inject;
import actions.DeviceActions;
import io.qameta.allure.Step;
import pages.interfaces.SettingsPage;

/**
 * Business logic and Selenide assertions for the Settings app screen.
 */
public class SettingsSteps extends BaseSteps {

    private final SettingsPage settingsPage;

    @Inject
    public SettingsSteps(DeviceActions deviceActions, SettingsPage settingsPage) {
        super(deviceActions);
        this.settingsPage = settingsPage;
    }

    @Step("Checking if Search Settings element is visible")
    public void checkSearchSettingsInputIsVisible() {
        settingsPage.searchBarTitle().shouldBe(Condition.visible);
    }

    @Step("Swiping in settings menu")
    public void checkCanSwipeToSettingFromMenu() {
        swipeUpUntilVisible(settingsPage.menuTargetSetting());
    }

}
