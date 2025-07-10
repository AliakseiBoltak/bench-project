package listeners;

import com.codeborne.selenide.Screenshots;
import io.qameta.allure.Allure;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AllureListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        Allure.step("Starting test: " + result.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        Allure.step("Finishing test: " + context.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Allure.step("Test passed: " + result.getName());
    }

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

    @Override
    public void onTestSkipped(ITestResult result) {
        Allure.step("Test skipped: " + result.getName());
    }

}
