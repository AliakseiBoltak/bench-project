package pages.implementations.android;

import actions.DeviceActions;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.appium.AppiumSelectors;
import com.google.inject.Inject;
import org.openqa.selenium.By;
import pages.BasePage;
import pages.interfaces.NotificationsPage;

import static com.codeborne.selenide.Selenide.$;

public class AndroidNotificationsPage extends BasePage implements NotificationsPage {

    private SelenideElement notificationContainer = $(By.id("com.android.systemui:id/notifications_container"))
            .as("System Notifications Container");
    private SelenideElement clearAllNotificationsButton = $(AppiumSelectors.withText("Clear all"))
            .as("Clear all Button");

    @Inject
    public AndroidNotificationsPage(DeviceActions deviceActions) {
        super(deviceActions);
    }

    @Override
    public boolean isNotificationWithTextVisible(String text) {
        waitForElementVisible(notificationContainer);
        SelenideElement notificationText = $(AppiumSelectors.withText(text));
        return isElementVisible(notificationText);
    }

    @Override
    public void openNotifications() {
        deviceActions.swipeDown();
    }

    @Override
    public void clearAllNotifications() {
        clickElement(clearAllNotificationsButton);
    }

}
