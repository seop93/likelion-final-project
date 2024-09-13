package com.snsrestapi.domain.dto.response.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snsrestapi.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponse {

    private Long id;
    private String comment;
    private String userName;
    private Long postId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static Page<CommentResponse> of(Page<Comment> comments) {
        return comments.map(comment -> CommentResponse.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userName(comment.getUser().getUserName())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .build());
    }

}
