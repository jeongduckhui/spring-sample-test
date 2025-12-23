package com.example.demo.common.system.api;

import java.time.Instant;

public record ApiResponse<T>(
        boolean ok,
        String code,
        String message,
        String traceId,
        Instant timestamp,
        T data
) {
    public static <T> ApiResponse<T> ok(T data, String traceId) {
        return new ApiResponse<>(true, "OK", "SUCCESS", traceId, Instant.now(), data);
    }

    public static <T> ApiResponse<T> fail(String code, String message, String traceId) {
        return new ApiResponse<>(false, code, message, traceId, Instant.now(), null);
    }
}
