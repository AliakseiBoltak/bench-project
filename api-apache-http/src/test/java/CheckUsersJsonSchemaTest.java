import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.example.config.ConfigLoader;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static constants.Constants.USER_URI;
import static org.testng.Assert.assertTrue;

public class CheckUsersJsonSchemaTest extends BaseAPITest {

    @Inject
    public CheckUsersJsonSchemaTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @Test(description = "Checks that users JSON response matches the schema")
    void checkUsersJsonSchemaTest() throws IOException {
        String responseBody = get(USER_URI, 200);

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        try (InputStream schemaStream = getClass().getClassLoader().getResourceAsStream("schemas/getUsersSchema.json")) {
            JsonSchema schema = factory.getSchema(schemaStream);
            JsonNode responseNode = OBJECT_MAPPER.readTree(responseBody);

            Set<ValidationMessage> validationErrors = schema.validate(responseNode);
            assertTrue(validationErrors.isEmpty(), "Schema validation errors: " + validationErrors);
        }
    }
}
