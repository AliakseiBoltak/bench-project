package pages.implementations.ios;

import actions.DeviceActions;
import com.google.inject.Inject;
import factory.DriverProvider;
import pages.BasePage;
import pages.interfaces.NotificationsPage;

public class IOSNotificationsPage extends BasePage implements NotificationsPage {

    @Inject
    public IOSNotificationsPage(DriverProvider driverProvider, DeviceActions deviceActions) {
        super(driverProvider, deviceActions);
    }

    @Override
    public boolean isNotificationWithTextVisible(String text) {
        // Adjust when iOS notification locators are available.
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
