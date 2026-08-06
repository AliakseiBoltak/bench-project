import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import constants.Constants;
import io.qameta.allure.Allure;
import io.qameta.allure.httpclient.AllureHttpClientRequest;
import io.qameta.allure.httpclient.AllureHttpClientResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.example.config.ConfigLoader;
import org.example.guice.CoreModule;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static constants.Constants.USER_URI;
import static org.testng.Assert.assertEquals;

@Guice(modules = {CoreModule.class})
public abstract class BaseAPITest {

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected final ConfigLoader configLoader;
    protected final String baseUri;
    protected CloseableHttpClient httpClient;

    @Inject
    public BaseAPITest(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        this.baseUri = configLoader.getBaseUrl();
    }

    @BeforeClass
    public void setUp() {
        httpClient = HttpClientBuilder.create()
                .addInterceptorFirst(new AllureHttpClientRequest())
                .addInterceptorFirst(new AllureHttpClientResponse())
                .build();
    }

    @AfterClass
    public void tearDownClient() throws IOException {
        httpClient.close();
    }

    protected <T> T post(String path, Object requestBody, int expectedStatusCode, Class<T> responseType) throws IOException {
        HttpPost request = new HttpPost(baseUri + path);
        addCommonHeaders(request);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(OBJECT_MAPPER.writeValueAsString(requestBody), StandardCharsets.UTF_8));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            assertEquals(response.getStatusLine().getStatusCode(), expectedStatusCode, "Unexpected status code");
            return OBJECT_MAPPER.readValue(response.getEntity().getContent(), responseType);
        }
    }

    protected String get(String path, int expectedStatusCode) throws IOException {
        HttpGet request = new HttpGet(baseUri + path);
        addCommonHeaders(request);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            assertEquals(response.getStatusLine().getStatusCode(), expectedStatusCode, "Unexpected status code");
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        }
    }

    protected void cleaningUpCreatedUser(int userId) throws IOException {
        Allure.step("Cleaning up created user with ID: " + userId);
        HttpDelete request = new HttpDelete(baseUri + USER_URI + "/" + userId);
        addCommonHeaders(request);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            assertEquals(response.getStatusLine().getStatusCode(), 204, "Unexpected status code on cleanup");
        }
    }

    private void addCommonHeaders(HttpRequestBase request) {
        request.setHeader(Constants.X_API_KEY_HEADER, Constants.X_API_KEY_VALUE);
    }
}
