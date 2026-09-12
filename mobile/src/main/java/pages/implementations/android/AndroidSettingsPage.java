package pages.implementations.android;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.appium.AppiumSelectors;
import org.openqa.selenium.By;
import pages.interfaces.SettingsPage;

import static com.codeborne.selenide.appium.SelenideAppium.$;

/**
 * Android implementation of the cross-platform {@link SettingsPage} contract.
 * See {@link SettingsPage} for the rationale behind the interface + two-implementation pattern.
 */
public class AndroidSettingsPage implements SettingsPage {

    private final SelenideElement searchBarTitle = $(By.id("com.android.settings:id/search_bar_title"))
            .as("Settings Search Bar Title");
    private final SelenideElement menuTargetSetting = $(AppiumSelectors.withText("System"))
            .as("System Settings Option");

    @Override
    public SelenideElement searchBarTitle() {
        return searchBarTitle;
    }

    @Override
    public SelenideElement menuTargetSetting() {
        return menuTargetSetting;
    }

}