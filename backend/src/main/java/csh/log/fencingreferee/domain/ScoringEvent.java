package csh.log.fencingreferee.domain;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ScoringEvent {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bout_id", nullable = false)
    private Bout bout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private ScoringJob job;

    @Column(nullable = false)
    private long timestampMs;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ScoringSide side;

    private double confidence;

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String mlPayload; // store full ML output

    // getters/setters

    public void setBout(Bout bout) {
        this.bout = bout;
    }

    public void setJob(ScoringJob job) {
        this.job = job;
    }

    public void setTimestampMs(long timestampMs) {
        this.timestampMs = timestampMs;
    }

    public void setSide(ScoringSide side) {
        this.side = side;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public void setRawPayload(String payload) {
        this.mlPayload = payload;
    }

    public Long getId() {
        return this.id;
    }

    public Bout getBout() {
        return this.bout;
    }

    public ScoringJob getJob() {
        return this.job;
    }

    public long getTimestampMs() {
        return this.timestampMs;
    }

    public ScoringSide getSide() {
        return this.side;
    }

    public double getConfidence() {
        return this.confidence;
    }

    public String getMlPayload() {
        return this.mlPayload;
    }
}
