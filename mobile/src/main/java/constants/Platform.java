package constants;

public enum Platform {

    ANDROID,
    IOS;

    public static Platform from(String platformName) {
        if (platformName == null) {
            throw new IllegalArgumentException("Platform name cannot be null");
        }
        try {
            return Platform.valueOf(platformName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unsupported platform specified in config: " + platformName);
        }
    }
}