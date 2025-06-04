package org.chous.bets.util;

import org.chous.bets.model.User;
import org.chous.bets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors error) {
        User user = (User) o;

        try {
            userService.loadUserByUsername(user.getEmail());
            error.rejectValue("email", "", "Этот Email уже используется");
        } catch (UsernameNotFoundException ignored) {
            // Этот email ещё не занят
        }

        try {
            userService.loadUserByName(user.getUsername());
            error.rejectValue("username", "", "Это имя уже занято");
        } catch (UsernameNotFoundException ignored) {
            // Этот username ещё не занят
        }
    }


    public void checkEquality(String password, String passwordConfirm, Errors error) {
        if (!password.equals(passwordConfirm)) {
            error.rejectValue("password", "", "Пароли не совпадают");
        }
    }
}