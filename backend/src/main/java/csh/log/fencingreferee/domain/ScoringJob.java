package csh.log.fencingreferee.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Enumerated;

public class ScoringJob {
    
    @Id
    @GeneratedValue
    private Long id;

    private Bout bout;

    private String modelVersion;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private Instant started_at;
    private Instant finished_at;

    private String errorMessage;

    private Instant createdAt = Instant.now();

    // getters/setters
    public void setBout(Bout bout) {
        this.bout = bout;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public void setStartedAt(Instant started_at) {
        this.started_at = started_at;
    }

    public void setFinishedAt(Instant finished_at) {
        this.finished_at = finished_at;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
}
