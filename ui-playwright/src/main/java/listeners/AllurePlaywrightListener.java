package listeners;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class AllurePlaywrightListener implements ITestListener {

    public static ThreadLocal<Page> pageHolder = new ThreadLocal<>();

    public static void setPage(Page page) {
        pageHolder.set(page);
    }

    public static void removePage() {
        pageHolder.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Page page = pageHolder.get();
        if (page != null) {
            byte[] screenshot = page.screenshot();
            Allure.addAttachment("Screenshot on failure", new ByteArrayInputStream(screenshot));
        }
    }
}