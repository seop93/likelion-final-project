package com.snsrestapi.controller.api;

import com.snsrestapi.domain.dto.request.user.UserChangeRoleRequest;
import com.snsrestapi.domain.dto.request.user.UserJoinRequest;
import com.snsrestapi.domain.dto.request.user.UserLoginRequest;
import com.snsrestapi.domain.dto.response.Response;
import com.snsrestapi.domain.dto.response.user.UserChangeRoleResponse;
import com.snsrestapi.domain.dto.response.user.UserJoinResponse;
import com.snsrestapi.domain.dto.response.user.UserLoginResponse;
import com.snsrestapi.domain.entity.User;
import com.snsrestapi.enumerate.UserRole;
import com.snsrestapi.service.UserService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User savedUser = userService.Join(request.userName(), request.password());
        return Response.success(new UserJoinResponse(savedUser.getId(), savedUser.getUserName()));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.userName(), request.password());
        return Response.success(new UserLoginResponse(token));
    }

    @PostMapping("/{id}/role/change")
    public Response<UserChangeRoleResponse> changeRole(@PathVariable("id") Long id, @RequestBody UserChangeRoleRequest request) {
        UserRole userRole = userService.changeRole(id, request);
        return Response.success(new UserChangeRoleResponse("변경되었습니다.", userRole));
    }



}
