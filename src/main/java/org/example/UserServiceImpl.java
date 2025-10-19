package org.example;

import org.example.dao.UserDao;
import org.example.model.User;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserById(Long id) {
        User user = userDao.findById(id);
        if (user == null) throw new IllegalArgumentException("User not found");
        return user;
    }

    @Override
    public User createUser(User user) {
        Long id = userDao.save(user);
        return userDao.findById(id);
    }

    @Override
    public void updateUser(User user) {
        userDao.update(user);
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userDao.findById(id);
        if (user == null) throw new IllegalArgumentException("User not found");
        userDao.delete(user);
    }
}
