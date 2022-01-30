package com.hust.webchat.dto;

import lombok.Data;

@Data
public class ActivityLogDTO extends AbstractDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer actionType;

    private String oldContent;

    private String content;

    private Long userId;
}
