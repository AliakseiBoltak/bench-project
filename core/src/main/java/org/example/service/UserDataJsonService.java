package org.example.service;

import com.google.inject.Inject;
import org.example.dao.UserDataDao;
import org.example.exception.DataException;
import org.example.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDataJsonService implements UserDataService {

    private final UserDataDao userDataDao;

    @Inject
    public UserDataJsonService(UserDataDao userDataDao) {
        this.userDataDao = userDataDao;
    }

    @Override
    public User getUserByType(String type) {
        List<User> foundUsers = new ArrayList<>(userDataDao.findUserData(type) != null ?
                userDataDao.findUserData(type) : List.of());
        return foundUsers.stream()
                .filter(user -> user.getUsertype().equals(type))
                .findFirst()
                .orElseThrow(() -> new DataException("No user found with type: " + type));
    }
}