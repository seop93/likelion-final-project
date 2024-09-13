package com.snsrestapi.domain.dto.response.error;

import com.snsrestapi.exception.ErrorCode;

public record ErrorResponse(ErrorCode errorCode, String message) {

}
