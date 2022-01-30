package com.hust.webchat.web.rest;

import com.hust.webchat.domain.File;
import com.hust.webchat.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api")
public class FileResource {

    private final FileService fileService;

    public FileResource(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/public/files/{fileId}/view")
    ResponseEntity<?> viewFileImage(@PathVariable("fileId") Long fileId) {
        Resource resource = fileService.viewFileImage(fileId);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PostMapping("/files/upload")
    ResponseEntity<File> fileUpload(@RequestParam(name = "file") MultipartFile file,
                                    @RequestParam(value = "ownerId") Long ownerId,
                                    @RequestParam(value = "ownerType") String ownerType) {
        return ResponseEntity.ok(fileService.uploadFile(file, ownerId, ownerType));
    }
}
