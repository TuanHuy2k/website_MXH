package com.hust.webchat.web.rest;

import com.hust.webchat.dto.UserDTO;
import com.hust.webchat.dto.request.UserLoginRequest;
import com.hust.webchat.dto.request.UserRegisterRequest;
import com.hust.webchat.dto.request.UserUpdateRequest;
import com.hust.webchat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/authenticate")
    @ResponseBody
    public String login(@RequestBody @Valid UserLoginRequest loginRequest,
                        HttpServletRequest request, HttpServletResponse response) throws IOException {
        return userService.login(loginRequest.getUsername(), loginRequest.getPassword(), request, response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest,
                                        HttpServletRequest request, HttpServletResponse response) {
        return userService.register(userRegisterRequest, response, request);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOS = userService.getAllUser();
        return ResponseEntity.ok(userDTOS);
    }

    // *************** FRIENDS *********************
    @PostMapping(value = "/users/friends/{id}")
    public ResponseEntity<UserDTO> addFriend(@PathVariable Long id) {
        UserDTO userDTO = userService.addFriend(id);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping(value = "/users/friends")
    public ResponseEntity<List<UserDTO>> getFriends() {
        List<UserDTO> friends = userService.getFriendsOfCurrentUser();
        return ResponseEntity.ok(friends);
    }

    @GetMapping(value = "/users/friends/approve/{userId}")
    public ResponseEntity<Boolean> approveFriend(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.approveFriend(userId));
    }

    @GetMapping(value = "/users/user-not-friends")
    public ResponseEntity<List<UserDTO>> getUserNotFriends() {
        List<UserDTO> userNotFriends = userService.getUserNotFriend();
        return ResponseEntity.ok(userNotFriends);
    }

    @GetMapping(value = "/users/friends/approve-list")
    public ResponseEntity<List<UserDTO>> getListApproveFriends() {
        return ResponseEntity.ok(userService.getListApproveFriends());
    }

    @GetMapping(value = "/users/friends/cancel-request-friend/{userId}")
    public ResponseEntity<Boolean> cancelRequestFriend(@PathVariable Long userId) {
        userService.cancelRequestFriend(userId);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @GetMapping(value = "/users/friends/cancel-friend/{userId}")
    public ResponseEntity<Boolean> cancelFriend(@PathVariable Long userId) {
        userService.cancelRequestFriend(userId);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    // ************** PROFILE *****************
    @PutMapping(value = "/users/profile/cover-image/{fileId}/{type}")
    public ResponseEntity<Boolean> updateImage(@PathVariable Long fileId, @PathVariable String type) {
        userService.updateImage(fileId, type);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @PutMapping(value = "/user/profile")
    public ResponseEntity<Boolean> update(@RequestBody @Valid UserUpdateRequest request) {
        userService.update(request);
        return ResponseEntity.ok(Boolean.TRUE);
    }

}
