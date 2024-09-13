package com.snsrestapi.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlamType {
    NEW_COMMENT_ON_POST("new comment!"),
    NEW_LIKE_ON_POST("new like!");
    private final String message;
}
