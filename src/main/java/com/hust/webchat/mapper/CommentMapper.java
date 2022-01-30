package com.hust.webchat.mapper;

import com.hust.webchat.domain.Comment;
import com.hust.webchat.dto.CommentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {
    Comment toEntity(CommentDTO messageDTO);

    CommentDTO toDto(Comment message);
}
