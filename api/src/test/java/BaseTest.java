import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.config.ConfigLoader;
import org.testng.annotations.BeforeClass;

public abstract class BaseTest {

    protected RequestSpecification baseRequestSpec;
    protected String baseUri ;

    @BeforeClass
    public void setup() {

        baseUri = new ConfigLoader(System.getProperty("env", "default")).getBaseUrl();

        baseRequestSpec = RestAssured
                .given()
                .baseUri(baseUri)
                .log().all()
                .contentType(ContentType.JSON)
                .header(Constants.X_API_KEY_HEADER, Constants.X_API_KEY_VALUE);
    }
}