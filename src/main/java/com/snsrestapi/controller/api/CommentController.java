package com.snsrestapi.controller.api;

import com.snsrestapi.domain.dto.request.comment.CommentRequest;
import com.snsrestapi.domain.dto.response.Response;
import com.snsrestapi.domain.dto.response.comment.CommentCreateResponse;
import com.snsrestapi.domain.dto.response.comment.CommentDeleteResponse;
import com.snsrestapi.domain.dto.response.comment.CommentModifyResponse;
import com.snsrestapi.domain.dto.response.comment.CommentResponse;
import com.snsrestapi.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> findAllComment(
            @PathVariable("postId") Long postId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(commentService.findAllComments(postId, pageable));
    }

    @PostMapping("/{postId}/comments")
    public Response<CommentCreateResponse> createComment(@PathVariable("postId") Long postId, @Valid @RequestBody CommentRequest request, Authentication authentication) {
        return Response.success(commentService.createComment(postId, request, authentication.getName()));
    }

    @PutMapping("/{postId}/comments/{id}")
    public Response<CommentModifyResponse> modifyComment(@PathVariable("postId") Long postId,
                                                         @PathVariable("id") Long commentId,
                                                         @Valid @RequestBody CommentRequest request,
                                                         Authentication authentication) {
        return Response.success(commentService.modifyComment(postId, commentId, request,authentication.getName()));
    }

    @DeleteMapping("/{postId}/comments/{id}")
    public Response<CommentDeleteResponse> deleteComment(@PathVariable("postId") Long postId,
                                                         @PathVariable("id") Long commentId,
                                                         Authentication authentication) {
        return Response.success(commentService.deleteComment(postId, commentId, authentication.getName()));
    }
}
