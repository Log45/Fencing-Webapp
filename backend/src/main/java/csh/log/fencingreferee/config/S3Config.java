package csh.log.fencingreferee.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Bean
    public S3Presigner s3Presigner(
        @Value("${s3.endpoint}") String endpoint,
        @Value("${s3.accessKey}") String accessKey,
        @Value("${s3.secretKey}") String secretKey,
        @Value("${s3.region}") String region
    ) {

        AwsBasicCredentials credentials =
            AwsBasicCredentials.create(accessKey, secretKey);

        return S3Presigner.builder()
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(
                StaticCredentialsProvider.create(credentials)
            )
            .region(Region.of(region))
            .serviceConfiguration(
                S3Configuration.builder()
                    .pathStyleAccessEnabled(true)
                    .build()
            )
            .build();
    }
}