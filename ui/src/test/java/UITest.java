import org.example.CoreLogic;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UITest {

    @Test
    public void test() {
        Assert.assertEquals("one", "one");
        CoreLogic coreLogic = new CoreLogic();
        coreLogic.printMessage("Hello from UI test!");
        coreLogic.printOS();
    }
}
