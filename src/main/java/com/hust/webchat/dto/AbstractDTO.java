package com.hust.webchat.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class AbstractDTO implements Serializable {
    private String createdBy;

    private Long createdDate = Instant.now().getEpochSecond();

    private String lastModifiedBy;

    private Long lastModifiedDate = Instant.now().getEpochSecond();
}
