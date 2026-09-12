package pages.implementations.android;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.appium.AppiumSelectors;
import org.openqa.selenium.By;

import static com.codeborne.selenide.appium.SelenideAppium.$;

/**
 * Android-only page object for the system notification shade, used to verify received SMS
 * notifications.
 *
 * <p>This screen is a deliberate <b>exception</b> to the module's default "one interface, two
 * implementations" pattern (see {@code pages.interfaces.SettingsPage} for the default pattern).
 * SMS/notification-shade automation via Appium is not supported on the iOS simulator at all, so
 * there is no cross-platform behavior to abstract - and thus no
 * {@code pages.interfaces.NotificationsPage} interface, no {@code IOSSmsNotificationsPage} stub,
 * and no {@code guice.DynamicPageProvider} binding for it. This class is injected directly (as a
 * concrete type) by Android-only steps/tests - see {@code steps.android.AndroidSmsNotificationsSteps}.
 *
 * <p>Rule of thumb for new pages: default to an interface + Android/iOS implementation pair
 * (assume identical behavior until proven otherwise). Only drop the interface, as done here, when
 * a feature is genuinely platform-exclusive or unsupported on the other platform. See the
 * "Page Objects" section of the module README for more details and other examples.
 */
public class AndroidSmsNotificationsPage {

    private final SelenideElement notificationsContainer = $(By.id("com.android.systemui:id/notifications_container"))
            .as("System Notifications Container");
    private final SelenideElement clearAllButton = $(AppiumSelectors.withText("Clear all"))
            .as("Clear all Button");

    public SelenideElement notificationsContainer() {
        return notificationsContainer;
    }

    public SelenideElement clearAllButton() {
        return clearAllButton;
    }

    public SelenideElement notificationWithText(String text) {
        return $(AppiumSelectors.withText(text)).as("Notification with text: " + text);
    }

}
