package service;

import com.google.inject.Inject;
import model.User;
import org.example.exception.DataException;
import org.example.guice.CoreModule;

import org.example.utils.JSONDataLoader;
import org.testng.annotations.Guice;

import java.util.Arrays;
import java.util.List;

@Guice(modules = {CoreModule.class})
public class UserDataService {

    private final JSONDataLoader jsonDataLoader;

    @Inject
    public UserDataService(JSONDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }

    public User getUserByTypeFromJson(String type, String path)
    {
        List<User> testUsers = Arrays.asList(jsonDataLoader.getData(path, User[].class));
        return testUsers.stream()
                .filter(user -> user.getUsertype().equals(type))
                .findFirst()
                .orElseThrow(() -> new DataException("No user found with type: " + type));
    }
}