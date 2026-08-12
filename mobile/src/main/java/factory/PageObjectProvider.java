package factory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import constants.Platform;
import org.example.config.ConfigLoader;

public class PageObjectProvider {

    private final Injector injector;
    private final ConfigLoader configLoader;

    @Inject
    public PageObjectProvider(Injector injector, ConfigLoader configLoader) {
        this.injector = injector;
        this.configLoader = configLoader;
    }

    public <T> T getPage(Class<? extends T> androidClass, Class<? extends T> iosClass) {
        Platform platform = Platform.from(configLoader.getPlatformName());

        Class<? extends T> implementationClass = switch (platform) {
            case ANDROID -> androidClass;
            case IOS -> iosClass;
        };

        return injector.getInstance(implementationClass);
    }
}

