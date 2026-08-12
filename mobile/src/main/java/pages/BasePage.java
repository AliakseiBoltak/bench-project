package pages;

import factory.DriverProvider;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final DriverProvider driverProvider;

    protected BasePage(DriverProvider driverProvider) {
        this.driverProvider = driverProvider;
    }

    protected AppiumDriver getDriver() {
        return driverProvider.getDriver();
    }

    protected WebElement waitForElementVisible(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForElementVisible(By locator) {
        return waitForElementVisible(locator, 5);
    }

    protected boolean isElementVisible(By locator, int timeoutSeconds) {
        try {
            waitForElementVisible(locator, timeoutSeconds);
            return true;
        } catch (TimeoutException | org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    protected boolean isElementVisible(By locator) {
        return isElementVisible(locator, 5);
    }

    protected void click(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    protected void click(By locator) {
        click(locator, 5);
    }
}