package com.snsrestapi.domain.dto.response.comment;

import com.snsrestapi.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentCreateResponse {
    private Long id;
    private String comment;
    private String userName;
    private Long postId;
    private LocalDateTime createdAt;

    public static CommentCreateResponse of(Comment comment) {
        return CommentCreateResponse.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userName(comment.getUser().getUserName())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
