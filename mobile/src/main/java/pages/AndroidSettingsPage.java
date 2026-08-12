package pages;

import com.google.inject.Inject;
import factory.DriverProvider;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AndroidSettingsPage implements SettingsPage {

    private final DriverProvider driverProvider;
    private final By searchSettingsTitle = By.id("com.android.settings:id/search_bar_title");

    @Inject
    public AndroidSettingsPage(DriverProvider driverProvider) {
        this.driverProvider = driverProvider;
    }

    @Override
    public boolean isAppInForeground(String appIdentifier) {
        InteractsWithApps driver = (InteractsWithApps) driverProvider.getDriver();
        return driver.queryAppState(appIdentifier) == ApplicationState.RUNNING_IN_FOREGROUND;
    }

    @Override
    public boolean isSearchSettingsVisible() {
        try {
            WebElement element = new WebDriverWait(driverProvider.getDriver(),
                    Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(searchSettingsTitle));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
