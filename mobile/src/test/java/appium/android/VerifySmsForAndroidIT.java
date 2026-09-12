package appium.android;

import actions.DeviceActions;
import appium.BaseMobileTest;
import com.google.inject.Inject;
import config.MobileConfigLoader;
import factory.AppiumDriverProvider;
import io.qameta.allure.Allure;
import model.SmsData;
import org.testng.annotations.*;
import org.example.loader.JSONDataLoader;
import steps.android.AndroidSmsNotificationsSteps;

import java.util.Arrays;

import static constants.Constants.SMS_DATA_PATH;

public class VerifySmsForAndroidIT extends BaseMobileTest {

    private final DeviceActions deviceActions;
    private final JSONDataLoader jsonDataLoader;
    private final AndroidSmsNotificationsSteps androidSmsNotificationsSteps;

    @Inject
    public VerifySmsForAndroidIT(MobileConfigLoader configLoader, AppiumDriverProvider driverProvider,
                                 DeviceActions deviceActions, JSONDataLoader jsonDataLoader,
                                 AndroidSmsNotificationsSteps androidSmsNotificationsSteps) {
        super(configLoader, driverProvider);
        this.deviceActions = deviceActions;
        this.jsonDataLoader = jsonDataLoader;
        this.androidSmsNotificationsSteps = androidSmsNotificationsSteps;
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
        androidSmsNotificationsSteps.openNotifications();
    }

    @Test(description = "Verify that Android emulator can receive SMS messages", dataProvider = "smsData")
    void verifyAndroidEmulatorReceivesSms(SmsData smsData) {
        String phoneNumber = smsData.getPhoneNumber();
        String messageText = smsData.getText();

        Allure.step(String.format("Sending SMS from %s with message: %s", phoneNumber, messageText));
        deviceActions.sendSMS(phoneNumber, messageText);

        androidSmsNotificationsSteps.checkNotificationWithTextIsVisible(messageText);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest() {
        androidSmsNotificationsSteps.clearAllNotifications();
    }

}
