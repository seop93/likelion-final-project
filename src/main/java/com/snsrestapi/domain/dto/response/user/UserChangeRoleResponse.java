package com.snsrestapi.domain.dto.response.user;

import com.snsrestapi.enumerate.UserRole;

public record UserChangeRoleResponse(String message, UserRole userRole) {
}
