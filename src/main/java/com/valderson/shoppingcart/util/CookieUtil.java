package com.valderson.shoppingcart.util;

import com.valderson.shoppingcart.config.JwtConfig;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final JwtConfig jwtConfig;

    public void addAuthCookie(final HttpServletResponse response, final String token) {
        Cookie cookie = new Cookie(jwtConfig.getCookieName(), token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // true para HTTPS em produção
        cookie.setPath("/");
        cookie.setMaxAge(jwtConfig.getExpiration());
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    public void clearAuthCookie(final HttpServletResponse response) {
        Cookie cookie = new Cookie(jwtConfig.getCookieName(), "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // true para HTTPS em produção
        cookie.setPath("/");
        cookie.setMaxAge(0); // Remove o cookie
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }
}