package com.hust.webchat.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserLoginRequest {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
