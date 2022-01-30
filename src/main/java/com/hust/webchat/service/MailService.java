package com.hust.webchat.service;

import com.hust.webchat.domain.User;

public interface MailService {
    /**
     * gui mail kich hoat tai khoan
     *
     * @param user
     */
    void sendMailActivate(User user);
}
