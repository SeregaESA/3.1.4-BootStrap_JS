package ru.kata.spring.boot_security.demo.util;


public class UserNotCreateException extends RuntimeException {

    public UserNotCreateException(String msg) {
        super(msg);
    }
}
