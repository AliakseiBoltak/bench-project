import com.google.inject.Injector;
import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.config.ConfigLoader;
import org.example.guice.CoreModule;
import com.google.inject.Guice;
import org.testng.annotations.BeforeSuite;

@org.testng.annotations.Guice(modules = {CoreModule.class})
public class BaseAPITest {

    protected RequestSpecification baseRequestSpec;
    protected ConfigLoader configLoader;
    protected String baseUri ;

    @BeforeSuite
    public void setUp() {
        Injector injector = Guice.createInjector(new CoreModule());
        configLoader = injector.getInstance(ConfigLoader.class);
        baseUri = configLoader.getBaseUrl();
        baseRequestSpec = RestAssured
                .given()
                .baseUri(baseUri)
                .log().all()
                .contentType(ContentType.JSON)
                .header(Constants.X_API_KEY_HEADER, Constants.X_API_KEY_VALUE);
    }
}