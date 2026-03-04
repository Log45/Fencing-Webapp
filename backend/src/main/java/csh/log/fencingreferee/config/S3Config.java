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

curl -X PUT \                                                                     
-T "/Users/log/Documents/code/Fencing-Webapp/videos/princeton-1.MOV" \
"http://localhost:9000/fencing-videos/users/1/bouts/1/princeton-1.MOV?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20260304T071823Z&X-Amz-SignedHeaders=host&X-Amz-Credential=backend-user%2F20260304%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Expires=900&X-Amz-Signature=cc11dd9b6edea05eebe4f48885d217cc0cb52a61e0938944c66e83c563a55be5"