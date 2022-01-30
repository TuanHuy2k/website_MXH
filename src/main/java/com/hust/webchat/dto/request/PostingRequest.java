package com.hust.webchat.dto.request;

import lombok.Data;

@Data
public class PostingRequest {
    private String content;

    private Long fileId;
}
