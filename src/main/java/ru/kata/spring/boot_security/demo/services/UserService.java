package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getAllUsers();

    void save(User user);

    void delete(Long id);

    User getOne(Long id);

    void update(Long id, User user);

    User oneUser();

    User createUser(User user, Set<Role> roles);

    User updateUser(User user, Set<Role> roles, Long id);

}
