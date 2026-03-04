package csh.log.fencingreferee.service;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Service;

import csh.log.fencingreferee.integration.S3StorageClient;

@Service
public class VideoStorageService {

    private static final String BUCKET = "fencing-videos";
    private static final Duration URL_EXPIRATION = Duration.ofMinutes(15);

    private final S3StorageClient storageClient;

    public VideoStorageService(S3StorageClient storageClient) {
        this.storageClient = storageClient;
    }

    public PresignedUpload generateUploadUrl(String originalFilename) {

        // Basic business logic
        String objectKey = "uploads/" + UUID.randomUUID() + "-" + originalFilename;

        URL url = storageClient.generatePresignedUploadUrl(
            BUCKET,
            objectKey,
            URL_EXPIRATION
        );

        return new PresignedUpload(objectKey, url.toString());
    }

    public record PresignedUpload(
        String objectKey,
        String uploadUrl
    ) {}
}