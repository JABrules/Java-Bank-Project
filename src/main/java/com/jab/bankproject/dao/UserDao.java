package com.jab.bankproject.dao;

import com.jab.bankproject.entities.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();
    List<User> getUsersByNameAndPassword(String username, String password);
    User getUserByID(int id);
    User addUser(User user);
    void deleteUserByID(int id);
}
