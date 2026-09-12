package guice;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import config.MobileConfigLoader;
import constants.Platform;
import java.util.Objects;

/**
 * Resolves the platform-specific implementation of a cross-platform page interface at runtime,
 * based on the configured platform (see {@code pages.interfaces.SettingsPage} for the pattern
 * this supports).
 *
 * <p>Given an interface {@code pages.interfaces.<Name>}, this provider looks up
 * {@code pages.implementations.android.Android<Name>} or {@code pages.implementations.ios.IOS<Name>}
 * depending on {@link MobileConfigLoader#getPlatformName()}, and instantiates it via Guice.
 *
 * <p>Only used for interfaces registered in {@code guice.PageModule}. Platform-exclusive pages
 * (no interface, only one platform's implementation) never go through this provider - they are
 * injected directly as concrete types.
 */
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
