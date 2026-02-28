package csh.log.fencingreferee.domain;

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

    @Lob
    private String rawPayload; // JSON from ML service

    public void setBout(Bout bout2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBout'");
    }

    public void setTimestampMs(Object timestampMs2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setTimestampMs'");
    }

    public void setSide(ScoringSide valueOf) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSide'");
    }

    public void setConfidence(Object confidence2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setConfidence'");
    }

    public void setModelVersion(Object modelVersion2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setModelVersion'");
    }

    public void setRawPayload(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setRawPayload'");
    }

    // getters/setters
}
