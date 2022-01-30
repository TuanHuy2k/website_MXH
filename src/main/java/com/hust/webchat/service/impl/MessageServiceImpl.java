package com.hust.webchat.service.impl;

import com.hust.webchat.domain.Message;
import com.hust.webchat.dto.MessageDTO;
import com.hust.webchat.mapper.MessageMapper;
import com.hust.webchat.repository.MessageRepository;
import com.hust.webchat.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageMapper messageMapper, MessageRepository messageRepository) {
        this.messageMapper = messageMapper;
        this.messageRepository = messageRepository;
    }

    @Override
    public List<MessageDTO> getMessageHistory(Long senderId, Long receiverId) {
        List<Message> messages = messageRepository.findAllBySenderIdAndReceiverId(senderId, receiverId);
        List<MessageDTO> messageDTOS = messages.stream()
                .sorted(Comparator.comparingLong(Message::getSendingTime))
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
        return messageMapper.toDto(messages);
    }

    @Override
    public MessageDTO save(MessageDTO messageDTO) {
        Message message = messageMapper.toEntity(messageDTO);
        Message messageSaved = messageRepository.save(message);
        return messageMapper.toDto(messageSaved);
    }
}
