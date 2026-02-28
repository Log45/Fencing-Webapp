package csh.log.fencingreferee.domain;

import java.time.Instant;

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

    private String videoUrl;

    @Enumerated(EnumType.STRING)
    private BoutStatus status;

    private Instant createdAt = Instant.now();

    public void setVideoUrl(String videoUrl) {
    }

    public void setStatus(BoutStatus boutStatus) {
    }

    public String getVideoUrl() {
    }

    // getters/setters
}
