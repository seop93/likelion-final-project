package com.snsrestapi.config;

import com.snsrestapi.utils.JwtTokenFilter;
import com.snsrestapi.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final String[] PERMIT_URL = {
            "/api/v1/users/join", "/api/v1/users/login", "/api/v1/hello"};

    private final String[] PERMIT_URL_SWAGGER = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/swagger/**",
            "/swagger-ui.html/**",
            "/swagger-ui/**",
            "/swagger-ui"
    };



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http

                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))

                .authorizeRequests(auth -> auth
                        .requestMatchers(PERMIT_URL).permitAll()
                        .requestMatchers(PERMIT_URL_SWAGGER).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/v1/users/**/role/change")).hasRole("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(new JwtTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }




}
