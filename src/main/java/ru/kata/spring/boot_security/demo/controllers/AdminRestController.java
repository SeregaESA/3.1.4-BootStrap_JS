package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<User> admin() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getOne((long) id);
    }

    @GetMapping("users/roles")
    public List<Role> allRoles() {
        return roleService.findAll();
    }

    @PostMapping(value = "/new")
    public ResponseEntity<HttpStatus> newUser(@RequestBody User user) {
        userService.save(userService.createUser(user, user.getRoles()));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<HttpStatus> update(@RequestBody User user) {
        userService.update(user.getId(), userService.updateUser(user, user.getRoles(), user.getId()));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/authUser")
    public ResponseEntity<User> authenticationUser() {
        User user = userService.oneUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
