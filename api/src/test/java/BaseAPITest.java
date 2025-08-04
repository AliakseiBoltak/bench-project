import com.google.inject.Inject;
import constants.Constants;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.config.ConfigLoader;
import org.example.guice.CoreModule;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Guice;

@Guice(modules = {CoreModule.class})
public abstract class BaseAPITest {

    protected final ConfigLoader configLoader;
    protected final String baseUri;
    protected RequestSpecification baseRequestSpec;

    @Inject
    public BaseAPITest(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        this.baseUri = configLoader.getBaseUrl();
    }

    @BeforeSuite
    public void setUp() {
        baseRequestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new AllureRestAssured())
                .addHeader(Constants.X_API_KEY_HEADER, Constants.X_API_KEY_VALUE)
                .build();
    }
}