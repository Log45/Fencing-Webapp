package csh.log.fencingreferee.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Enumerated;

@Entity
public class ScoringJob {
    
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bout_id", nullable = false)
    private Bout bout;


    private String modelVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @Column(nullable = false)
    private Instant startedAt;

    private Instant finishedAt;

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

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getId() {
        return this.id;
    }

    public Bout getBout() {
        return this.bout;
    }

    public String getModelVersion() {
        return this.modelVersion;
    }

    public JobStatus getStatus() {
        return this.status;
    }

    public Instant getStartedAt() {
        return this.startedAt;
    }

    public Instant getFinishedAt() {
        return this.finishedAt;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }    

    public Instant getCreatedAt() {
        return this.createdAt;
    }
}
