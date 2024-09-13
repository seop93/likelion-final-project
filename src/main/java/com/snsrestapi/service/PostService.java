package com.snsrestapi.service;

import com.snsrestapi.domain.dto.request.post.PostRequest;
import com.snsrestapi.domain.dto.response.post.PostCreateResponse;
import com.snsrestapi.domain.dto.response.post.PostDeleteResponse;
import com.snsrestapi.domain.dto.response.post.PostResponse;
import com.snsrestapi.domain.dto.response.post.PostUpdateResponse;
import com.snsrestapi.domain.entity.Post;
import com.snsrestapi.domain.entity.User;
import com.snsrestapi.enumerate.UserRole;
import com.snsrestapi.exception.AppException;
import com.snsrestapi.exception.ErrorCode;
import com.snsrestapi.repository.PostRepository;
import com.snsrestapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private static final String SUCCESS_MESSAGE = "포스트 등록 완료";
    private static final String UPDATE_MESSAGE = "포스트 수정 완료";
    private static final String DELETE_MESSAGE = "포스트 삭제 완료";

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostCreateResponse createPost(PostRequest request, String userName){
        User findUser = findUser(userName);
        Post createPost = Post.createPost(request.title(), request.body(), findUser);
        Post savedPost = postRepository.save(createPost);
        return new PostCreateResponse(SUCCESS_MESSAGE, savedPost.getId());
    }

    @Transactional
    public PostDeleteResponse deletePost(Long postId, String userName){
        Post post = findByPostAuthorizedUser(postId, userName);
        post.deletePost();
        return new PostDeleteResponse(DELETE_MESSAGE, postId);
    }


    public PostResponse findOne(Long postId) {
        Post post = findPost(postId);
        return PostResponse.of(post);
    }

    public Page<PostResponse> findOwn(String userName, Pageable pageable) {
        User user = findUser(userName);
        Page<Post> myPosts = postRepository.findAllByUserId(user.getId(), pageable);
        return PostResponse.of(myPosts);
    }

    public Page<PostResponse> findAll(Pageable pageable) {
        return PostResponse.of(postRepository.findAll(pageable));
    }

    @Transactional
    public PostUpdateResponse updatePost(Long id, PostRequest request, String userName){
        Post findPost = findByPostAuthorizedUser(id, userName);
        findPost.updatePost(request.title(), request.body());
        return new PostUpdateResponse(UPDATE_MESSAGE, findPost.getId());
    }







    private Post findByPostAuthorizedUser(Long postId, String userName) {
        User user = findUser(userName);
        Post post = findPost(postId);


        if (Objects.equals(post.getUser().getId(), user.getId()) || Objects.equals(user.getRole(), UserRole.ADMIN)) {
            return post;
        } else {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }



    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
    }


}
