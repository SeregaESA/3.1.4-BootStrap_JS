package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.UserDetailsImpl;
import ru.kata.spring.boot_security.demo.services.UserDetailService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
public class UserController {

    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user")
    public String user(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        model.addAttribute("user", user);
        return "oneUser";
    }

    @GetMapping("/admin/")
    public String admin(Model model) {
        model.addAttribute("user", userDetailService.getAllUsers());
        return "users";
    }

    @GetMapping(value = "/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "newUser";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute User user, @RequestParam("role") List<String> roles) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roleSet = new HashSet<>();
        for (String role : roles) {
            Role newRole = userDetailService.getRole(role);
            if (newRole != null) {
                roleSet.add(newRole);
            }
        }
        user.setRoles(roleSet);
        userDetailService.save(user);
        return "redirect:/admin/";
    }

    @GetMapping(value = "/delete")
    public String delete(@RequestParam("id") long id) {
        userDetailService.delete(id);
        return "redirect:/admin/";
    }

    @GetMapping(value = "/edit")
    public String edit(@RequestParam(value = "id") long id, Model model) {
        model.addAttribute("user", userDetailService.getOne(id));
        return "edit";
    }

    @PostMapping(value = "/update")
    public String update(@ModelAttribute("user") User user, @RequestParam("id") long id, @RequestParam(value = "role") List<String> roles) {
        User oldUser = userDetailService.getOne(id);
        String oldPassword = oldUser.getPassword();
        String newPassword = user.getPassword();

        if (newPassword != null && !newPassword.isEmpty() && !passwordEncoder.matches(newPassword, oldPassword)) {

            user.setPassword(passwordEncoder.encode(newPassword));
        } else {
            user.setPassword(oldPassword);
        }

        Set<Role> roleSet = new HashSet<>();

        if (roles == null || roles.isEmpty()) {
            roleSet = oldUser.getRoles();
        } else {
            for (String role : roles) {
                Role newRole = userDetailService.getRole(role);
                if (newRole != null) {
                    roleSet.add(newRole);
                }
            }
        }

        user.setRoles(roleSet);
        userDetailService.update(id, user);
        return "redirect:/admin/";
    }
}
