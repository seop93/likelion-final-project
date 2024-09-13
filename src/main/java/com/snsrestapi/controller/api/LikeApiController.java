package com.snsrestapi.controller.api;

import com.snsrestapi.domain.dto.response.Response;
import com.snsrestapi.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class LikeApiController {

    private final LikeService likeService;

    @PostMapping("/{postId}/likes")
    public Response<String> createLike(@PathVariable("postId") Long postId, Authentication authentication) {
        return Response.success(likeService.createLike(postId, authentication.getName()));
    }

    @GetMapping("/{postsId}/likes")
    public Response<Long> countLikes(@PathVariable("postsId") Long postId) {
        return Response.success(likeService.countLikes(postId));
    }
}
