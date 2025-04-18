package com.hacknovation.systemservice.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonClient {

    @Value("${spring.AWS_CONFIG.KEY}")
    private String accessKey;
    @Value("${spring.AWS_CONFIG.SECRET}")
    private String secretKey;
    @Value("${spring.AWS_CONFIG.REGION}")
    private String region;

    @Bean
    public AmazonS3 s3client() {

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
//                .withAccelerateModeEnabled(true)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();

    }

}
