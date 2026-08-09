---
name: run-mobile-tests
description: Sets up and runs the Appium/Android tests in the mobile module — installing/starting Appium, starting an Android emulator or connecting a device, matching env.conf to the actual device, and troubleshooting common session-start failures. Trigger on "run the mobile tests", "run appium test", "set up appium", "start android emulator for tests", "mobile test won't start".
---

# Run Mobile (Appium/Android) Tests

The `mobile` module (`BaseMobileTest`, test class `OpenSettingsAppTest`) drives a real Android device/emulator through Appium's `UiAutomator2` driver. Unlike the other modules, this can't just be run with `mvn test` — an Appium server and a running Android device must exist first. This is also why `mobile` is excluded from CI (see `CLAUDE.md`), same as `db`.

## 1. Prerequisites (one-time setup)

- **Node.js** (Appium is an npm package) — v16+.
- **Android SDK** with `platform-tools` and `emulator` packages installed, and `ANDROID_HOME` (or `ANDROID_SDK_ROOT`) pointing at the SDK install. Easiest path: install Android Studio, then add its SDK's `platform-tools/` and `emulator/` directories to `PATH`.
- **At least one Android Virtual Device (AVD)** created via Android Studio's Device Manager, or `avdmanager`, targeting a system image that includes the Settings app (any standard Google APIs / AOSP image does) — or a physical device with Developer Options → USB debugging enabled and connected via USB.
- **Appium** itself, plus the `UiAutomator2` driver:
  ```sh
  npm install -g appium
  appium driver install uiautomator2
  ```

## 2. Start the Appium server

```sh
appium
```

By default this listens on `http://127.0.0.1:4723` — matches `appium.serverUrl` in `mobile/src/test/resources/env.conf`. Leave it running in its own terminal; watch its log while running the test for a clearer picture of session-start failures than surefire's output alone gives you.

## 3. Start the emulator (or connect a device)

```sh
emulator -avd <your_avd_name>          # list AVDs with: emulator -list-avds
```

Or start it from Android Studio's Device Manager. Wait until it's **fully booted** (home screen visible) before running tests — sessions started against a still-booting emulator fail unpredictably.

Verify it's visible to ADB:

```sh
adb devices
```

Expect a line like `emulator-5554   device` (not `offline` or `unauthorized`; for a physical device, accept the USB-debugging authorization prompt on the device screen).

## 4. Match `env.conf` to your actual device

`mobile/src/test/resources/env.conf` ships with defaults tuned for the standard first emulator instance:

```hocon
appium {
  serverUrl        = "http://127.0.0.1:4723"
  platformVersion  = "16"
  deviceName       = "emulator-5554"
  automationName   = "UiAutomator2"
  appPackage       = "com.android.settings"
  appActivity      = ".Settings"
}
```

If your device differs, update:
- `deviceName` — must match the identifier `adb devices` printed (e.g. `emulator-5556` if you have multiple emulators running, or the device's serial for a physical phone: `adb devices` also shows this).
- `platformVersion` — get it with `adb shell getprop ro.build.version.release`.

`appPackage`/`appActivity` point at the device's pre-installed Settings app deliberately, so the test needs no APK build/install step — leave these unless you're deliberately retargeting the test at a different (also pre-installed) app.

## 5. Build `core`, then run the test

```sh
mvn -f core/pom.xml clean install -q      # required once per session / after core changes
mvn -f mobile/pom.xml clean test
```

To target a non-default `env.conf` profile: `-Denv=dev` (etc.) — see `module-structure.md` in `.claude/conventions/` for how profile fallback works.

## Troubleshooting

- **`Could not find a connected Android device`** → `adb devices` first; if empty, the emulator isn't booted or the device isn't authorized yet.
- **Session-creation timeout, or Appium logs show it can't find the app** → confirm `appPackage`/`appActivity` are actually installed on *this* device (`adb shell pm list packages | grep settings` should show `com.android.settings`) — stock AOSP/Google system images always have it, but a minimal/custom image might not.
- **Connection refused to `appium.serverUrl`** → the Appium server isn't running, or started on a different port than configured; check the terminal where you ran `appium` for the actual bound address.
- **`UnknownHostException`/`MalformedURLException` from `AndroidDriverFactory`** → `appium.serverUrl` in `env.conf` is malformed (must be a full URL, e.g. `http://127.0.0.1:4723`, not just a host:port fragment).
- **Test passes locally but you're wondering why it's not in CI** → intentional; CI has no Android device/emulator or Appium server available (mirrors the `db`-module exclusion rationale).
