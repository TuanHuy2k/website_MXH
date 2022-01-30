package com.hust.webchat.service;

import com.hust.webchat.domain.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * kiem tra file da ton tai
     *
     * @param id
     * @return
     */
    File ensureExisted(Long id);

    /**
     * view file
     *
     * @param fileId
     * @return
     */
    Resource viewFileImage(Long fileId);

    /**
     * upload file
     *
     * @param file
     * @param ownerId
     * @param ownerType
     * @return
     */
    File uploadFile(MultipartFile file, Long ownerId, String ownerType);
}
