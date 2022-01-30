package com.hust.webchat.dto;

import com.hust.webchat.domain.User;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO extends AbstractDTO {

    private Long id;

    private String username;

    private String password;

    private String salt;

    private String fullName;

    private Long dateOfBirth;

    private Boolean activated;

    private String email;

    private String address;

    private Integer gender;

    private String phoneNumber;

    private Integer status;

    private String activationKey;

    private String avatarUrl;

    private String coverAvatarUrl;

    private List<User> friends = new ArrayList<>();
}
