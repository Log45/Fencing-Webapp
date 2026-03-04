package csh.log.fencingreferee.domain;

import java.time.Instant;

import org.springframework.web.bind.annotation.PathVariable;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Enumerated;

@Entity
public class Bout {

    @Id
    @GeneratedValue
    private Long id;

    private String videoObjectKey;

    @Enumerated(EnumType.STRING)
    private BoutStatus status;

    private Instant createdAt = Instant.now();

    // getters/setters

    public void setVideoObjectKey(String videoObjectKey) {
        this.videoObjectKey = videoObjectKey;
    }

    public void setStatus(BoutStatus status) {
        this.status = status;
    }

    public String getVideoObjectKey() {
        return this.videoObjectKey;
    }

    public Long getId() {
        return this.id;
    }

    public PathVariable getStatus() {
        return this.status;
    }
}
