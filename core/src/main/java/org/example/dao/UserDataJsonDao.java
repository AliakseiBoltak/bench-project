package org.example.dao;

import com.google.inject.Inject;
import org.example.config.JSONDataLoader;
import org.example.model.User;

import java.util.Arrays;
import java.util.List;

public class UserDataJsonDao implements UserDataDao {

    @Inject
    private JSONDataLoader jsonDataLoader;

    @Override
    public List<User> findUserData(String userType, String userDataPath) {
        return Arrays.asList(jsonDataLoader.getData(userDataPath, User[].class));
    }

}