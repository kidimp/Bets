package org.chous.bets.util;

import lombok.experimental.UtilityClass;
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
}
