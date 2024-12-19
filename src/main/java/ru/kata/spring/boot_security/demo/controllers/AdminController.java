package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;
import java.util.Set;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/")
    public String admin(Model model) {
        model.addAttribute("user", userService.getAllUsers());
        model.addAttribute("admin", userService.oneUser());
        return "users";
    }

    @GetMapping(value = "/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("admin", userService.oneUser());
        return "newUser";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute User user, @RequestParam("role") Set<Role> roles) {
        userService.save(userService.createUser(user, roles));
        return "redirect:/admin/";
    }

    @GetMapping(value = "/delete")
    public String delete(@RequestParam("id") long id) {
        userService.delete(id);
        return "redirect:/admin/";
    }

    @GetMapping(value = "/edit")
    public String edit(@RequestParam(value = "id") long id, Model model) {
        model.addAttribute("userNew", userService.getOne(id));
        return "edit";
    }

    @PostMapping(value = "/update")
    public String update(@ModelAttribute("user") User user, @RequestParam("id") long id, @RequestParam(value = "role") List<String> roles) {
        userService.update(id, userService.updateUser(user, roles, id));
        return "redirect:/admin/";
    }

    @GetMapping("/user/admin")
    public String user(Model model) {
        model.addAttribute("user", userService.oneUser());
        return "oneUserAdmin";
    }

}
