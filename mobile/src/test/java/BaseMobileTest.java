import com.google.inject.Inject;
import factory.DriverProvider;
import io.qameta.allure.Allure;
import org.example.config.ConfigLoader;
import org.example.guice.CoreModule;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;

@Guice(modules = {CoreModule.class})
public abstract class BaseMobileTest {

    protected final ConfigLoader configLoader;
    protected final DriverProvider driverProvider;

    @Inject
    public BaseMobileTest(ConfigLoader configLoader, DriverProvider driverProvider) {
        this.configLoader = configLoader;
        this.driverProvider = driverProvider;
    }

    @BeforeMethod
    public void setUp() {
        Allure.step("Starting mobile session for platform: " + configLoader.getPlatformName()
                + " on device: " + configLoader.getAppiumDeviceName());
        driverProvider.initDriver(configLoader);
    }

    @AfterMethod
    public void tearDown() {
        Allure.step("Closing mobile session");
        driverProvider.quitDriver();
    }
}