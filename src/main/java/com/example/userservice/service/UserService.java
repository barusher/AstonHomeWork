package com.example.userservice.service;

import com.example.userservice.dao.UserDao;
import com.example.userservice.model.UserEntity;

import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(UserEntity user) {
        userDao.save(user);
    }

    public void updateUser(UserEntity user) {
        userDao.update(user);
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
    }

    public List<UserEntity> getAllUsers() {
        return userDao.getAll();
    }

    public UserEntity getUserById(Long id) {
        return userDao.getById(id);
    }
}
