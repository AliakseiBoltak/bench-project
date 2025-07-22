package org.example.loader;

public interface DataLoader {
    <T> T getData(String dataPath, Class<T> genericType);
}
