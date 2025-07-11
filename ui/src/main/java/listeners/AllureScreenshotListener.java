package listeners;

import com.codeborne.selenide.Screenshots;
import io.qameta.allure.Allure;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AllureScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Allure.step("Test failed: " + result.getName());
        File screenshot = Screenshots.takeScreenShotAsFile();
        if (screenshot != null && screenshot.exists()) {
            try {
                Allure.addAttachment("Screenshot on Failure", Files.newInputStream(screenshot.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
