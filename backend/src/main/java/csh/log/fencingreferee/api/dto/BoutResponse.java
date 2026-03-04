package csh.log.fencingreferee.api.dto;

import csh.log.fencingreferee.domain.BoutStatus;

public record BoutResponse(
    Long id,
    BoutStatus status
) {}
