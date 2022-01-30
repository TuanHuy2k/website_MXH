package com.hust.webchat.dto;

import lombok.Data;

@Data
public class EmotionDTO extends AbstractDTO {

    private Long id;

    private Long userId;

    private String type;

    private Long commentId;

    private Long postingId;
}
