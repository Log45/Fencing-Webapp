package csh.log.fencingreferee.service;

import java.util.List;
import java.time.Instant;

import org.springframework.stereotype.Service;

import csh.log.fencingreferee.api.dto.ScoringEventRequest;
import csh.log.fencingreferee.domain.Bout;
import csh.log.fencingreferee.domain.User;
import csh.log.fencingreferee.domain.JobStatus;
import csh.log.fencingreferee.domain.BoutStatus;
import csh.log.fencingreferee.domain.ScoringEvent;
import csh.log.fencingreferee.domain.ScoringJob;
import csh.log.fencingreferee.domain.ScoringSide;
import csh.log.fencingreferee.integration.MlInferenceClient;
import csh.log.fencingreferee.persistence.BoutRepository;
import csh.log.fencingreferee.persistence.ScoringEventRepository;
import csh.log.fencingreferee.persistence.ScoringJobRepository;
import csh.log.fencingreferee.service.VideoStorageService;
import csh.log.fencingreferee.persistence.ScoringJobRepository;

import jakarta.transaction.Transactional;

@Service
public class BoutService {

    private final BoutRepository boutRepo;
    private final ScoringEventRepository eventRepo;
    private final ScoringJobRepository jobRepo;
    private final MlInferenceClient mlClient;
    private final VideoStorageService storageService;

    public BoutService(
        BoutRepository boutRepo,
        ScoringEventRepository eventRepo,
        MlInferenceClient mlClient,
        ScoringJobRepo jobRepo,
        VideoStorageService storageService
    ) {
        this.boutRepo = boutRepo;
        this.eventRepo = eventRepo;
        this.mlClient = mlClient;
        this.storageService = storageService;
        this.jobRepo = jobRepo;
    }

    @Transactional
    public Bout createBout(User user) {
        Bout bout = new Bout();
        bout.setUser(user);
        bout.setStatus(BoutStatus.UPLOAD_PENDING);
        return boutRepo.save(bout);
    }

    @Transactional
    public PresignedUpload createUploadUrl(User user, String filename) {

        Bout bout = createBout(user);

        String objectKey = String.format(
            "users/%d/bouts/%d/%s",
            user.getId(),
            bout.getId(),
            filename
        );

        bout.setVideoObjectKey(objectKey);

        String url = storageService.generateUploadUrl(objectKey);

        return new PresignedUpload(
            bout.getId(),
            objectKey,
            url
        );
    }

    @Transactional
    public void scoreBout(Long boutId) {
        Bout bout = boutRepo.findById(boutId).orElseThrow();
        bout.setStatus(BoutStatus.PROCESSING);

        ScoringJob job = new ScoringJob();
        job.setBout(bout);
        job.setStartedAt(Instant.now());
        job.setStatus(JobStatus.PROCESSING);
        jobRepo.save(job);

        try {
            ScoreBoutResponse response =
                mlClient.scoreBout(bout.getVideoObjectKey());

            job.setModelVersion(response.modelVersion());

            for (ScoringEventRequest e : response.events()) {

                ScoringEvent event = new ScoringEvent();
                event.setBout(bout);
                event.setJob(job);
                event.setTimestampMs(e.timestampMs());
                event.setSide(ScoringSide.valueOf(e.side()));
                event.setConfidence(e.confidence());
                event.setRawPayload(e.payload().toString());
            
                eventRepo.save(event);
            }

            job.setStatus(JobStatus.COMPLETED);
            job.setFinishedAt(Instant.now());
            bout.setStatus(BoutStatus.COMPLETED);

        } catch (Exception ex) {

            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(ex.getMessage());
            job.setFinishedAt(Instant.now());

            bout.setStatus(BoutStatus.FAILED); // might want to throw an exception in the future
        }
    }
}