package org.example.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class JSONDataLoader implements DataLoader{
    private final Gson gson = new GsonBuilder().create();
    private static final Logger LOGGER = LogManager.getLogger(JSONDataLoader.class);

    private InputStream getFileInputStream(String filePath) throws IOException
    {
        return new FileInputStream(new File(filePath).getAbsolutePath());
    }

    private InputStream getInputStream(String resource)
    {
        return JSONDataLoader.class.getResourceAsStream(resource);
    }

    @Override
    public <T> T getData(String dataPath, Class<T> genericType)
    {
        try (InputStream inputStream = JSONDataLoader.class.getResource(dataPath) == null
                ? getFileInputStream(dataPath)
                : getInputStream(dataPath);
             Reader reader = new InputStreamReader(inputStream))
        {
            return gson.fromJson(reader, genericType);
        }
        catch (IOException e)
        {
            LOGGER.error("Error reading data from path: {}", dataPath, e);
            throw new RuntimeException(e);
        }
    }

}