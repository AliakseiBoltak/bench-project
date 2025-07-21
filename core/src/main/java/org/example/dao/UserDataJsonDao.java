package org.example.dao;

import com.google.inject.Inject;
import org.example.model.User;
import org.example.utils.JSONDataLoader;

import java.util.Arrays;
import java.util.List;

import static org.example.config.Constants.USERS_TEST_DATA_PATH;

public class UserDataJsonDao implements UserDataDao {

    private JSONDataLoader jsonDataLoader;

    @Inject
    public UserDataJsonDao(JSONDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }

    @Override
    public List<User> findUserData(String type) {
        return Arrays.asList(jsonDataLoader.getData(USERS_TEST_DATA_PATH, User[].class));
    }

}