package com.example.demo.common.system.error;

public class AppException extends RuntimeException {

    private final String code;

    public AppException(AppErrorCode error) {
        super(error.message());
        this.code = error.code();
    }

    public AppException(AppErrorCode error, Throwable cause) {
        super(error.message(), cause);
        this.code = error.code();
    }

    public String code() {
        return code;
    }
}
