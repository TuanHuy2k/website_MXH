package com.hust.webchat.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.webchat.domain.User;
import com.hust.webchat.dto.MessageDTO;
import com.hust.webchat.dto.UserDTO;
import com.hust.webchat.message.ChatMessage;
import com.hust.webchat.service.MessageService;
import com.hust.webchat.service.UserService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Khanhdd183932
 */
@Controller
public class ChatController {

    private final UserService userService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final MessageService messageService;

    public ChatController(UserService userService, SimpMessagingTemplate simpMessagingTemplate, MessageService messageService) {
        this.userService = userService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/message-group")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/message-group")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // put to session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/chat/{currentUser}/{username}")
    public void sendMessage(@DestinationVariable String currentUser,
                            @DestinationVariable String username, String objectMessage) throws JsonProcessingException {
        ChatMessage chatMessage = new ObjectMapper().readValue(objectMessage, ChatMessage.class);
        Optional<User> currentUserLogin = userService.findByLogin(currentUser);
        Optional<User> user = userService.findByLogin(username);
        if (user.isPresent()) {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setSenderId(currentUserLogin.get().getId());
            messageDTO.setReceiverId(user.get().getId());
            messageDTO.setMessageContent(chatMessage.getContent());
            messageDTO.setSendingTime(Instant.now().getEpochSecond());
            messageDTO.setMessageGroupCode(UUID.randomUUID().toString());
            messageService.save(messageDTO);

            simpMessagingTemplate.convertAndSend("/topic/messages/" + currentUser + "/" + username, chatMessage);
        }
    }

    @GetMapping("/message")
    public String message(Model model) {
        // get list friend
        List<UserDTO> friends = userService.getFriendsOfCurrentUser();
        model.addAttribute("friends", friends);
        return "message/message";
    }

}
