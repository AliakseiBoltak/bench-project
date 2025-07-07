package org.example.service;

import com.google.inject.Inject;
import org.example.dao.UserDataDao;
import org.example.exception.DataException;
import org.example.guice.TestModule;
import org.example.model.User;
import org.testng.annotations.Guice;

import java.util.ArrayList;
import java.util.List;

@Guice(modules = {TestModule.class})
public class UserDataService {

    @Inject
    public UserDataService(UserDataDao userDataJsonDao) {
        this.userDataJsonDao = userDataJsonDao;
    }

    private final UserDataDao userDataJsonDao;

    public List<User> getUsersData(String path)
    {
        return new ArrayList<>(userDataJsonDao.findUserData(path));
    }

    public User getTestUserByType(String type, String path)
    {
        List<User> testUsers = getUsersData(path);
        return testUsers.stream()
                .filter(user -> user.getUsertype().equals(type))
                .findFirst()
                .orElseThrow(() -> new DataException("No user found with type: " + type));
    }

}