package csh.log.fencingreferee.service;

import java.net.URL;
import java.time.Duration;

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

    public String generateUploadUrl(String objectKey) {
        URL url = storageClient.generatePresignedUploadUrl(
            BUCKET,
            objectKey,
            URL_EXPIRATION
        );
    
        return url.toString();
    }

    public String generateDownloadUrl(String objectKey) {
        URL url = storageClient.generatePresignedDownloadUrl(
            BUCKET,
            objectKey,
            URL_EXPIRATION
        );

        return url.toString();
    }

    public record PresignedUpload(
        Long boutId,
        String objectKey,
        String uploadUrl
    ) {}
}