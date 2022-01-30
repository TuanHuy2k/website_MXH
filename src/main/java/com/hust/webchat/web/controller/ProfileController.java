package com.hust.webchat.web.controller;

import com.hust.webchat.domain.User;
import com.hust.webchat.dto.PostingDTO;
import com.hust.webchat.dto.UserDTO;
import com.hust.webchat.service.PostingService;
import com.hust.webchat.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProfileController {

    private final UserService userService;

    private final PostingService postingService;

    public ProfileController(UserService userService, PostingService postingService) {
        this.userService = userService;
        this.postingService = postingService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);

        // get list friend
        List<UserDTO> friends = userService.getFriendsOfCurrentUser();
        model.addAttribute("friends", friends);

        // get list posting of currentUser
        List<PostingDTO> postingDTOS = postingService.getAllPostingOfCurrentUser();
        model.addAttribute("postingsOfCurrentUser", postingDTOS);
        return "profile/profile";
    }

    @GetMapping("/users")
    public String searchUser(@RequestParam String keyword, Model model) {
        List<UserDTO> userDTOS = userService.searchUser(keyword);
        model.addAttribute("users", userDTOS);
        return "/users/users";
    }
}
