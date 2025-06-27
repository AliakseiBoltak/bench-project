package org.example.dao;

import com.google.inject.Inject;
import org.example.config.JSONDataLoader;
import org.example.guice.TestModule;
import org.example.model.User;
import org.testng.annotations.Guice;

import java.util.Arrays;
import java.util.List;

@Guice(modules = {TestModule.class})
public class UserDataJsonDao implements UserDataDao {

    @Inject
    public UserDataJsonDao(JSONDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }

    private final JSONDataLoader jsonDataLoader;

    @Override
    public List<User> findUserData(String userDataPath) {
        return Arrays.asList(jsonDataLoader.getData(userDataPath, User[].class));
    }

}