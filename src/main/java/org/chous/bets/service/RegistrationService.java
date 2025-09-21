package org.chous.bets.service;

import org.chous.bets.model.dto.RegistrationRequestDTO;
import org.chous.bets.model.dto.UserDTO;

public interface RegistrationService {

    void register(RegistrationRequestDTO registrationRequest, String rawPassword);

    boolean activateUser(String code);

    void updateResetPasswordToken(String email);

    UserDTO getByResetPasswordToken(String token);

    void updatePassword(UserDTO userDTO, String newPassword);
}
