package com.jolupbisang.demo.infrastructure.aws.s3;

import com.jolupbisang.demo.global.properties.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3ClientUtil {

    private final S3Client s3Client;
    private final S3Properties s3Properties;

    public String uploadInputStream(String key, InputStream inputStream, long contentLength, String contentType) throws IOException {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucket())
                    .key(key)
                    .contentType(contentType)
                    .contentLength(contentLength)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));
            return s3Client.utilities().getUrl(builder -> builder.bucket(s3Properties.getBucket()).key(key)).toExternalForm();
        } catch (S3Exception e) {
            log.error("Error uploading input stream to S3: {}", e.getMessage());
            throw new IOException("Failed to upload input stream to S3", e);
        }
    }
}
