package steps.android;

import com.codeborne.selenide.Condition;
import com.google.inject.Inject;
import actions.DeviceActions;
import io.qameta.allure.Step;
import pages.implementations.android.AndroidSmsNotificationsPage;
import steps.BaseSteps;

/**
 * Business logic and Selenide assertions for the Android notification shade screen.
 * Tests call these methods instead of chaining Page-object calls and asserting
 * booleans, so failures report the exact element/locator via Selenide's logs.
 *
 * <p>This class is Android-specific by design (package {@code steps.android}), and depends
 * directly on the concrete {@link AndroidSmsNotificationsPage} rather than a shared interface -
 * SMS notifications are not supported on iOS via Appium, so there is no cross-platform contract
 * to depend on. See {@link AndroidSmsNotificationsPage} and the module README's "Page Objects"
 * section for the full rationale behind this exception to the default page-object pattern.
 */
public class AndroidSmsNotificationsSteps extends BaseSteps {

    private final AndroidSmsNotificationsPage androidSmsNotificationsPage;

    @Inject
    public AndroidSmsNotificationsSteps(DeviceActions deviceActions,
                                        AndroidSmsNotificationsPage androidSmsNotificationsPage) {
        super(deviceActions);
        this.androidSmsNotificationsPage = androidSmsNotificationsPage;
    }

    @Step("Opening notification shade to check for received SMS")
    public void openNotifications() {
        deviceActions.swipeDown();
    }

    @Step("Verifying SMS is received in the notifications shade")
    public void checkNotificationWithTextIsVisible(String text) {
        androidSmsNotificationsPage.notificationsContainer().shouldBe(Condition.visible);
        androidSmsNotificationsPage.notificationWithText(text).shouldBe(Condition.visible);
    }

    @Step("Cleaning up notifications by clicking Clear all")
    public void clearAllNotifications() {
        try {
            androidSmsNotificationsPage.clearAllButton().shouldBe(Condition.visible, Condition.enabled).click();
        } catch (Exception e) {
            // Notifications might already be cleared - not a hard failure for teardown.
        }
    }

}
