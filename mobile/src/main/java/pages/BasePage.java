package pages;

import actions.DeviceActions;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.google.inject.Inject;

public abstract class BasePage {

    protected DeviceActions deviceActions;
    protected static final int DEFAULT_MAX_SWIPES = 5;

    @Inject
    protected BasePage(DeviceActions deviceActions) {
        this.deviceActions = deviceActions;
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
        element.shouldBe(Condition.visible, Condition.enabled).setValue(text);
    }

    protected void swipeUpUntilVisible(SelenideElement element) {
        int swipes = 0;
        while (!isElementVisible(element) && swipes < DEFAULT_MAX_SWIPES) {
            deviceActions.swipeUp();
            swipes++;
        }
        // Wait for the element to be visible after swiping
        waitForElementVisible(element);
    }

}