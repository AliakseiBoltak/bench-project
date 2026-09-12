package unit;

import com.google.common.reflect.ClassPath;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

import static org.testng.Assert.fail;

/**
 * Enforces the module's default Page Object convention: every interface declared under
 * {@code pages.interfaces} (e.g. {@code SettingsPage}) must have both an
 * {@code pages.implementations.android.Android<Name>} and a
 * {@code pages.implementations.ios.IOS<Name>} implementation.
 *
 * <p>This check intentionally does NOT apply to platform-exclusive pages such as
 * {@code pages.implementations.android.AndroidSmsNotificationsPage} - those pages have no
 * interface at all (by design, since the feature doesn't exist on the other platform) and
 * therefore never appear in {@code pages.interfaces}, so they're outside this test's scope.
 * See the module README's "Page Objects" section for the full rationale.
 */
public class ArchitectureConventionTest {

    @Test(description = "Checks that each page interface has physical implementations for Android and iOS")
    public void testPageImplementationsExist() throws IOException {
        // Define package constants
        final String INTERFACES_PACKAGE = "pages.interfaces";
        final String ANDROID_PACKAGE = "pages.implementations.android.";
        final String IOS_PACKAGE = "pages.implementations.ios.";

        // Use Guava ClassPath to scan the interfaces package
        ClassPath classPath = ClassPath.from(ClassLoader.getSystemClassLoader());
        Set<ClassPath.ClassInfo> interfaceClasses = classPath.getTopLevelClasses(INTERFACES_PACKAGE);

        // Foolproof check: verify that the test actually found the package and it is not empty
        if (interfaceClasses.isEmpty()) {
            fail("Architecture test failed: No interfaces found in package " + INTERFACES_PACKAGE);
        }

        // Iterate through each found interface
        for (ClassPath.ClassInfo classInfo : interfaceClasses) {
            String interfaceName = classInfo.getSimpleName(); // e.g., "SettingsPage"

            // Construct expected paths to the implementation classes
            String expectedAndroidClass = ANDROID_PACKAGE + "Android" + interfaceName;
            String expectedIosClass = IOS_PACKAGE + "IOS" + interfaceName;

            // Verify the physical existence of the classes
            assertClassExists(expectedAndroidClass, interfaceName, "Android");
            assertClassExists(expectedIosClass, interfaceName, "iOS");
        }
    }

    /**
     * Helper method that attempts to load a class into memory.
     * If the class is missing, it fails the test with a clear and descriptive message.
     */
    private void assertClassExists(String className, String interfaceName, String platform) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            fail(String.format(
                    "❌ Architecture error: Implementation for %s not found for interface '%s'!\n" +
                            "Expected the developer to create class: %s",
                    platform, interfaceName, className
            ));
        }
    }
}