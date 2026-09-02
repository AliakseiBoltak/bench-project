package appium.android;

import actions.DeviceActions;
import appium.BaseMobileTest;
import com.google.inject.Inject;
import config.MobileConfigLoader;
import factory.DriverProvider;
import io.qameta.allure.Allure;
import lombok.extern.log4j.Log4j2;
import model.SmsData;
import org.testng.annotations.*;
import org.example.loader.JSONDataLoader;
import pages.interfaces.NotificationsPage;

import java.util.Arrays;

import static constants.Constants.SMS_DATA_PATH;
import static org.testng.Assert.assertTrue;

@Log4j2
public class VerifySmsForAndroidIT extends BaseMobileTest {

    private final DeviceActions deviceActions;
    private final JSONDataLoader jsonDataLoader;
    private final NotificationsPage notificationsPage;

    @Inject
    public VerifySmsForAndroidIT(MobileConfigLoader configLoader, DriverProvider driverProvider,
                                 DeviceActions deviceActions, JSONDataLoader jsonDataLoader,
                                 NotificationsPage notificationsPage) {
        super(configLoader, driverProvider);
        this.deviceActions = deviceActions;
        this.jsonDataLoader = jsonDataLoader;
        this.notificationsPage = notificationsPage;
    }

    @DataProvider(name = "smsData")
    public Object[][] getSmsData() {
        SmsData[] smsDataArray = jsonDataLoader.getData(SMS_DATA_PATH, SmsData[].class);
        return Arrays.stream(smsDataArray)
                .map(sms -> new Object[]{sms})
                .toArray(Object[][]::new);
    }

    @BeforeMethod
    public void setupTest() {
        Allure.step("Opening notification shade to check for received SMS");
        notificationsPage.openNotifications();
    }

    @Test(description = "Verify that Android emulator can receive SMS messages", dataProvider = "smsData")
    void verifyAndroidEmulatorReceivesSms(SmsData smsData) {
        String phoneNumber = smsData.getPhoneNumber();
        String messageText = smsData.getText();

        Allure.step(String.format("Sending SMS from %s with message: %s", phoneNumber, messageText));

        // Note: For this to work, the test must be running on Android Emulator.
        deviceActions.sendSMS(phoneNumber, messageText);

        Allure.step("Verifying SMS is received in the notifications shade");

        boolean isReceived = notificationsPage.isNotificationWithTextVisible(messageText);
        assertTrue(isReceived, "SMS text was not visible in the notifications shade");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest() {
        Allure.step("Cleaning up notifications by clicking Clear all");
        try {
            notificationsPage.clearAllNotifications();
        } catch (Exception e) {
            log.warn("Failed to clear notifications. It might be already cleared.", e);
        }
    }

}
