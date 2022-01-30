package com.hust.webchat.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String fullName;

    private String address;

    private String phoneNumber;

    private String mail;

    private Integer gender;
}
