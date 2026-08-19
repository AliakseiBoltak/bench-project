---
name: run-mobile-tests
description: Sets up and runs the Appium/Android/iOS tests in the mobile module — installing/starting Appium, starting an emulator/simulator, matching env.conf to the actual device, configuring -Dplatform and suite XML parameters, and troubleshooting common session-start failures. Trigger on "run the mobile tests", "run appium test", "set up appium", "start android emulator for tests", "mobile test won't start".
---

# Run Mobile (Appium) Tests

The mobile module tests drive real/emulated Android and iOS devices through Appium. Unlike non-UI modules, an Appium server and a running mobile device/emulator must exist first. This module is excluded from CI until cloud device farm integration is complete.

## 1. Prerequisites (Android & iOS) (one-time setup)

- Node.js (Appium is an npm package) — v16+.
- Android SDK with platform-tools and emulator packages installed, and ANDROID_HOME pointing at the SDK install. Ensure platform-tools/ and emulator/ are in your system PATH.
- At least one Android Virtual Device (AVD) created via Android Studio's Device Manager, or avdmanager.
- Appium server and required drivers (e.g., npm install -g appium, appium driver install uiautomator2).

## 2. Start the Appium server

Run `appium` in your terminal. By default, this listens on http://127.0.0.1:4723 — matching platform-default.appium.serverUrl in mobile/src/test/resources/env.conf.

## 3. Start the emulator (or connect a device)

Run `emulator -avd <your_avd_name>` or start it from Android Studio's Device Manager. Wait until the device is fully booted. Verify ADB visibility with `adb devices`.

## 4. Match env.conf to your actual device

mobile/src/test/resources/env.conf uses a dual-layer structure separating Backend environments (-Denv) from Executable platforms (-Dplatform). Default platform fallback settings live in platform-default, while individual platforms (like android-16, android-17, ios-17) configure specific device names and versions.

## 5. Build core, then run the tests

Always build core first if any shared configuration classes changed:
mvn -f core/pom.xml clean install -q

Run tests using explicit platform profiles and suite files:
mvn -f mobile/pom.xml clean verify -Dplatform=android-17
mvn -f mobile/pom.xml clean verify -Dplatform=android-16 -DintegrationSuiteXmlFile=smoke-suite
mvn -f mobile/pom.xml clean verify -Denv=qa -Dplatform=android-17

## Troubleshooting

- ConfigException$Missing -> Ensure custom providers or classes (like PageModule) inject MobileConfigLoader instead of the base ConfigLoader.
- Could not find a connected Android device -> Run adb devices. If empty, the emulator is offline or not fully booted.
- Session-creation timeout or ADB reconnect loops -> Verify udid/deviceName in env.conf matches adb devices exactly.
- Guice/ErrorInCustomProvider -> Verify Guice modules properly bind MobileConfigLoader for mobile page providers.