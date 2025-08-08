package org.chous.bets.service;

import org.chous.bets.model.dto.UserDTO;
import org.chous.bets.model.dto.UserUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface UserServiceAPI {

    List<UserDTO> findAll();

    Optional<UserDTO> findByUsername(String username);

    void update(String username, UserUpdateDTO userUpdateDTO);

    int getPrincipalUserId(String email);
}
