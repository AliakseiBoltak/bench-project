package pages.implementations.ios;

import actions.DeviceActions;
import com.google.inject.Inject;
import pages.BasePage;
import pages.interfaces.NotificationsPage;

public class IOSNotificationsPage extends BasePage implements NotificationsPage {

    @Inject
    public IOSNotificationsPage(DeviceActions deviceActions) {
        super(deviceActions);
    }

    @Override
    public boolean isNotificationWithTextVisible(String text) {
        // Temporarily do nothing for iOS
        return true;
    }

    @Override
    public void openNotifications() {
        // Temporarily do nothing for iOS
    }

    @Override
    public void clearAllNotifications() {
        // Temporarily do nothing for iOS
    }
}
