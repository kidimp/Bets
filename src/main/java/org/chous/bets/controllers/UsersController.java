package org.chous.bets.controllers;

import org.chous.bets.models.User;
import org.chous.bets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UsersController {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @GetMapping("users/all")
    public String users(Model model) {
        model.addAttribute("users", usersRepository.findAll());

        return "users/all";
    }


    @GetMapping("/users/{username}/edit")
    public String edit(Model model, @PathVariable String username) {
        if (usersRepository.findByUsername(username).isPresent()) {
            model.addAttribute("user", usersRepository.findByUsername(username).get());
        }

        return "users/edit";
    }


    @PostMapping("users/{username}/edit")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @PathVariable("username") String username) {

        User userToChange = null;

        if (usersRepository.findByUsername(username).isPresent()) {
            userToChange = usersRepository.findByUsername(username).get();
        }

        assert userToChange != null;
        userToChange.setUsername(user.getUsername());

        if (bindingResult.hasErrors()) {
            return "/users/edit";
        }

        usersRepository.save(userToChange);

        return "redirect:/users/all";
    }


//    @GetMapping("{user}")
//    public String userEditForm(@PathVariable User user, Model model) {
//        model.addAttribute("user", user);
//        model.addAttribute("roles", Role.values());
//
//        return "userEdit";
//    }


//    @PostMapping
//    public String userSave(
//            @RequestParam String username,
//            @RequestParam Map<String, String> form,
//            @RequestParam("userId") User user
//    ) {
//        user.setUsername(username);
//
//        Set<String> roles = Arrays.stream(Role.values())
//                .map(Role::name)
//                .collect(Collectors.toSet());
//
//        user.getRoles().clear();
//
//        for (String key : form.keySet()) {
//            if (roles.contains(key)) {
//                user.getRoles().add(Role.valueOf(key));
//            }
//        }
//
//        usersRepository.save(user);
//
//        return "redirect:users/all";
//    }
}