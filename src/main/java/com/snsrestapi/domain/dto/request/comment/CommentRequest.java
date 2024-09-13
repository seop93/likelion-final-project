package com.snsrestapi.domain.dto.request.comment;

import jakarta.validation.constraints.NotEmpty;

public record CommentRequest(@NotEmpty String comment) {
}
