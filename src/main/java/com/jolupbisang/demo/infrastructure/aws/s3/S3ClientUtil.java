package com.jolupbisang.demo.infrastructure.aws.s3;

import com.jolupbisang.demo.global.properties.S3Properties;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3ClientUtil {

    private final S3Template s3Template;
    private final S3Properties s3Properties;

    public String uploadFile(String key, File file) throws IOException {
        return uploadFile(key, file.toPath());
    }

    public String uploadFile(String key, Path filePath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            Resource s3Resource = s3Template.upload(s3Properties.getBucket(), key, inputStream);
            URL url = s3Resource.getURL();
            return url.toString();
        }
    }

    public String uploadMultipartFile(String key, MultipartFile multipartFile) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Resource s3Resource = s3Template.upload(s3Properties.getBucket(), key, inputStream);
            URL url = s3Resource.getURL();
            return url.toString();
        }
    }

    public String uploadInputStream(String key, InputStream inputStream, long contentLength, String contentType) throws IOException {
        Resource s3Resource = s3Template.upload(s3Properties.getBucket(), key, inputStream);
        URL url = s3Resource.getURL();
        return url.toString();
    }
}
