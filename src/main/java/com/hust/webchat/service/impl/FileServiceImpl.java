package com.hust.webchat.service.impl;

import com.hust.webchat.config.StorageProperties;
import com.hust.webchat.domain.File;
import com.hust.webchat.repository.FileRepository;
import com.hust.webchat.service.FileService;
import com.hust.webchat.util.Constants;
import com.hust.webchat.util.MD5Util;
import com.hust.webchat.util.StringUtil;
import com.hust.webchat.web.error.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private static final String ENTITY_NAME = "FileService";

    private final FileRepository fileRepository;

    private final StorageProperties storageProperties;

    private final FileSystemStorageService fileSystemStorageService;

    public FileServiceImpl(FileRepository fileRepository, StorageProperties storageProperties, FileSystemStorageService fileSystemStorageService) {
        this.fileRepository = fileRepository;
        this.storageProperties = storageProperties;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    @Override
    public File ensureExisted(Long id) {
        File file = fileRepository.findById(id).orElseThrow(() -> new BadRequestAlertException("File not found", ENTITY_NAME,"fileNotFound"));
        file.setPublicUrl(storageProperties.getPublicStorageUrl());
        return file;
    }

    @Override
    public Resource viewFileImage(Long fileId) {
        File file = this.ensureExisted(fileId);
        String path = file.getPathFile();
        Resource resource = fileSystemStorageService.loadAsResource(path);
        return resource;
    }

    @Override
    public File uploadFile(MultipartFile file, Long ownerId, String ownerType) {
        String path;
        String md5Hashed = null;

        // format file
        String originalName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String destName = String.format("%s/%s", ownerType, ownerId);

        try {
            md5Hashed = MD5Util.MD5(file.getBytes());
        } catch (IOException e) {
            log.error("Gen MD5 error", e);
            new BadRequestAlertException("File upload invalid", ENTITY_NAME,"fileUploadInvalid");
        }

        // Lưu file
        String fileName = genNewFileName(originalName, md5Hashed);
        path = this.fileSystemStorageService.store(file, destName, fileName);

        File fileStored = File.builder()
                .fileName(originalName)
                .pathFile(path)
                .fileNameHash(fileName)
                .ownerId(ownerId)
                .type(ownerType)
                .status(Constants.ENTITY_STATUS.ACTIVE)
                .build();

        // save file to db
        fileRepository.save(fileStored);

        return fileStored;
    }

    /**
     * generate new file from hashed and originalName
     *
     * @param originalName
     * @param hashed
     * @return
     */
    public static String genNewFileName(String originalName, String hashed) {

        if (!StringUtils.hasLength(hashed)) {
            hashed = UUID.randomUUID().toString();
        }
        String newFileName;
        String fullName = StringUtil.getSafeFileName(Objects.requireNonNull(originalName));
        String fileType = "";
        if (StringUtils.hasText(fullName)) {
            int last = fullName.lastIndexOf(".");
            if (last >= 0) {
                fileType = fullName.substring(last);
            }
        }
        newFileName = String.format("%s-%s%s", hashed, UUID.randomUUID().toString().substring(0, 8), fileType);
        return newFileName;
    }


}
