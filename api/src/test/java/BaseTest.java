import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public abstract class BaseTest {

    protected RequestSpecification baseRequestSpec;

    @BeforeClass
    public void setupSpec() {
        baseRequestSpec = RestAssured
                .given()
                .baseUri(Constants.BASE_URI)
                .log().all()
                .contentType(ContentType.JSON)
                .header(Constants.X_API_KEY_HEADER, Constants.X_API_KEY_VALUE);
    }
}