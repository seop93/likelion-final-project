package com.snsrestapi.controller.api;

import com.snsrestapi.domain.dto.request.post.PostRequest;
import com.snsrestapi.domain.dto.response.Response;
import com.snsrestapi.domain.dto.response.post.PostCreateResponse;
import com.snsrestapi.domain.dto.response.post.PostDeleteResponse;
import com.snsrestapi.domain.dto.response.post.PostResponse;
import com.snsrestapi.domain.dto.response.post.PostUpdateResponse;
import com.snsrestapi.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController {

    private final PostService postService;

    @GetMapping
    public Response<Page<PostResponse>> findAll(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable) {
        return Response.success(postService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public Response<PostResponse> findOne(@PathVariable("id") Long id) {
        return Response.success(postService.findOne(id));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> findOwn(Authentication authentication, @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(postService.findOwn(authentication.getName(), pageable));
    }

    @PostMapping
    public Response<PostCreateResponse> save(@RequestBody PostRequest request, Authentication authentication) {
        return Response.success(postService.createPost(request, authentication.getName()));
    }

    @PutMapping("/{id}")
    public Response<PostUpdateResponse> update(@PathVariable("id") Long id, @RequestBody PostRequest request, Authentication authentication) {
        log.info("title = {}, body = {}", request.title(), request.body());
        return Response.success(postService.updatePost(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public Response<PostDeleteResponse> delete(@PathVariable("id") Long id, Authentication authentication) {
        return Response.success(postService.deletePost(id, authentication.getName()));
    }


}
