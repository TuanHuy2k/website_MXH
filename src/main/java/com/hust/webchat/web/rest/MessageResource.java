package com.hust.webchat.web.rest;

import com.hust.webchat.domain.Message;
import com.hust.webchat.dto.MessageDTO;
import com.hust.webchat.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class MessageResource {

    private final MessageService messageService;

    public MessageResource(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping(value = "/message/history/{senderId}/{receiverId}")
    public ResponseEntity<List<MessageDTO>> getMessageHistory(@PathVariable Long senderId,
                                                              @PathVariable Long receiverId) {
        List<MessageDTO> messages = messageService.getMessageHistory(senderId, receiverId);
        return ResponseEntity.ok(messages);
    }
}
