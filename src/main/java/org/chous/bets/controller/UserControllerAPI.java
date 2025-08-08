package org.chous.bets.controller;

import jakarta.validation.Valid;
import org.chous.bets.model.dto.UserUpdateDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/users")
public interface UserControllerAPI {

    @GetMapping()
    String getUsers(Model model);

    @GetMapping("/{username}/edit")
    String getEditForm(@PathVariable String username, Model model);

    @PostMapping("/{username}/edit")
    String updateUser(@PathVariable String username,
                      @Valid @ModelAttribute("user") UserUpdateDTO userUpdateDTO,
                      BindingResult bindingResult,
                      Model model);
}
