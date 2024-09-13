package com.snsrestapi.service;

import com.snsrestapi.domain.entity.User;
import com.snsrestapi.domain.entity.UserDetail;
import com.snsrestapi.exception.AppException;
import com.snsrestapi.exception.ErrorCode;
import com.snsrestapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, username + ErrorCode.USERNAME_NOT_FOUND.getMessage()));
        if (user != null) {
            return new UserDetail(user);
        }
        return null;
    }
}
