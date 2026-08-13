package guice;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import constants.Platform;
import org.example.config.ConfigLoader;

public class DynamicPageProvider<T> implements Provider<T> {

    private final Class<T> interfaceType;

    @Inject
    private Injector injector;

    @Inject
    private ConfigLoader configLoader;

    public DynamicPageProvider(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    @Override
    public T get() {
        Platform platform = Platform.from(configLoader.getPlatformName());

        String simpleName = interfaceType.getSimpleName();

        String packageName = "pages.implementations.";
        String prefix;

        if (platform == Platform.ANDROID) {
            packageName += "android.";
            prefix = "Android";
        } else {
            packageName += "ios.";
            prefix = "IOS";
        }

        String implementationClassName = packageName + prefix + simpleName;

        try {
            @SuppressWarnings("unchecked")
            Class<? extends T> implClass = (Class<? extends T>) Class.forName(implementationClassName);
            return injector.getInstance(implClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find implementation class: " + implementationClassName, e);
        }
    }
}
