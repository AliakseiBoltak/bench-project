package steps;

import com.codeborne.selenide.Condition;
import com.google.inject.Inject;
import actions.DeviceActions;
import io.qameta.allure.Step;
import pages.interfaces.NotificationsPage;

/**
 * Business logic and Selenide assertions for the notification shade screen.
 * Tests call these methods instead of chaining Page-object calls and asserting
 * booleans, so failures report the exact element/locator via Selenide's logs.
 */
public class NotificationsSteps extends BaseSteps {

    private final NotificationsPage notificationsPage;

    @Inject
    public NotificationsSteps(DeviceActions deviceActions, NotificationsPage notificationsPage) {
        super(deviceActions);
        this.notificationsPage = notificationsPage;
    }

    @Step("Opening notification shade to check for received SMS")
    public void openNotifications() {
        deviceActions.swipeDown();
    }

    @Step("Verifying SMS is received in the notifications shade")
    public void checkNotificationWithTextIsVisible(String text) {
        notificationsPage.notificationsContainer().shouldBe(Condition.visible);
        notificationsPage.notificationWithText(text).shouldBe(Condition.visible);
    }

    @Step("Cleaning up notifications by clicking Clear all")
    public void clearAllNotifications() {
        try {
            notificationsPage.clearAllButton().shouldBe(Condition.visible, Condition.enabled).click();
        } catch (Exception e) {
            // Notifications might already be cleared - not a hard failure for teardown.
        }
    }

}
