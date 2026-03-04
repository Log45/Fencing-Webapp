package csh.log.fencingreferee.domain;

import java.time.Instant;

import org.springframework.web.bind.annotation.PathVariable;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Enumerated;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String email;

    // private String passwordHash; later we will do authentication

    private Instant createdAt = Instant.now();

    // getters/setters
}
