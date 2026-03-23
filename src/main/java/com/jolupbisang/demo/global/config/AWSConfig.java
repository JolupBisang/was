package com.jolupbisang.demo.global.config;

import com.jolupbisang.demo.global.properties.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sfn.SfnClient;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class AWSConfig {

    private final AwsProperties awsProperties;

    @Bean
    public SfnClient sfnClient() {
        String region = awsProperties.getRegion();
        String accessKey = awsProperties.getCredentials().getAccessKey();
        String secretKey = awsProperties.getCredentials().getSecretKey();
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return SfnClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .httpClientBuilder(ApacheHttpClient.builder()
                        .socketTimeout(Duration.ofSeconds(300))   // 소켓 타임아웃 (예: 300초 = 5분)
                        .connectionTimeout(Duration.ofSeconds(10)) // 연결 타임아웃 (예: 10초)
                )
                .build();
    }

    @Bean
    public S3Client s3Client() {
        String region = awsProperties.getRegion();
        String accessKey = awsProperties.getCredentials().getAccessKey();
        String secretKey = awsProperties.getCredentials().getSecretKey();
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        String region = awsProperties.getRegion();
        String accessKey = awsProperties.getCredentials().getAccessKey();
        String secretKey = awsProperties.getCredentials().getSecretKey();
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
