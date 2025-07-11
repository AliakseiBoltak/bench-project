package org.example.listeners;

import io.qameta.allure.Allure;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        Allure.step("Test started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Allure.step("Test passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Allure.step("Test failed: " + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Allure.step("Test skipped: " + result.getName());
    }
}