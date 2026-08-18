package actions;

import com.google.inject.Inject;
import factory.DriverProvider;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.SupportsSpecialEmulatorCommands;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;

public class DeviceActions {

    private final DriverProvider driverProvider;

    @Inject
    public DeviceActions(DriverProvider driverProvider) {
        this.driverProvider = driverProvider;
    }

    public boolean isAppInForeground(String appIdentifier) {
        AppiumDriver driver = driverProvider.getDriver();
        if (!(driver instanceof InteractsWithApps appDriver)) {
            throw new IllegalStateException("Driver does not support querying application state: "
                    + driver.getClass().getName());
        }
        return appDriver.queryAppState(appIdentifier) == ApplicationState.RUNNING_IN_FOREGROUND;
    }

    public void swipeUp() {
        performSwipe(0.8, 0.2);
    }

    public void swipeDown() {
        performSwipe(0.05, 0.6);
    }

    private void performSwipe(double startYRatio, double endYRatio) {
        AppiumDriver driver = driverProvider.getDriver();
        var size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * startYRatio);
        int endY = (int) (size.height * endYRatio);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(700), PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    public void sendSMS(String phoneNumber, String message) {
        AppiumDriver driver = driverProvider.getDriver();
        if (driver instanceof SupportsSpecialEmulatorCommands emulatorDriver) {
            emulatorDriver.sendSMS(phoneNumber, message);
        } else {
            throw new UnsupportedOperationException("Sending SMS is only supported on Android Emulators.");
        }
    }
    
}
