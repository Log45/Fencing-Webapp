package csh.log.fencingreferee.service;

import java.util.List;

import org.springframework.stereotype.Service;

import csh.log.fencingreferee.api.dto.ScoringEventRequest;
import csh.log.fencingreferee.domain.Bout;
import csh.log.fencingreferee.domain.BoutStatus;
import csh.log.fencingreferee.domain.ScoringEvent;
import csh.log.fencingreferee.domain.ScoringSide;
import csh.log.fencingreferee.integration.MlInferenceClient;
import csh.log.fencingreferee.persistence.BoutRepository;
import csh.log.fencingreferee.persistence.ScoringEventRepository;
import jakarta.transaction.Transactional;

@Service
public class BoutService {

    private final BoutRepository boutRepo;
    private final ScoringEventRepository eventRepo;
    private final MlInferenceClient mlClient;

    public BoutService(
        BoutRepository boutRepo,
        ScoringEventRepository eventRepo,
        MlInferenceClient mlClient
    ) {
        this.boutRepo = boutRepo;
        this.eventRepo = eventRepo;
        this.mlClient = mlClient;
    }

    @Transactional
    public Bout createBout(String videoUrl) {
        Bout bout = new Bout();
        bout.setVideoUrl(videoUrl);
        bout.setStatus(BoutStatus.PENDING);
        return boutRepo.save(bout);
    }

    @Transactional
    public void scoreBout(Long boutId) {
        Bout bout = boutRepo.findById(boutId).orElseThrow();
        bout.setStatus(BoutStatus.PROCESSING);

        List<ScoringEventRequest> events = mlClient.scoreBout(bout.getVideoUrl());

        for (ScoringEventRequest e : events) {
            ScoringEvent event = new ScoringEvent();
            event.setBout(bout);
            event.setTimestampMs(e.timestampMs());
            event.setSide(ScoringSide.valueOf(e.side()));
            event.setConfidence(e.confidence());
            event.setModelVersion(e.modelVersion());
            event.setRawPayload(e.payload().toString());
            eventRepo.save(event);
        }

        bout.setStatus(BoutStatus.COMPLETED);
    }
}
