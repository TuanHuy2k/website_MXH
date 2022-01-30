package com.hust.webchat.service.impl;

import com.hust.webchat.config.StorageProperties;
import com.hust.webchat.service.StorageService;
import com.hust.webchat.util.MD5Util;
import com.hust.webchat.util.StringUtil;
import com.hust.webchat.web.error.BadRequestAlertException;
import com.hust.webchat.web.error.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {
    private final Logger log = LoggerFactory.getLogger(FileSystemStorageService.class);

    private final String rootLocation;

    private final StorageProperties storageProperties;


    public FileSystemStorageService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
        this.rootLocation = storageProperties.getFolderUpload();
    }

    private String dateFolderPath() {
        String format = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return "/" + sdf.format(new Date()).replace("\\", "/") + "/";
    }

    @Override
    public String store(MultipartFile file) {
        String newFileName = "";
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            String fullName = StringUtil.getSafeFileName(Objects.requireNonNull(file.getOriginalFilename()));
            String fileType = "";
            if (StringUtils.hasText(fullName)) {
                int last = fullName.lastIndexOf(".");
                if (last >= 0) {
                    fileType = fullName.substring(last);
                }
            }
            newFileName = MD5Util.MD5(file.getBytes()) + UUID.randomUUID().toString().substring(0, 10) + fileType;

            String folder = this.dateFolderPath();
            Path path = Paths.get(rootLocation + folder + newFileName);

            if (!Files.exists(Paths.get(rootLocation + folder))) {
                new File(rootLocation + folder).mkdirs();
            }
            Files.copy(file.getInputStream(), path);
            return path.toString();
        } catch (FileAlreadyExistsException e) {
            log.error("store file error", e);
            return newFileName;
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public String store(MultipartFile file, String ownerFolderPath, String fileName) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }

            String folder = rootLocation + "/" + ownerFolderPath + this.dateFolderPath();

            Path path = Paths.get(folder + fileName);

            if (!Files.exists(Paths.get(folder))) {
                new File(folder).mkdirs();
            }
            Files.copy(file.getInputStream(), path);

            return path.toString();
        } catch (FileAlreadyExistsException e) {
            log.error("store file error", e);
            throw new BadRequestAlertException("Invalid input","", "invalidInput");
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            Path rootPath = Paths.get(this.storageProperties.getFolderUpload());
            return Files.walk(rootPath, 1)
                    .filter(path -> !path.equals(rootPath))
                    .map(rootPath::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public InputStreamResource download(String path) {
        try {
            log.info(storageProperties.getFolderUpload());
            File file = ResourceUtils.getFile(path);

            byte[] data = FileUtils.readFileToByteArray(file);

            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));

            return new InputStreamResource(inputStream);
        } catch (IOException e) {
            log.error("Error: ", e);
            throw new BadRequestAlertException("Cannot read file","", "cannot read file");
        }

    }

    @Override
    public Path load(String filename) {
        Path path = Paths.get(this.storageProperties.getFolderUpload());
        return path.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filePath) {
        try {
            Path file = Paths.get(filePath);
            return new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            throw new BadRequestAlertException("Cannot read file" + filePath,"", "cannot read file");
        }
    }
}
