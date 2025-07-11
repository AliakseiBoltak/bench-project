import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.config.ConfigLoader;
import org.testng.annotations.BeforeClass;

import static org.example.constants.Constants.ENV;

public abstract class BaseAPITest {

    protected RequestSpecification baseRequestSpec;
    protected String baseUri ;

    @BeforeClass
    public void setup() {

        baseUri = new ConfigLoader(ENV).getBaseUrl();

        baseRequestSpec = RestAssured
                .given()
                .baseUri(baseUri)
                .log().all()
                .contentType(ContentType.JSON)
                .header(Constants.X_API_KEY_HEADER, Constants.X_API_KEY_VALUE);
    }
}