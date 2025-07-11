import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.config.ConfigLoader;
import org.testng.annotations.BeforeClass;

public abstract class BaseTest {

    protected RequestSpecification baseRequestSpec;
    protected String BASE_URI;

    @BeforeClass
    public void setup() {

        BASE_URI = new ConfigLoader(System.getProperty("env", "default")).getBaseUrl();

        baseRequestSpec = RestAssured
                .given()
                .baseUri(BASE_URI)
                .log().all()
                .contentType(ContentType.JSON)
                .header(Constants.X_API_KEY_HEADER, Constants.X_API_KEY_VALUE);
    }
}