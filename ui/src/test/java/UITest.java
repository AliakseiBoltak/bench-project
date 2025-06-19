import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.example.guice.TestModule;
import org.example.model.User;
import org.example.service.UserDataService;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UITest {

    @Inject
    private UserDataService userDataService;

    private static final String TEST_DATA_PATH =  System.getProperty("user.dir") + "/src/test/resources/"
            + System.getProperty("env") + "/users.json";
    private static final String TEST_USER_TYPE =  "suspended";

    @Test
    public void test() {
        Injector injector = Guice.createInjector(new TestModule());
        UserDataService obj = injector.getInstance(UserDataService.class);
        User user = obj.getTestUserByType(TEST_USER_TYPE, TEST_DATA_PATH);
        Assert.assertEquals(user.getUsername(), "charlie");
        Assert.assertEquals(user.getPassword(), "qwerty789");
        Assert.assertEquals(user.getUsertype(), TEST_USER_TYPE);
    }
}
