package com.somacode.celmybell.service.tool;

import com.somacode.celmybell.config.exception.BadRequestException;
import com.somacode.celmybell.service.LogService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class StorageTool {

    private static final String STORAGE_PATH = "/static/storage";


    public void save(Resource resource, String folder, String name) {
        try {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Error when save resource with name null or empty");
            }
            Path path = Paths.get(getRootPath() + STORAGE_PATH + (folder == null ? "" : folder));
            if (!Files.exists(path)) {
                LogService.out.debug("Create directories: " + path);
                Files.createDirectories(path);
            }

            Path resolve = path.resolve(StringUtils.cleanPath(name));
            Files.copy(resource.getInputStream(), resolve, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(MultipartFile multipartFile, String folder) {
        save(multipartFile, folder, null);
    }

    public void save(MultipartFile multipartFile, String folder, String name) {
        try {
            if (multipartFile.isEmpty()) {
                throw new IllegalArgumentException("Error multipartFile is empty");
            }
            String originalFilename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            if (originalFilename.contains("..")) {
                throw new IllegalArgumentException("Cannot store file with relative path outside current directory " + originalFilename);
            }

            Path path = Paths.get(getRootPath() + STORAGE_PATH + (folder == null ? "" : folder));
            if (!Files.exists(path)) {
                LogService.out.debug("Create directories: " + path);
                Files.createDirectories(path);
            }

            Path resolve = path.resolve(StringUtils.cleanPath(name != null ? name : multipartFile.getOriginalFilename()));
            Files.copy(multipartFile.getInputStream(), resolve, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(byte[] bytes, String fileName, String folder, String name) {
        try {
            String originalFilename = StringUtils.cleanPath(fileName);
            if (originalFilename.contains("..")) {
                throw new IllegalArgumentException("Cannot store file with relative path outside current directory " + originalFilename);
            }

            Path path = Paths.get(getRootPath() + STORAGE_PATH + (folder == null ? "" : folder));
            if (!Files.exists(path)) {
                LogService.out.debug("Create directories: " + path);
                Files.createDirectories(path);
            }

            Path resolve = path.resolve(StringUtils.cleanPath(name != null ? name : fileName));
            Files.copy(new ByteArrayInputStream(bytes), resolve, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Resource load(String folder, String name) {
        Path path = Paths.get(getRootPath() + STORAGE_PATH + (folder == null ? "" : folder));
        Path resolve = path.resolve(StringUtils.cleanPath(name));
        if (!Files.exists(path)) {
            throw new BadRequestException("Directory doesn't exist");
        } else if (!Files.exists(resolve)) {
            throw new BadRequestException("File doesn't exist");
        }

        try {
            return new UrlResource(resolve.toUri());
        } catch (MalformedURLException e) {
            throw new BadRequestException("File doesn't exist");
        }
    }

    public void delete(String folder, String name) {
        Path path = Paths.get(getRootPath() + STORAGE_PATH + (folder == null ? "" : folder));
        Path resolve = path.resolve(StringUtils.cleanPath(name));
        if (!Files.exists(path)) {
            throw new BadRequestException("Directory doesn't exist");
        } else if (!Files.exists(resolve)) {
            throw new BadRequestException("File doesn't exist");
        }

        try {
            Files.delete(resolve);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String getRootPath() {
        Path path = Paths.get("").toAbsolutePath();
        return path.toString().replace("\\", "/");
    }

}