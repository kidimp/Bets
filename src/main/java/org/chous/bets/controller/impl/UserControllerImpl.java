package org.chous.bets.controller.impl;

import lombok.RequiredArgsConstructor;
import org.chous.bets.controller.UserControllerAPI;
import org.chous.bets.model.dto.UserDTO;
import org.chous.bets.model.dto.UserUpdateDTO;
import org.chous.bets.model.enums.RoleEnum;
import org.chous.bets.service.UserServiceAPI;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserControllerImpl implements UserControllerAPI {

    private final UserServiceAPI userService;

    @Override
    public String getUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/users/all";
    }

    @Override
    public String getEditForm(String username, Model model) {
        userService.findByUsername(username)
                .ifPresent(user -> model.addAttribute("user", user));

        model.addAttribute("roles", Arrays.asList(RoleEnum.values()));
        return "/users/edit";
    }

    @Override
    public String updateUser(String username,
                             UserUpdateDTO userUpdateDTO,
                             BindingResult bindingResult,
                             Model model) {

        model.addAttribute("roles", Arrays.asList(RoleEnum.values()));

        if (bindingResult.hasErrors()) {
            return "/users/edit";
        }

        userService.update(username, userUpdateDTO);
        return "redirect:/admin/users";
    }
}
