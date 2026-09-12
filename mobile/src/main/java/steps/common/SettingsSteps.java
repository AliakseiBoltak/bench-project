package steps.common;

import com.codeborne.selenide.Condition;
import com.google.inject.Inject;
import actions.DeviceActions;
import io.qameta.allure.Step;
import pages.interfaces.SettingsPage;
import steps.BaseSteps;

/**
 * Business logic and Selenide assertions for the Settings app screen.
 *
 * <p>Lives in {@code steps.common} (not {@code steps.android}/{@code steps.ios}) and depends on
 * the {@link SettingsPage} interface rather than a concrete implementation, because the Settings
 * screen's behavior is identical on both platforms - Guice resolves the correct
 * Android/iOS {@link SettingsPage} implementation at runtime. See {@link SettingsPage} for details.
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
