package guice;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import config.MobileConfigLoader;
import constants.Platform;
import java.util.Objects;

public class DynamicPageProvider<T> implements Provider<T> {

    private final Class<T> interfaceType;

    @Inject
    private Injector injector;

    @Inject
    private MobileConfigLoader configLoader;

    public DynamicPageProvider(Class<T> interfaceType) {
        this.interfaceType = Objects.requireNonNull(interfaceType, "interfaceType");
    }

    @Override
    public T get() {
        Platform platform = Platform.from(configLoader.getPlatformName());
        String simpleName = interfaceType.getSimpleName();

        String implClassName = String.format(
                "pages.implementations.%s.%s%s",
                platform == Platform.ANDROID ? "android" : "ios",
                platform == Platform.ANDROID ? "Android" : "IOS",
                simpleName
        );

        try {
            @SuppressWarnings("unchecked")
            Class<? extends T> implClass = (Class<? extends T>) Class.forName(implClassName);
            return injector.getInstance(implClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find implementation class: " + implClassName, e);
        }
    }
}
