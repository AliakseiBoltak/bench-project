package constants;

import java.util.Arrays;

/**
 * Single source of truth for the platforms this module supports.
 * Parsed once from {@code appium.platformName} so driver creation and page-object
 * resolution can never drift apart.
 */
public enum Platform {

    ANDROID,
    IOS;

    public static Platform from(String platformName) {
        return Arrays.stream(values())
                .filter(platform -> platform.name().equalsIgnoreCase(platformName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported platform specified in config: " + platformName));
    }
}
