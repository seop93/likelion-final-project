package com.snsrestapi.domain.dto.request.user;

import com.snsrestapi.enumerate.UserRole;

public record UserChangeRoleRequest(UserRole role) {
}
