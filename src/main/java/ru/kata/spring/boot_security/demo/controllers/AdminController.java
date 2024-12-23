package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.UserNotCreateException;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/")
    @ResponseBody
    public List<User> admin() {
        return userService.getAllUsers();
    }

    @GetMapping("/admin/{id}")// ?????
    @ResponseBody
    public User getUser (@PathVariable int id) {
        return userService.getOne((long) id);
    }


//    @GetMapping("/admin/")
//    public String admin(Model model) {
//        model.addAttribute("user", userService.getAllUsers());
//        model.addAttribute("admin", userService.oneUser());
//        return "users";
//    }

   @PostMapping(value = "/new")
    public ResponseEntity<HttpStatus> newUser(@RequestBody @Valid User user, BindingResult bindingResult) {

       if (bindingResult.hasErrors()) {
           StringBuilder errorMsg = new StringBuilder();
           List<FieldError> errors = bindingResult.getFieldErrors();
           for (FieldError error : errors) {
               errorMsg.append(error.getField())
                       .append("-").append(error.getDefaultMessage())
                       .append(";");
           }
           throw new UserNotCreateException(errorMsg.toString());
       }
       userService.save(userService.createUser(user, user.getRoles()));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException (UserNotCreateException e) {
        UserErrorResponse response = new UserErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



//    @GetMapping(value = "/new")
//    public String newUser(Model model) {
//        model.addAttribute("user", new User());
//        model.addAttribute("admin", userService.oneUser());
//        return "newUser";
//    }

//    @PostMapping("/create")
//    public String createUser(@ModelAttribute User user, @RequestParam("role") Set<Role> roles) {
//        userService.save(userService.createUser(user, roles));
//        return "redirect:/admin/";
//    }


    @GetMapping(value = "/delete")
    public ResponseEntity<HttpStatus> delete(@RequestBody User user) {
        userService.delete(user.getId());
        return ResponseEntity.ok(HttpStatus.OK);
    }

//    @GetMapping(value = "/delete")
//    public String delete(@RequestParam("id") long id) {
//        userService.delete(id);
//        return "redirect:/admin/";
//    }

    @PostMapping(value = "/update")
    public ResponseEntity<HttpStatus> update(@RequestBody User user) {
        userService.update(user.getId(), userService.updateUser(user, user.getRoles(), user.getId()));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/authUser")
    @ResponseBody
    public ResponseEntity<User> authenticationUser () {
        User user = userService.oneUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


//    @PostMapping(value = "/update")
//    public ResponseEntity<HttpStatus> update(@ModelAttribute("user") User user, @RequestParam("id") long id, @RequestParam(value = "role", required = false) Set<Role> roles) {
//        userService.update(id, userService.updateUser(user, roles, id));
//        return "redirect:/admin/";
//    }

//    @GetMapping("/user/admin")
//    public String user(Model model) {
//        model.addAttribute("user", userService.oneUser());
//        return "oneUserAdmin";
//    }
}
