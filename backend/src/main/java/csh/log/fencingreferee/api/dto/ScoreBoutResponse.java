package csh.log.fencingreferee.api.dto;

import java.util.List;

public record ScoreBoutResponse(
    String modelVersion,
    List<ScoringEventResponse> events
) {}