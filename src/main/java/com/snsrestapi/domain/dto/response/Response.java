package com.snsrestapi.domain.dto.response;

public record Response<T>(String resultCode, T result) {

    public static<T> Response<T> error(String resultCode, T result){
        return new Response<>(resultCode, result);
    }

    public static<T> Response<T> success(T result){
        return new Response<>("SUCCESS", result);
    }

}
