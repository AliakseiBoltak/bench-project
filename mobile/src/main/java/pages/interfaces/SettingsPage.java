package pages.interfaces;

import com.codeborne.selenide.SelenideElement;

/**
 * Cross-platform contract for the device Settings screen.
 *
 * <p>This is the <b>default</b> Page Object pattern in the {@code mobile} module: we assume
 * a screen's behavior is identical on Android and iOS unless proven otherwise, so we declare
 * one interface here and provide exactly two implementations -
 * {@code pages.implementations.android.AndroidSettingsPage} and
 * {@code pages.implementations.ios.IOSSettingsPage}. Only the underlying locators differ per
 * platform; the exposed elements/behavior are the same.
 *
 * <p>{@code guice.PageModule} binds this interface via {@code guice.DynamicPageProvider}, which
 * resolves the correct implementation at runtime based on the configured platform. Steps classes
 * (e.g. {@code steps.common.SettingsSteps}) depend only on this interface, never on a concrete
 * platform implementation.
 *
 * <p>Contrast this with a screen whose behavior genuinely differs (or is unsupported) on one
 * platform - e.g. SMS notifications, which only exist on Android. For those cases we do NOT
 * create a matching interface/two-implementation pair; see
 * {@code pages.implementations.android.AndroidSmsNotificationsPage} and the "Page Objects" section
 * of the module README for the full rationale.
 */
public interface SettingsPage {

    SelenideElement searchBarTitle();

    SelenideElement menuTargetSetting();

}
