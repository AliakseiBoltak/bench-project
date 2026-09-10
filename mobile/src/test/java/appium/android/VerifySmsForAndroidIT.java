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
import steps.NotificationsSteps;

import java.util.Arrays;

import static constants.Constants.SMS_DATA_PATH;

@Log4j2
public class VerifySmsForAndroidIT extends BaseMobileTest {

    private final DeviceActions deviceActions;
    private final JSONDataLoader jsonDataLoader;
    private final NotificationsSteps notificationsSteps;

    @Inject
    public VerifySmsForAndroidIT(MobileConfigLoader configLoader, DriverProvider driverProvider,
                                 DeviceActions deviceActions, JSONDataLoader jsonDataLoader,
                                 NotificationsSteps notificationsSteps) {
        super(configLoader, driverProvider);
        this.deviceActions = deviceActions;
        this.jsonDataLoader = jsonDataLoader;
        this.notificationsSteps = notificationsSteps;
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
        notificationsSteps.openNotifications();
    }

    @Test(description = "Verify that Android emulator can receive SMS messages", dataProvider = "smsData")
    void verifyAndroidEmulatorReceivesSms(SmsData smsData) {
        String phoneNumber = smsData.getPhoneNumber();
        String messageText = smsData.getText();

        Allure.step(String.format("Sending SMS from %s with message: %s", phoneNumber, messageText));

        // Note: For this to work, the test must be running on Android Emulator.
        deviceActions.sendSMS(phoneNumber, messageText);

        notificationsSteps.checkNotificationWithTextIsVisible(messageText);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest() {
        notificationsSteps.clearAllNotifications();
    }

}
