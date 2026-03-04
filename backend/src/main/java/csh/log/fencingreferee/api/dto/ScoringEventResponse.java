package csh.log.fencingreferee.api.dto;

public record ScoringEventResponse(
    long timestampMs,
    String side,
    double confidence
) {}
