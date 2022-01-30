package com.hust.webchat.web.controller;

import com.hust.webchat.domain.User;
import com.hust.webchat.dto.PostingDTO;
import com.hust.webchat.security.SecurityUtils;
import com.hust.webchat.security.jwt.TokenProvider;
import com.hust.webchat.service.PostingService;
import com.hust.webchat.service.UserService;
import com.hust.webchat.util.CookieUtil;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final TokenProvider tokenProvider;

    private final UserService userService;

    private final PostingService postingService;

    public HomeController(TokenProvider tokenProvider, UserService userService, PostingService postingService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.postingService = postingService;
    }

    @GetMapping("/")
    public String doShowHomePage(@RequestParam(value = "logoutSuccess", required = false) @Nullable boolean logoutSuccess,
                                 HttpServletResponse response, Model model, HttpServletRequest request) {

        if (logoutSuccess) {
            Cookie jwtCookie = new Cookie("jwt-token", null);
            Cookie usernameCookie = new Cookie("username", null);
            Cookie userIdCookie = new Cookie("userId", null);

            // delete all cookie
            CookieUtil.deleteCookies(response, jwtCookie, userIdCookie, usernameCookie);
            return "login/login";
        }

        Optional<User> optionalUser;
        String token = CookieUtil.getTokenCookie(request);
        if (SecurityUtils.isAuthenticated()) {
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            optionalUser = userService.findByLogin(principal.getUsername());
            if (optionalUser.isPresent()) {
                model.addAttribute("currentUser", optionalUser.get());
            }
        } else if (!StringUtils.isEmpty(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            optionalUser = userService.findByLogin(authentication.getName());
            if (optionalUser.isPresent()) {
                model.addAttribute("currentUser", optionalUser.get());
            }
        } else {
            return "login/login";
        }

        // get Posting Friend
        List<PostingDTO> postingDTOS = postingService.getAllPostingOfFriends();
        model.addAttribute("postings", postingDTOS);
        return "home/home";
    }
}
