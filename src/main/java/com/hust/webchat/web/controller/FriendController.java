package com.hust.webchat.web.controller;

import com.hust.webchat.domain.User;
import com.hust.webchat.dto.UserDTO;
import com.hust.webchat.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FriendController {

    private final UserService userService;

    public FriendController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/friends")
    public String profile(Model model) {
        User currentUser = userService.getCurrentUser();

        // list danh sach loi moi ket ban
        List<UserDTO> listFriendRequest = userService.getListApproveFriends();
        model.addAttribute("friendRequests", listFriendRequest);

        // list ban be
        List<UserDTO> friends = userService.getFriendsOfCurrentUser();
        model.addAttribute("friends", friends);

        // list danh sach nhung nguoi chua phai la ban be
        List<UserDTO> userNotFriends = userService.getUserNotFriend();
        model.addAttribute("userNotFriends", userNotFriends);

        model.addAttribute("currentUser", currentUser);
        return "friends/friend";
    }
}
