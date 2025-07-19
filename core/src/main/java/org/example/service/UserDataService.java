package org.example.service;

import com.google.inject.Inject;
import org.example.dao.UserDataDao;
import org.example.exception.DataException;
import org.example.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDataService {

    private final UserDataDao userDataDao;

    @Inject
    public UserDataService(UserDataDao userDataDao) {
        this.userDataDao = userDataDao;
    }

    public User getUserByType(String type) {
        List<User> testUsers = new ArrayList<>(userDataDao.findUserData(type) != null ?
                userDataDao.findUserData(type) : List.of());
        return testUsers.stream()
                .filter(user -> user.getUsertype().equals(type))
                .findFirst()
                .orElseThrow(() -> new DataException("No user found with type: " + type));
    }
}