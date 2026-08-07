import com.google.inject.Inject;
import factory.AndroidDriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.example.config.ConfigLoader;
import org.example.guice.CoreModule;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;

@Guice(modules = {CoreModule.class})
public abstract class BaseMobileTest {

    protected final ConfigLoader configLoader;
    protected AndroidDriver driver;

    @Inject
    public BaseMobileTest(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    @BeforeMethod
    public void setUp() {
        Allure.step("Starting Android session on device: " + configLoader.getAppiumDeviceName());
        driver = AndroidDriverFactory.createDriver(configLoader);
    }

    @AfterMethod
    public void tearDown() {
        Allure.step("Closing Android session");
        if (driver != null) {
            driver.quit();
        }
    }
}
