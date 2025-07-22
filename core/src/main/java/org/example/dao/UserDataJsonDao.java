package org.example.dao;

import com.google.inject.Inject;
import org.example.loader.DataLoader;
import org.example.model.User;
import org.example.loader.JSONDataLoader;

import java.util.Arrays;
import java.util.List;

import static org.example.config.Constants.USERS_TEST_DATA_PATH;

public class UserDataJsonDao implements UserDataDao {

    private DataLoader dataLoader;

    @Inject
    public UserDataJsonDao(JSONDataLoader jsonDataLoader) {
        this.dataLoader = jsonDataLoader;
    }

    @Override
    public List<User> findUserData(String type) {
        return Arrays.asList(dataLoader.getData(USERS_TEST_DATA_PATH, User[].class));
    }

}