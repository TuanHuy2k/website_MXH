package com.hust.webchat.service;

import com.hust.webchat.dto.MessageDTO;

import java.util.List;

public interface MessageService {
    /**
     * get history of message
     *
     * @param senderId
     * @param receiverId
     * @return
     */
    List<MessageDTO> getMessageHistory(Long senderId, Long receiverId);

    /**
     * save message
     *
     * @param messageDTO
     * @return
     */
    MessageDTO save(MessageDTO messageDTO);
}
