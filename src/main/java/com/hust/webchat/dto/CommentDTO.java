package com.hust.webchat.dto;

import com.hust.webchat.domain.Posting;
import lombok.Data;

@Data
public class CommentDTO extends AbstractDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Posting posting;

    private String content;

    private UserDTO userComment;
}
