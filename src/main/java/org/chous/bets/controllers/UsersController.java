package org.chous.bets.controllers;

import org.chous.bets.models.Role;
import org.chous.bets.models.User;
import org.chous.bets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UsersController {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @GetMapping("/admin/users")
    public String users(Model model, @ModelAttribute("user") @Valid User user) {
        model.addAttribute("users", usersRepository.findAll());
        return "/users/all";
    }


    @GetMapping("/admin/users/{username}/edit")
    public String edit(Model model, @PathVariable String username) {

        if (usersRepository.findByUsername(username).isPresent()) {
            model.addAttribute("user", usersRepository.findByUsername(username).get());
        }

        List<Role> roles = new ArrayList<>();
        roles.add(Role.ROLE_ADMIN);
        roles.add(Role.ROLE_USER);

        model.addAttribute("allRoles", roles);

        return "/users/edit";
    }


    @PostMapping("/admin/users/{username}/edit")
    public String update(Model model, @ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @PathVariable("username") String username) {

        List<Role> roles = new ArrayList<>();
        roles.add(Role.ROLE_ADMIN);
        roles.add(Role.ROLE_USER);

        User userToChange = null;

        if (usersRepository.findByUsername(username).isPresent()) {
            userToChange = usersRepository.findByUsername(username).get();
        }

        assert userToChange != null;
        userToChange.setUsername(user.getUsername());

        model.addAttribute("allRoles", roles);
        userToChange.setRole(user.getRole());

        if (bindingResult.hasErrors()) {
            return "/users/edit";
        }

        usersRepository.save(userToChange);

        return "redirect:/admin/users";
    }

}