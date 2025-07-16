package service;

import com.google.inject.Inject;
import dao.UserDataDao;
import model.User;
import org.example.exception.DataException;

import java.util.ArrayList;
import java.util.List;

public class UserDataService {

    private final UserDataDao userDataDao;

    @Inject
    public UserDataService(UserDataDao userDataDao) {
        this.userDataDao = userDataDao;
    }

    public List<User> getUserData(String type)
    {
        List<User> userData = new ArrayList<>();
        userData.addAll(userDataDao.findUserData(type));
        return userData;
    }

    public User getUserByType(String type)
    {
        List<User> testUsers = getUserData(type);
        return testUsers.stream()
                .filter(user -> user.getUsertype().equals(type))
                .findFirst()
                .orElseThrow(() -> new DataException("No user found with type: " + type));
    }
}