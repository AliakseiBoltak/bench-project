package unit;

import com.google.common.reflect.ClassPath;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

import static org.testng.Assert.fail;

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