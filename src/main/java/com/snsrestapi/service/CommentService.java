package com.snsrestapi.service;

import com.snsrestapi.domain.dto.request.comment.CommentRequest;
import com.snsrestapi.domain.dto.response.comment.CommentCreateResponse;
import com.snsrestapi.domain.dto.response.comment.CommentDeleteResponse;
import com.snsrestapi.domain.dto.response.comment.CommentModifyResponse;
import com.snsrestapi.domain.dto.response.comment.CommentResponse;
import com.snsrestapi.domain.entity.Alarm;
import com.snsrestapi.domain.entity.Comment;
import com.snsrestapi.domain.entity.Post;
import com.snsrestapi.domain.entity.User;
import com.snsrestapi.enumerate.AlamType;
import com.snsrestapi.exception.AppException;
import com.snsrestapi.exception.ErrorCode;
import com.snsrestapi.repository.AlarmRepository;
import com.snsrestapi.repository.CommentRepository;
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
@Transactional
public class CommentService {

    private static final String DELETE_COMMENT_MESSAGE = "댓글 삭제 완료";
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    @Transactional(readOnly = true)
    public Page<CommentResponse> findAllComments(Long postId, Pageable pageable) {
        validateExistPost(postId);
        return CommentResponse.of(commentRepository.findAllByPostId(postId, pageable));
    }

    public CommentCreateResponse createComment(Long postId, CommentRequest request, String userName) {
        Post post = findPost(postId);
        User user = findUser(userName);
        Comment savedComment = commentRepository.save(Comment.creatComment(request.comment(), post, user));
        saveNewAlarmComment(postId, user);
        return CommentCreateResponse.of(savedComment);

    }

    public CommentModifyResponse modifyComment(Long postId, Long commentId, CommentRequest request, String userName) {
        validateExistPost(postId);
        Comment comment = findCommentByAuthorizedUser(commentId, userName);
        comment.updateComment(request.comment());
        return CommentModifyResponse.of(comment);
    }

    public CommentDeleteResponse deleteComment(Long postId, Long commentId, String userName) {
        validateExistPost(postId);
        Comment comment = findCommentByAuthorizedUser(commentId, userName);
        commentRepository.delete(comment);
        return new CommentDeleteResponse(DELETE_COMMENT_MESSAGE, commentId);
    }

    private void saveNewAlarmComment(Long postId, User user) {
        Long userId = user.getId();
        User postWriterUser = findPost(postId).getUser();
        alarmRepository.save(Alarm.createdAlarm( postId, userId, AlamType.NEW_COMMENT_ON_POST, AlamType.NEW_COMMENT_ON_POST.getMessage(), postWriterUser));
    }

    private void validateExistPost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage());
        }
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
    }

    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.DUPLICATED_USER_NAME.getMessage()));
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.DUPLICATED_USER_NAME.getMessage()));
    }

    private Comment findCommentByAuthorizedUser(Long commentId, String userName) {
        User user = findUser(userName);
        Comment comment = findComment(commentId);
        if (Objects.equals(user.getId(), comment.getUser().getId())) {
            return comment;
        } else {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }


}
