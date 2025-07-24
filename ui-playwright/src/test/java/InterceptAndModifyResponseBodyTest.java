import com.google.inject.Inject;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Route;
import github.GitHubBaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.qameta.allure.internal.shadowed.jackson.databind.node.ObjectNode;
import org.example.config.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.util.concurrent.atomic.AtomicReference;

public class InterceptAndModifyResponseBodyTest extends GitHubBaseTest {

    private static final String REQRES_API_URL = "https://reqres.in/api/users";
    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE = "content-type";
    private static final String MODIFIED_RESPONSE_BODY_TEXT =
            "The text you see on the screen has been intercepted and modified by Playwright!!!";
    private String modifiedBody;

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
        Allure.addAttachment("Intercepted and modified API Response", APPLICATION_JSON, modifiedBody);
        Allure.addAttachment("Screenshot with modified text displayed on the page",
                new ByteArrayInputStream(page.screenshot()));
    }

    @Test
    public void testInterceptAndModifyReqresApiResponse() {
        AtomicReference<String> interceptedBody = new AtomicReference<>("");
        ObjectMapper objectMapper = new ObjectMapper();
        page.route(REQRES_API_URL, (Route route) -> {
            APIResponse response = route.fetch();
            String originalBody = response.text();
            Allure.addAttachment("Original API Response", APPLICATION_JSON, originalBody);
            try {
                ObjectNode root = (ObjectNode) objectMapper.readTree(originalBody);
                updateTextFieldsInJsonWithNewText(root, MODIFIED_RESPONSE_BODY_TEXT);
                String modifiedBody = objectMapper.writeValueAsString(root);
                interceptedBody.set(modifiedBody);
                route.fulfill(new Route.FulfillOptions()
                        .setStatus(response.status())
                        .setHeaders(response.headers())
                        .setContentType(response.headers().getOrDefault(CONTENT_TYPE, APPLICATION_JSON))
                        .setBody(modifiedBody));
            } catch (JsonProcessingException e) {
                Allure.step("Failed to modify JSON response: " + e.getMessage());
                Allure.step("Returning original response body: " + originalBody);
                interceptedBody.set(originalBody);
                route.fulfill(new Route.FulfillOptions()
                        .setStatus(response.status())
                        .setHeaders(response.headers())
                        .setContentType(response.headers().getOrDefault(CONTENT_TYPE, APPLICATION_JSON))
                        .setBody(originalBody));
            }
        });

        page.navigate(REQRES_API_URL);
        modifiedBody = interceptedBody.get();

        Assert.assertNotNull(modifiedBody, "Response body should not be null");
        Assert.assertFalse(modifiedBody.isEmpty(), "Response body should not be empty");
        Assert.assertTrue(modifiedBody.contains(MODIFIED_RESPONSE_BODY_TEXT),
                "Response body does not contain expected modified text");

        Assert.assertTrue(page.content().contains(MODIFIED_RESPONSE_BODY_TEXT),
                "Modified text is not displayed on the page!");

    }

    private void updateTextFieldsInJsonWithNewText(ObjectNode node, String newText) {
        node.fields().forEachRemaining(entry -> {
            if ("text".equals(entry.getKey())) {
                node.put("text", newText);
            } else if (entry.getValue().isObject()) {
                updateTextFieldsInJsonWithNewText((ObjectNode) entry.getValue(), newText);
            } else if (entry.getValue().isArray()) {
                entry.getValue().forEach(child -> {
                    if (child.isObject()) {
                        updateTextFieldsInJsonWithNewText((ObjectNode) child, newText);
                    }
                });
            }
        });
    }
}