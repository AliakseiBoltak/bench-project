package dao;

import model.User;

import java.util.List;

public interface UserDataDao {
    List<User> findUserData(String type);
}