package pages.implementations.ios;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import pages.interfaces.SettingsPage;

import static com.codeborne.selenide.appium.SelenideAppium.$;

/**
 * iOS implementation of the cross-platform {@link SettingsPage} contract.
 * See {@link SettingsPage} for the rationale behind the interface + two-implementation pattern.
 */
public class IOSSettingsPage implements SettingsPage {

    private final SelenideElement searchBarTitle = $(By.className("XCUIElementTypeSearchField"))
            .as("Settings Search Bar Title");
    private final SelenideElement menuTargetSetting = $(By.id("Developer"))
            .as("Developer Settings Option");

    @Override
    public SelenideElement searchBarTitle() {
        return searchBarTitle;
    }

    @Override
    public SelenideElement menuTargetSetting() {
        return menuTargetSetting;
    }

}
