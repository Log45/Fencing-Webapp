package csh.log.fencingreferee.persistence;

import java.util.List;xw
import org.springframework.data.jpa.repository.JpaRepository;
import csh.log.fencingreferee.domain.ScoringEvent;

public interface ScoringEventRepository extends JpaRepository<ScoringEvent, Long> {

    List<ScoringEvent> findByBoutIdOrderByTimestampMsAsc(Long boutId);
}
