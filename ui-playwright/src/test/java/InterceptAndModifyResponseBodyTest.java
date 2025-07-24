import com.google.inject.Inject;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import github.GitHubBaseTest;
import io.qameta.allure.Allure;
import org.example.config.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicReference;

public class InterceptAndModifyResponseBodyTest extends GitHubBaseTest {

    private static final String REQRES_API_URL = "https://reqres.in/api/users";
    private static final String INITIAL_RESPONSE_BODY_TEXT =
            "Tired of writing endless social media content? Let Content Caddy generate it for you.";
    private static final String MODIFIED_RESPONSE_BODY_TEXT =
            "The text you see on the screen has been intercepted and modified by Playwright!!!";
    private String interceptedAndModifiedBody;

    @Inject
    public InterceptAndModifyResponseBodyTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @Override
    protected boolean useStoredGitHubSession() {
        return false;
    }

    @AfterMethod
    public void addAllureAttachment() {
        Allure.addAttachment("Intercepted and modified API Response", "application/json",
                interceptedAndModifiedBody);
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.getLifecycle().addAttachment(
                "Screenshot with modified text displayed on the page",
                "image/png",
                "png",
                screenshot
        );
    }

    @Test
    public void testInterceptAndModifyReqresApiResponse() {
        AtomicReference<String> interceptedBody = new AtomicReference<>("");
        page.route(REQRES_API_URL, (Route route) -> {
            APIResponse response = route.fetch();
            String initialBody = response.text();
            String modifiedBody = initialBody.replace(INITIAL_RESPONSE_BODY_TEXT, MODIFIED_RESPONSE_BODY_TEXT);
            interceptedBody.set(modifiedBody);
            route.fulfill(new Route.FulfillOptions()
                    .setStatus(response.status())
                    .setHeaders(response.headers())
                    .setContentType(response.headers().getOrDefault("content-type", "application/json"))
                    .setBody(modifiedBody));
        });
        page.navigate(REQRES_API_URL);
        interceptedAndModifiedBody = interceptedBody.get();

        Assert.assertNotNull(interceptedAndModifiedBody, "Intercepted body should not be null");
        Assert.assertFalse(interceptedAndModifiedBody.isEmpty(), "Response body should not be empty");
        Assert.assertTrue(interceptedAndModifiedBody.contains(MODIFIED_RESPONSE_BODY_TEXT),
                "Response body does not contain expected modified text");

        String pageContent = page.content();
        Assert.assertTrue(pageContent.contains(MODIFIED_RESPONSE_BODY_TEXT),
                "Modified text is not displayed on the page!");

    }
}