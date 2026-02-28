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

    private String videoUrl;

    @Enumerated(EnumType.STRING)
    private BoutStatus status;

    private Instant createdAt = Instant.now();

    public void setVideoUrl(String videoUrl2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setVideoUrl'");
    }

    public void setStatus(BoutStatus pending) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStatus'");
    }

    public Object getVideoUrl() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVideoUrl'");
    }

    public Long getId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getId'");
    }

    public PathVariable getStatus() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStatus'");
    }

    // getters/setters
}
