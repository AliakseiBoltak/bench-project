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

    public User getUserByType(String type) {
        List<User> testUsers = new ArrayList<>(userDataDao.findUserData(type) != null ?
                userDataDao.findUserData(type) : List.of());
        return testUsers.stream()
                .filter(user -> user.getUsertype().equals(type))
                .findFirst()
                .orElseThrow(() -> new DataException("No user found with type: " + type));
    }
}