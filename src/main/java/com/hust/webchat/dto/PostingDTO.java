package com.hust.webchat.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostingDTO extends AbstractDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer status;

    private String content;

    private String fileUrl;

    private String hashTag;

    private Integer type;

    private Long userId;

    private String description;

    private List<CommentDTO> comments = new ArrayList<>();

    private UserDTO userDTO;

    private Integer numberComment;

    private Long numberLike;

    private Boolean liked;
}
