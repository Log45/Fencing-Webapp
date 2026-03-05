package csh.log.fencingreferee.api.dto;

import java.util.Map;

public record ScoringEventRequest(
    long timestampMs,
    String side,
    double confidence,
    Map<String, Object> mlPayload
) {

}
