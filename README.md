## How to Build and Run Tests

> **Before running any tests, you must first build and install the core module artifact.**
>
> 1. Import as a Maven project in your IDE.
> 2. Navigate to the `core` module directory:
>    ```sh
>    cd core
>    ```
> 3. Build and install the core artifact to your local Maven repository by running this command:
>    ```sh
>    mvn clean install
>    ```
>
> After this, you can navigate to the desired module (for example, `api`, `ui-playwright`, etc.) and execute the tests as described below.

### 1. Run Tests

```sh
mvn clean test
```

---

### 2. Restore Allure History from Previous Runs

To enable test trend statistics (history) in Allure reports, restore the history from your previous report using:

```sh
mvn antrun:run@restore-allure-history
```

---

### 3. Generate Allure Report

```sh
mvn allure:report
```

---

### 4. Save Allure History for Future Runs

After generating the report, save the current run's Allure history so trends will persist between runs:

```sh
mvn antrun:run@copy-allure-history
```

---

### 5. Open Allure Report

```sh
mvn allure:serve
```

---

> **Note:**  
> The Allure history steps (restore/save) are only required if you want to see trends/statistics across runs.  
> If you don’t need trends, you can skip steps 2 and 4.

---

## Notes

- **Allure Trends:**  
  To keep test trends and history visible in Allure reports across runs, always restore history before running tests and save history after generating the report.

- **Database Tests:**  
  Before running tests in the `db` module, execute the script `init_test_db.sql` to create the database and tables.

- **UI Playwright Tests:**  
  Before running tests in the `ui-playwright` module, you must install Playwright browsers by running:
  ```sh
  mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
  ```
  To run Playwright tests that require GitHub authentication (such as tests that use a stored session for GitHub),  
  you must update your `env.conf` file with valid GitHub credentials.

  After running your tests, you may have a Playwright trace file (for example, `github-login-trace.zip`).  
  To view and analyze this traced session in your browser, run the following command:

  ```sh
  mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace traces/github-login-trace.zip"
  ```
  This will open the Playwright Trace Viewer, allowing you to inspect every step of your test, including screenshots, network, console, and more.

- **Adjust Browser/Headless Mode:**  
  To change the default browser or headless mode for UI tests, pass JVM parameters when running tests. For example:
  ```sh
  mvn clean test -Dheadless=false -Dbrowser=firefox
  ```
  This will run the UI tests in headed mode using Firefox instead of the default headless Chromium.

- ```markdown
- **Mobile (Appium/Android/iOS) Tests:**  
  The `mobile` module drives a real Android device/emulator or iOS simulator via Appium and needs some one-time setup before running the tests will work:

    1. Install Node.js, then Appium and its UiAutomator2 driver:
       ```sh
       npm install -g appium
       appium driver install uiautomator2
       # For iOS testing on macOS, XCUITest driver is also required:
       # appium driver install xcuitest
       ```
    2. Start the Appium server (leave it running in its own terminal):
       ```sh
       appium
       ```
       By default it listens on `http://127.0.0.1:4723`, matching `appium.serverUrl` in `mobile/src/test/resources/env.conf`.
    3. Start an Android emulator (created via Android Studio's Device Manager or `avdmanager`) or connect a physical device with USB debugging enabled:
       ```sh
       emulator -avd <your_avd_name>          # list AVDs with: emulator -list-avds
       ```
       *(Note: iOS testing requires a macOS machine and an active iOS Simulator via Xcode).*
    4. Confirm it's visible to ADB before running tests:
       ```sh
       adb devices
       ```
       You should see a line like `emulator-5554   device` (not `offline`/`unauthorized`).
    5. If your device's identifier, platform, or Android version differs from the defaults in `env.conf` (e.g., `deviceName = "emulator-5554"`, `platformVersion = "16"`), update those values in the corresponding `android` or `ios` profile block. For Android, get the platform version with `adb shell getprop ro.build.version.release`.
    6. Run the test by specifying the target platform profile:

       **For Android:**
       ```sh
       mvn -f mobile/pom.xml clean verify -Denv=android
       ```

       **For iOS:**
       ```sh
       mvn -f mobile/pom.xml clean verify -Denv=ios
       ```
**Why use `verify` instead of `test` for Mobile?**
The `mobile` module strictly separates fast architectural unit tests from heavy Appium UI integration tests using Maven's lifecycle phases:
* The `test` phase (driven by `maven-surefire-plugin`) runs rapid architectural convention checks (e.g., verifying that all Page Object interfaces have corresponding Android and iOS implementations).
* The `integration-test` and `verify` phases (driven by `maven-failsafe-plugin`) run the actual cross-platform Appium tests.

  The test itself (`appium.OpenSettingsAppIT`) drives the device's pre-installed Settings app, so no APK/IPA build/install step is required. The `mobile` module is intentionally excluded from CI (no Android device/emulator or iOS simulator available there), same as `db`. See `.claude/skills/run-mobile-tests/SKILL.md` for detailed troubleshooting.