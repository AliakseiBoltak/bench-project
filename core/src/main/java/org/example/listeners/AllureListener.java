package org.example.listeners;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

public class AllureListener implements ITestListener {

    private static final Logger LOGGER = LogManager.getLogger(AllureListener.class);

    @Override
    public void onStart(ITestContext context) {
        // Suite-level callbacks run outside any test case, so Allure.step() would fail
        // with "no test case running" — log these instead.
        LOGGER.info("Test suite started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        LOGGER.info("Test suite finished: {}", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        LOGGER.info("Test started: {}", result.getName());
        Allure.step("Test started: " + result.getName());
        Object[] parameters = result.getParameters();
        if (parameters != null && parameters.length > 0) {
            Allure.addAttachment("Test Parameters", Arrays.toString(parameters));
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOGGER.info("Test passed: {}", result.getName());
        Allure.step("Test passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOGGER.error("Test failed: {}", result.getName());
        Allure.step("Test failed: " + result.getName());
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            Allure.addAttachment("Error Stacktrace", throwable + "\n" +
                    Arrays.toString(throwable.getStackTrace()));
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warn("Test skipped: {}", result.getName());
        Allure.step("Test skipped: " + result.getName());
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            Allure.addAttachment("Skip Reason", throwable.toString());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        LOGGER.warn("Test failed but within success percentage: {}", result.getName());
        Allure.step("Test failed but within success percentage: " + result.getName());
    }
}