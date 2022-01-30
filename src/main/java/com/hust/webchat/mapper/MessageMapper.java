package com.hust.webchat.mapper;

import com.hust.webchat.domain.Message;
import com.hust.webchat.dto.MessageDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    Message toEntity(MessageDTO messageDTO);

    MessageDTO toDto(Message message);
}
