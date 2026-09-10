package steps;

import actions.DeviceActions;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.google.inject.Inject;

/**
 * Common gesture helpers shared across Step classes.
 * Step classes hold the business logic and Selenide assertions for a screen;
 * Page classes underneath them expose only raw {@link SelenideElement}s.
 */
public abstract class BaseSteps {

    protected static final int DEFAULT_MAX_SWIPES = 5;

    protected final DeviceActions deviceActions;

    @Inject
    protected BaseSteps(DeviceActions deviceActions) {
        this.deviceActions = deviceActions;
    }

    /**
     * Swipes up until the element becomes visible (or the swipe budget is exhausted),
     * then asserts visibility with a Selenide condition so failures report the exact
     * element and locator that could not be found.
     */
    protected void swipeUpUntilVisible(SelenideElement element) {
        int swipes = 0;
        while (!element.is(Condition.visible) && swipes < DEFAULT_MAX_SWIPES) {
            deviceActions.swipeUp();
            swipes++;
        }
        element.shouldBe(Condition.visible);
    }

}

