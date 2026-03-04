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
import jakarta.persistence.FetchType;

@Entity
public class Bout {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String videoObjectKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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

    public BoutStatus getStatus() {
        return this.status;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }
}
