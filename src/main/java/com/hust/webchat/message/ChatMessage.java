package com.hust.webchat.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hust.webchat.util.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
}
