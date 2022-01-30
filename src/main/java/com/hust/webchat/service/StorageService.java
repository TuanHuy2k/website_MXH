package com.hust.webchat.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    String store(MultipartFile file);

    String store(MultipartFile filePart, String ownerFolderPath, String fileName);

    Path load(String filename);

    Resource loadAsResource(String filename);

    Stream<Path> loadAll();

    InputStreamResource download(String filename);
}
