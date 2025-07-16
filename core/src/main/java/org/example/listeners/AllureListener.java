package org.example.listeners;

import io.qameta.allure.Allure;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

public class AllureListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        Allure.step("Test suite started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        Allure.step("Test suite finished: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        Allure.step("Test started: " + result.getName());
        Object[] parameters = result.getParameters();
        if (parameters != null && parameters.length > 0) {
            Allure.addAttachment("Test Parameters", Arrays.toString(parameters));
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Allure.step("Test passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Allure.step("Test failed: " + result.getName());
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            Allure.addAttachment("Error Stacktrace", throwable + "\n" +
                    Arrays.toString(throwable.getStackTrace()));
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Allure.step("Test skipped: " + result.getName());
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            Allure.addAttachment("Skip Reason", throwable.toString());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        Allure.step("Test failed but within success percentage: " + result.getName());
    }
}