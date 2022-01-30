package com.hust.webchat.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserRegisterRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    @Size(max = 255, message = "Địa chỉ Email không được vượt quá 255 kí tự !!!")
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String repeatPassword;

}
