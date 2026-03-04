package csh.log.fencingreferee.integration;

import java.net.URL;
import java.time.Duration;

import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
public class S3StorageClient {

    private final S3Presigner presigner;

    public S3StorageClient(S3Presigner presigner) {
        this.presigner = presigner;
    }

    public URL generatePresignedUploadUrl(
        String bucket,
        String objectKey,
        Duration expiration
    ) {

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(objectKey)
            .build();

        PutObjectPresignRequest presignRequest =
            PutObjectPresignRequest.builder()
                .signatureDuration(expiration)
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presigned =
            presigner.presignPutObject(presignRequest);

        return presigned.url();
    }
    
    public URL generatePresignedDownloadUrl(
        String bucket,
        String objectKey,
        Duration expiration
    ) {

        GetObjectRequest getObjectRequest =
            GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();

        GetObjectPresignRequest presignRequest =
            GetObjectPresignRequest.builder()
                .signatureDuration(expiration)
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest =
            presigner.presignGetObject(presignRequest);

        return presignedRequest.url();
    }
}