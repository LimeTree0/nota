package com.limecoding.api.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private HttpStatus status;
    private T data;
    private String error;

    private ApiResponse(HttpStatus status, T data, String error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK, data, null);
    }

    public static <T> ApiResponse<T> success(HttpStatus status, T data) {
        return new ApiResponse<>(status, data, null);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String error) {
        return new ApiResponse<>(status, null, error);
    }
}
