import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "ReqresProvider", port = "0")
class PactContractTest {

    private static final Logger LOGGER = LogManager.getLogger(PactContractTest.class);

    @Pact(consumer = "ReqresConsumer", provider = "ReqresProvider")
    public V4Pact createPact(PactBuilder builder) {

        PactDslJsonBody responseBody = new PactDslJsonBody()
                .stringType("name", "morpheus")
                .stringType("job", "leader")
                .stringType("id", "123")
                .datetime("createdAt", "yyyy-MM-dd'T'HH:mm:ss.SSSX");

        return builder
                .usingLegacyDsl()
                .given("A request to create a user")
                .uponReceiving("A POST request to create a user")
                .path("/api/users")
                .method("POST")
                .body("{\"name\": \"morpheus\", \"job\": \"leader\"}")
                .willRespondWith()
                .status(201)
                .headers(Map.of("Content-Type", "application/json"))
                .body(responseBody)
                .toPact(V4Pact.class);
    }

    @Test
    void testCreateUser(MockServer mockServer) {
        LOGGER.info("Start PACT test for creating a user");
        given()
                .baseUri(mockServer.getUrl())
                .header("Content-Type", "application/json")
                .body("{\"name\": \"morpheus\", \"job\": \"leader\"}")
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
        LOGGER.info("Finishing PACT test for creating a user");
    }
}
