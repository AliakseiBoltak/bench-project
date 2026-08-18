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

    private SelenideElement searchBarTitle = $(By.id("com.android.settings:id/search_bar_title"))
            .as("Search Bar Title");
    private SelenideElement searchBarText = $(AppiumSelectors.withText("Search Settings"))
            .as("Search Bar Text");

    @Inject
    public AndroidSettingsPage(DriverProvider driverProvider) {
        super(driverProvider);
    }

    @Override
    public boolean isSearchSettingsVisible() {
        waitForElementVisible(searchBarTitle);
        waitForElementVisible(searchBarText);
        return true;
    }

}