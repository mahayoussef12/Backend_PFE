package com.isima.projet.Facture;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Logger;

public class test {
    Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getName());
    private final Path rootLocation = Paths.get("upload-dir");


    public void store(MultipartFile file) {
        try {
            Path Pathfile = this.rootLocation.resolve(Objects.requireNonNull(file.getOriginalFilename()));
            Resource resource = new UrlResource(Pathfile.toUri());
            if (!resource.exists()) {
                Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
            }
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }
    }

    public Resource loadFile(String filename) {
        try {
            Path file = this.rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }
}
