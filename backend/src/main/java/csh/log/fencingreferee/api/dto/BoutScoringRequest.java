package csh.log.fencingreferee.api.dto;

import java.util.List;

public record BoutScoringRequest(
    Long boutId,
    List<ScoringEventRequest> scoringEvents
) {
    
}
