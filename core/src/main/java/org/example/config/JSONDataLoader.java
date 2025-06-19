package org.example.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.exception.DataException;

import java.io.*;
import java.net.URL;

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
        Reader reader = null;
        try
        {
            URL url = JSONDataLoader.class.getResource(dataPath);
            InputStream inputStream = url == null ? getFileInputStream(dataPath) : getInputStream(dataPath);
            reader = new InputStreamReader(inputStream);
            return gson.fromJson(reader, genericType);
        }
        catch (IOException e)
        {
            throw new DataException(e);
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                }
            }
        }
    }


}