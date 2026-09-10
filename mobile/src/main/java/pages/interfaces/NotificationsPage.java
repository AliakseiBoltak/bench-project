package pages.interfaces;

import com.codeborne.selenide.SelenideElement;

public interface NotificationsPage {

    SelenideElement notificationsContainer();

    SelenideElement clearAllButton();

    SelenideElement notificationWithText(String text);

}
