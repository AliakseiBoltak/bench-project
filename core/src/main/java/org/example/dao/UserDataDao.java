package org.example.dao;

import org.example.model.User;

import java.util.List;

public interface UserDataDao {
    List<User> findUserData(String userDataPath);
}