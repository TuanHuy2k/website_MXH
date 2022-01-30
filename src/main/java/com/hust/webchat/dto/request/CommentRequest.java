package com.hust.webchat.dto.request;

import lombok.Data;

@Data
public class CommentRequest {
    private Long postingId;

    private String content;
}
