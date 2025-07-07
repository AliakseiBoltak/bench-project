package org.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exception.PathException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager {

    private FileManager() {
    }

    private static final Logger LOGGER = LogManager.getLogger(FileManager.class);

    public static String readFileAsString(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            LOGGER.error("Error reading file at path: {}", filePath, e);
            throw new PathException(e);
        }
    }

    public static byte[] readFileAsBytes(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            LOGGER.error("Error reading file at path: {}", filePath, e);
            throw new PathException(e);
        }
    }

    public static void writeFile(String filePath, String content) {
        try {
            Files.write(Paths.get(filePath), content.getBytes());
        } catch (IOException e) {
            LOGGER.error("Error reading file at path: {}", filePath, e);
            throw new PathException(e);
        }
    }
}
