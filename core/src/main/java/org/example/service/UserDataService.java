package org.example.service;

import com.google.inject.Inject;
import org.example.dao.UserDataDao;
import org.example.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDataService {

    @Inject
    private UserDataDao userDataJsonDao;

    public List<User> getUsersData(String type, String path)
    {
        return new ArrayList<>(userDataJsonDao.findUserData(type, path));
    }

    public User getTestUserByType(String type, String path)
    {
        List<User> testUsers = getUsersData(type, path);
        return testUsers.stream()
                .filter(user -> user.getUsertype().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No user found with type: " + type));
    }

}