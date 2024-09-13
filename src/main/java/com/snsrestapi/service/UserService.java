package com.snsrestapi.service;

import com.snsrestapi.domain.dto.request.user.UserChangeRoleRequest;
import com.snsrestapi.domain.entity.User;
import com.snsrestapi.enumerate.UserRole;
import com.snsrestapi.exception.AppException;
import com.snsrestapi.exception.ErrorCode;
import com.snsrestapi.repository.UserRepository;
import com.snsrestapi.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public User Join(String userName, String password) {

        validateDuplicatedUserName(userName);
        String encodedPassword = passwordEncoder.encode(password);
        User user = User.registerUser(userName, encodedPassword);
        return userRepository.save(user);

    }

    public String login(String userName, String password) {
        User findUser = findUserByUserName(userName);
        if (!passwordEncoder.matches(password, findUser.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
        }
        return tokenProvider.createToken(userName, findUser.getRole().getValue());
    }

    @Transactional
    public UserRole changeRole(Long id, UserChangeRoleRequest request) {
        User findUser = findByUserId(id);
        findUser.changeRole(request.role());
        return findUser.getRole();
    }


    private void validateDuplicatedUserName(String userName) {
        if (userRepository.findByUserName(userName).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage());
        }
    }

    private User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
            new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

    }

    private User findByUserId(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.USERNAME_NOT_FOUND, "변경하려는 유저가 존재하지 않습니다"));
    }

}
