package exceptions;

/**
 * Base runtime exception for framework-level (as opposed to assertion) failures
 * in the mobile module, e.g. misconfiguration, unsupported driver capabilities,
 * or unavailable sessions. Carries the current platform for easier triage in
 * Allure reports and logs.
 */
public class MobileFrameworkException extends RuntimeException {

    private final String platform;

    public MobileFrameworkException(String message, String platform) {
        super(decorate(message, platform));
        this.platform = platform;
    }

    public MobileFrameworkException(String message, String platform, Throwable cause) {
        super(decorate(message, platform), cause);
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }

    private static String decorate(String message, String platform) {
        return String.format("[platform=%s] %s", platform, message);
    }
}

