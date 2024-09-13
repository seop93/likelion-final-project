package com.snsrestapi.service;

import com.snsrestapi.domain.entity.Alarm;
import com.snsrestapi.domain.entity.Like;
import com.snsrestapi.domain.entity.Post;
import com.snsrestapi.domain.entity.User;
import com.snsrestapi.enumerate.AlamType;
import com.snsrestapi.exception.AppException;
import com.snsrestapi.exception.ErrorCode;
import com.snsrestapi.repository.AlarmRepository;
import com.snsrestapi.repository.LikeRepository;

import com.snsrestapi.repository.PostRepository;
import com.snsrestapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private static final String SUCCESS_LIKE_MESSAGE = "좋아요를 눌렀습니다.";
    private static final String DUPLICATE_LIKE_USER_MESSAGE = "이미 좋아요를 누른 유저입니다.";

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AlarmRepository alarmRepository;

    @Transactional
    public String createLike(Long postId, String userName) {
        User user = findUser(userName);
        Post post = findPost(postId);
        duplicateLike(user, post);
        likeRepository.save(Like.createLike(post, user));
        saveNewLikeAlarm(postId, userName);
        return SUCCESS_LIKE_MESSAGE;
    }

    public Long countLikes(Long postId) {
        Post post = findPost(postId);
        return likeRepository.countByPost(post);
    }

    private void saveNewLikeAlarm(Long postId, String userName) {
        Long fromId = findUser(userName).getId();
        User postWriterUser = findPost(postId).getUser(); // 알람은 외래키인 유저도 찾아야함 작성자
        alarmRepository.save(Alarm.createdAlarm(postId, fromId, AlamType.NEW_LIKE_ON_POST, AlamType.NEW_LIKE_ON_POST.getMessage(), postWriterUser));

    }

    private void duplicateLike(User user, Post post) {
        likeRepository.findByUserAndPost(user, post).ifPresent(like ->
        { throw new AppException(ErrorCode.DUPLICATED_USER_NAME, DUPLICATE_LIKE_USER_MESSAGE);});

    }

    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(()
        -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(()
        -> new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
    }

}
