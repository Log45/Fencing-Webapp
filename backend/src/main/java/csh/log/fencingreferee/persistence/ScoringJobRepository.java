package csh.log.fencingreferee.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import csh.log.fencingreferee.domain.ScoringJob;

public interface ScoringJobRepository extends JpaRepository<ScoringJob, Long> {
}