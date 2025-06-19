import com.google.inject.Inject;
import org.example.guice.TestModule;
import org.example.model.User;
import org.example.service.UserDataService;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

@Guice(modules = {TestModule.class})
public class UITest {

    @Inject
    public UITest(final UserDataService userDataService){
        this.userDataService = userDataService;
    }

    private UserDataService userDataService;
    private static final String TEST_DATA_PATH =  System.getProperty("user.dir") + "/src/test/resources/"
            + System.getProperty("env") + "/users.json";
    private static final String TEST_USER_TYPE =  "suspended";

    @Test
    public void test() {
        User user = userDataService.getTestUserByType(TEST_USER_TYPE, TEST_DATA_PATH);
        Assert.assertEquals(user.getUsername(), "charlie");
        Assert.assertEquals(user.getPassword(), "qwerty789");
        Assert.assertEquals(user.getUsertype(), TEST_USER_TYPE);
    }
}
