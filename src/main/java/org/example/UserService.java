package org.example;

import org.example.model.User;

public interface UserService {
    User getUserById(Long id);
    User createUser(User user);
    void updateUser(User user);
    void deleteUserById(Long id);
}
