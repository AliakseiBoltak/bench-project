package org.example.utils;

import org.example.config.Constants;
import org.example.constants.UserTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomUserGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomUserGenerator.class);

    private static final int USERNAME_OFFSET = 1000;
    private static final int USERNAME_RANGE = 9000;
    private static final int PASSWORD_OFFSET = 100000;
    private static final int PASSWORD_RANGE = 900000;
    private static final int DEFAULT_USER_COUNT = 5;

    public static void main(String[] args) throws IOException {
        int userCount = DEFAULT_USER_COUNT;

        if (args.length > 0) {
            try {
                userCount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                LOGGER.warn("Invalid input for user count '{}'. Falling back to default value: {}", args[0],
                        DEFAULT_USER_COUNT);
            }
        }

        String outPath = args.length > 1
                ? args[1]
                : Constants.GENERATED_USERS_PATH;

        Path outPathObj = Paths.get(outPath);
        File parentDir = outPathObj.getParent().toFile();
        if (!parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (created) {
                LOGGER.info("Created missing parent directories for output path: {}", parentDir.getAbsolutePath());
            } else {
                LOGGER.warn("Failed to create parent directories for output path: {}", parentDir.getAbsolutePath());
            }
        }

        Random random = new Random();
        UserTypes[] userTypes = UserTypes.values();

        String usersJson = IntStream.range(0, userCount)
                .mapToObj(i -> {
                    UserTypes selectedType = userTypes[random.nextInt(userTypes.length)];
                    String usertype = selectedType.name().toLowerCase();
                    String username = usertype + (USERNAME_OFFSET + random.nextInt(USERNAME_RANGE));
                    String password = "pwd" + (PASSWORD_OFFSET + random.nextInt(PASSWORD_RANGE));
                    return String.format("  {\"usertype\": \"%s\", \"username\": \"%s\", \"password\": \"%s\"}",
                            usertype, username, password);
                })
                .collect(Collectors.joining(",\n", "[\n", "\n]\n"));

        try (FileWriter writer = new FileWriter(outPath)) {
            writer.write(usersJson);
        }
        LOGGER.info("Generated {} users at {}", userCount, outPath);

        LOGGER.info("Available user types:");
        Arrays.stream(userTypes).forEach(type -> LOGGER.info(" - {}", type));
    }

}