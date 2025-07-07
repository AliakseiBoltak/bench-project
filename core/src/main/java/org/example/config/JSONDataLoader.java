package org.example.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.exception.DataException;

import java.io.*;

public class JSONDataLoader {
    private final Gson gson = new GsonBuilder().create();

    private InputStream getFileInputStream(String filePath) throws IOException
    {
        return new FileInputStream(new File(filePath).getAbsolutePath());
    }

    private InputStream getInputStream(String resource)
    {
        return JSONDataLoader.class.getResourceAsStream(resource);
    }

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
            throw new DataException(e.getMessage());
        }
    }

}