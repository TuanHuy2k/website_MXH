package com.hust.webchat.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;

public class CookieUtil {

    private static final String DOMAIN = "localhost";

    private static final String PATH_COOKIE = "/";

    public static void addCookies(HttpServletResponse response, Cookie... cookies) {
        Arrays.asList(cookies).stream().forEach(cookie -> {
            cookie.setDomain(DOMAIN);
            cookie.setPath(PATH_COOKIE);
            response.addCookie(cookie);
        });
    }

    public static void deleteCookies(HttpServletResponse response, Cookie... cookies) {
        Arrays.asList(cookies).stream().forEach(cookie -> {
            cookie.setDomain(DOMAIN);
            cookie.setMaxAge(0);
            cookie.setPath(PATH_COOKIE);
            response.addCookie(cookie);
        });
    }

    public static String getTokenCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (Objects.nonNull(cookies)) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("jwt-token")) {
                    token = cookies[i].getValue().replace("[", "").replace("]", "");
                }
            }
        }
        return token;
    }

    public static String getUsernameCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String username = null;
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("username")) {
                username = cookies[i].getValue().replace("[", "").replace("]", "");
            }
        }
        return username;
    }
}
