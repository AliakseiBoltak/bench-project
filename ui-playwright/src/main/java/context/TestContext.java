package context;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.cucumber.guice.ScenarioScoped;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ScenarioScoped
public class TestContext {
    Playwright playwright;
    Browser browser;
    BrowserContext browserContext;
    Page page;
}