import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.guice.TestModule;
import org.example.model.User;
import org.example.service.UserDataService;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import static org.example.constants.Constants.TEST_DATA_PATH;

@Guice(modules = {TestModule.class})
public class UITest {

    @Inject
    public UITest(final UserDataService userDataService){
        this.userDataService = userDataService;
    }

    private final UserDataService userDataService;
    private static final String TEST_USER_TYPE =  "suspended";
    private static final Logger LOGGER = LogManager.getLogger(UITest.class);

    @Test
    public void test() {
        LOGGER.info("Starting UI test....");
        User user = userDataService.getTestUserByType(TEST_USER_TYPE, TEST_DATA_PATH);
        Assert.assertEquals(user.getUsername(), "charlie");
        Assert.assertEquals(user.getPassword(), "");
        Assert.assertEquals(user.getUsertype(), TEST_USER_TYPE);
        LOGGER.info("Finishing UI test....");
    }
}
