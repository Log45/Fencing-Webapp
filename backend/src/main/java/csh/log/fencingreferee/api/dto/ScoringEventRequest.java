package csh.log.fencingreferee.api.dto;

public record ScoringEventRequest(
    long timestampMs,
    String side,
    double confidence,
    String modelVersion,
    Object payload
) {

}
