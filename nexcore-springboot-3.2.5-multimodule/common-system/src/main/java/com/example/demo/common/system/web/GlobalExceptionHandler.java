package com.example.demo.common.system.web;

import com.example.demo.common.system.api.ApiResponse;
import com.example.demo.common.system.error.AppException;
import com.example.demo.common.system.error.CommonErrors;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleApp(AppException e) {
        String traceId = MDC.get(TraceIdFilter.MDC_KEY);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(e.code(), e.getMessage(), traceId));
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class, ConstraintViolationException.class })
    public ResponseEntity<ApiResponse<Void>> handleValidation(Exception e) {
        String traceId = MDC.get(TraceIdFilter.MDC_KEY);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(CommonErrors.BAD_REQUEST.code(), "Validation error", traceId));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception e, HttpServletRequest req) {
        String traceId = MDC.get(TraceIdFilter.MDC_KEY);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(CommonErrors.INTERNAL_ERROR.code(), CommonErrors.INTERNAL_ERROR.message(), traceId));
    }
}
