package guice;

import com.google.inject.AbstractModule;
import pages.interfaces.SettingsPage;

/**
 * Binds cross-platform page interfaces (i.e. those following the default "one interface, two
 * implementations" pattern - see {@code pages.interfaces.SettingsPage}) to a
 * {@link DynamicPageProvider} that resolves the correct Android/iOS implementation at runtime.
 *
 * <p>Platform-exclusive pages (e.g. {@code pages.implementations.android.AndroidSmsNotificationsPage})
 * are intentionally NOT bound here: they have no interface and no counterpart on the other
 * platform, so Guice injects them directly as concrete types wherever they're needed.
 */
public class PageModule extends AbstractModule {

    @Override
    protected void configure() {
        bindPage(SettingsPage.class);
    }

    private <T> void bindPage(Class<T> interfaceType) {
        bind(interfaceType).toProvider(new DynamicPageProvider<>(interfaceType));
    }

}