package org.chous.bets.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityContextUtil {

    /**
     * Получение id текущего авторизированного пользователя
     * @return id текущего авторизированного пользователя
     */
    public static String getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Проверка, авторизирован ли пользователь
     * @return true or false
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);
    }
}
