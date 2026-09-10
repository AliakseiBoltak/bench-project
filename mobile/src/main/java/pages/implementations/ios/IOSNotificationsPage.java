package pages.implementations.ios;

import com.codeborne.selenide.SelenideElement;
import pages.interfaces.NotificationsPage;

public class IOSNotificationsPage implements NotificationsPage {

    //iOS notification Page is currently not supported.
    // This provides a minimal and clean dummy class structure.

    @Override
    public SelenideElement notificationsContainer() {
        return null;
    }

    @Override
    public SelenideElement clearAllButton() {
        return null;
    }

    @Override
    public SelenideElement notificationWithText(String text) {
        return null;
    }

}
