package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.google.inject.Inject;
import factory.DriverProvider;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import java.time.Duration;
import java.util.Collections;

public abstract class BasePage {

    protected DriverProvider driverProvider;
    protected static final int DEFAULT_MAX_SWIPES = 5;

    @Inject
    public BasePage(DriverProvider driverProvider) {
        this.driverProvider = driverProvider;
    }

    protected AppiumDriver getDriver() {
        return driverProvider.getDriver();
    }


    protected boolean isElementVisible(SelenideElement element) {
        try {
            element.shouldBe(Condition.visible);
            return true;
        } catch (Error | Exception e) {
            return false;
        }
    }

    protected void waitForElementVisible(SelenideElement element) {
        element.shouldBe(Condition.visible);
    }

    protected void clickElement(SelenideElement element) {
        element.shouldBe(Condition.visible, Condition.enabled).click();
    }

    protected void typeText(SelenideElement element, String text) {
        element.shouldBe(Condition.visible).setValue(text);
    }

    protected void swipeUpUntilVisible(SelenideElement element) {
        int swipes = 0;
        AppiumDriver driver = getDriver();
        while (!isElementVisible(element) && swipes < DEFAULT_MAX_SWIPES) {
            // Getting the size of the screen to calculate swipe coordinates
            var size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.8); // swipe from the bottom (from 80% of screen height)
            int endY = (int) (size.height * 0.2);   // swipe to the top (to 20% of screen height)

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);
            swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(700), PointerInput.Origin.viewport(), startX, endY));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(swipe));
            swipes++;
        }

        // Wait for the element to be visible after swiping
        waitForElementVisible(element);
    }

}