package com.hust.webchat.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class MessageDTO extends AbstractDTO {

    private Long id;

    private String messageGroupCode;

    private Long senderId;

    private String messageContent;

    private Long sendingTime;

    private String type;

    private String nameMessage;

    private Long receiverId;
}
