package listeners;

import com.codeborne.selenide.Screenshots;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AllureSelenideListener implements ITestListener {

    private static final Logger LOGGER = LogManager.getLogger(AllureSelenideListener.class);

    @Override
    public void onTestFailure(ITestResult result) {
        Allure.step("Test failed: " + result.getName());
        File screenshot = Screenshots.takeScreenShotAsFile();
        if (screenshot != null && screenshot.exists()) {
            try {
                Allure.addAttachment("Screenshot on Failure", Files.newInputStream(screenshot.toPath()));
            } catch (IOException e) {
                LOGGER.error("Failed to attach screenshot", e);
                e.printStackTrace();
            }
        }
    }

}