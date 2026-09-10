package pages.implementations.android;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.appium.AppiumSelectors;
import org.openqa.selenium.By;
import pages.interfaces.NotificationsPage;

import static com.codeborne.selenide.appium.SelenideAppium.$;

public class AndroidNotificationsPage implements NotificationsPage {

    private final SelenideElement notificationsContainer = $(By.id("com.android.systemui:id/notifications_container"))
            .as("System Notifications Container");
    private final SelenideElement clearAllButton = $(AppiumSelectors.withText("Clear all"))
            .as("Clear all Button");

    @Override
    public SelenideElement notificationsContainer() {
        return notificationsContainer;
    }

    @Override
    public SelenideElement clearAllButton() {
        return clearAllButton;
    }

    @Override
    public SelenideElement notificationWithText(String text) {
        return $(AppiumSelectors.withText(text)).as("Notification with text: " + text);
    }

}
