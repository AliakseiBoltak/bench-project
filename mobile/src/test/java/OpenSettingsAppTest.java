import com.google.inject.Inject;
import io.qameta.allure.Allure;
import org.example.config.ConfigLoader;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

class OpenSettingsAppTest extends BaseMobileTest {

    @Inject
    public OpenSettingsAppTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @Test(description = "Checks the Android Settings app launches and is the foreground app")
    void checkSettingsAppLaunchedTest() {
        String currentPackage = driver.getCurrentPackage();
        Allure.step("Current foreground package: " + currentPackage);

        assertEquals(currentPackage, configLoader.getAppiumAppPackage(),
                "Settings app was not launched as the foreground app");
    }
}
