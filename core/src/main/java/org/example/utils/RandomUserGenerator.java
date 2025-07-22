package org.example.utils;

import org.example.constants.UserTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomUserGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomUserGenerator.class);

    public static void main(String[] args) throws IOException {
        int userCount = args.length > 0 ? Integer.parseInt(args[0]) : 5;

        String outPath = args.length > 1
                ? args[1]
                :  System.getProperty("user.dir") + "/src/main/resources/users.json";

        Random random = new Random();
        UserTypes[] userTypes = UserTypes.values();

        String usersJson = IntStream.range(0, userCount)
                .mapToObj(i -> {
                    UserTypes selectedType = userTypes[random.nextInt(userTypes.length)];
                    String usertype = selectedType.name().toLowerCase();
                    String username = usertype + (1000 + random.nextInt(9000));
                    String password = "pwd" + (100000 + random.nextInt(900000));
                    return String.format("  {\"usertype\": \"%s\", \"username\": \"%s\", \"password\": \"%s\"}",
                            usertype, username, password);
                })
                .collect(Collectors.joining(",\n", "[\n", "\n]\n"));

        try (FileWriter writer = new FileWriter(outPath)) {
            writer.write(usersJson);
        }
        LOGGER.info("Generated {} users at {}", userCount, outPath);

        LOGGER.info("Available user types:");
        for (UserTypes type : userTypes) {
            LOGGER.info(" - {}", type);
        }
    }

}