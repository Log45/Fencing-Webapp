package csh.log.fencingreferee.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;

@Entity
public class ScoringEvent {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bout bout;

    private long timestampMs;

    @Enumerated(EnumType.STRING)
    private ScoringSide side;

    private double confidence;

    private String modelVersion;

    @Column(columnDefinition = "jsonb")
    private String mlPayload; // store full ML output

    // getters/setters

    public void setBout(Bout bout) {
        this.bout = bout;
    }

    public void setTimestampMs(long timestampMs) {
        this.timestampMs = timestampMs;
    }

    public void setSide(ScoringSide side) {
        this.side = side;
    }

    public void setConfidence(double confidence) {
        this.confidence=confidence;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public void setRawPayload(String payload) {
        this.mlPayload = payload;
    }


}
