package com.example.demo.common.system.error;

public enum CommonErrors implements AppErrorCode {

    BAD_REQUEST("SYS-400", "잘못된 요청입니다."),
    UNAUTHORIZED("SYS-401", "인증이 필요합니다."),
    FORBIDDEN("SYS-403", "권한이 없습니다."),
    NOT_FOUND("SYS-404", "대상을 찾을 수 없습니다."),
    INTERNAL_ERROR("SYS-500", "시스템 오류가 발생했습니다.");

    private final String code;
    private final String message;

    CommonErrors(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override public String code() { return code; }
    @Override public String message() { return message; }
}
