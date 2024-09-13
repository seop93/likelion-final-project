package com.snsrestapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 유효하지 않은 사용자가 보호된 리소스에 접근할때 EntryPoint
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

        // HTTP 상태 코드 401 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        // JSON 형태의 에러 메시지 작성
        Map<String, Object> data = new HashMap<>();
        data.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        data.put("message", "Unauthorized: Authentication is required to access this resource.");
        data.put("error", authException.getMessage());

        // JSON 출력
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(data));
    }
}
