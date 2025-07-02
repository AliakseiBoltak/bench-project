import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.guice.TestModule;
import org.example.model.User;
import org.example.service.UserDataService;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.example.constants.Constants.TEST_DATA_PATH;

@Guice(modules = {TestModule.class})
public class UITest {

    @Inject
    public UITest(final UserDataService userDataService){
        this.userDataService = userDataService;
    }

    private User user;
    private final UserDataService userDataService;
    private static final String TEST_USER_TYPE =  "suspended";
    private static final Logger LOGGER = LogManager.getLogger(UITest.class);


    @BeforeClass
    public void loginTestSetUp() {
        LOGGER.info("Get user for testing...");
        user = userDataService.getTestUserByType(TEST_USER_TYPE, TEST_DATA_PATH);
    }

    @Test
    public void loginTest() {
        LOGGER.info("Starting UI test....");
        open("https://github.com/login");
        $(By.id("login_field")).setValue(user.getUsername());
        $(By.id("password")).setValue(user.getPassword());
        $(By.name("commit")).click();
//        Assert.assertTrue(
//                $(By.xpath("//h2[text()='Home']")).isDisplayed(),
//                "Login failed! Expected to see 'Home' header after login, but it was not found."
//        );
        LOGGER.info("Finishing UI test....");
    }
}
