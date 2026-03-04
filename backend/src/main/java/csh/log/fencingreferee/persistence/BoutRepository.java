package csh.log.fencingreferee.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import csh.log.fencingreferee.domain.Bout;

public interface BoutRepository extends JpaRepository<Bout, Long> {

    List<Bout> findByUserIdOrderByIdDesc(Long userId);

}