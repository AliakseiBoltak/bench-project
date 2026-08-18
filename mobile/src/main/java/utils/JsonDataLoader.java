package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;

@Singleton
public class JsonDataLoader {

    private static final Logger LOGGER = LogManager.getLogger(JsonDataLoader.class);
    private final Gson gson = new GsonBuilder().create();

    private InputStream getFileInputStream(String filePath) throws IOException {
        return new FileInputStream(new File(filePath).getAbsolutePath());
    }

    private InputStream getInputStream(String resource) {
        return JsonDataLoader.class.getResourceAsStream(resource);
    }

    public <T> T getData(String dataPath, Class<T> genericType) {
        LOGGER.info("Loading test data from: {}", dataPath);
        try {
            URL url = JsonDataLoader.class.getResource(dataPath);
            InputStream inputStream = url == null ? getFileInputStream(dataPath) : getInputStream(dataPath);
            if (inputStream == null) {
                throw new RuntimeException("Could not find file or resource: " + dataPath);
            }
            try (inputStream; Reader reader = new InputStreamReader(inputStream)) {
                return gson.fromJson(reader, genericType);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON data from " + dataPath, e);
        }
    }
}
