package com.hust.webchat.dto;

import lombok.Data;

@Data
public class FileDTO extends AbstractDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String fileName;

    private String pathFile;

    private String fileNameHash;

    private Integer status;

    private Long ownerId;
}
