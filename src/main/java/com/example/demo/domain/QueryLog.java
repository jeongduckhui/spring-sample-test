package com.example.demo.domain;

import lombok.Data;
import java.sql.Timestamp; // 또는 java.time.LocalDateTime 사용

@Data
public class QueryLog {
    private Long id;
    private String mapperId; // 여기서는 Service Method ID가 저장됨
    private String executedQuery;
    private int executionTimeMs;
    private Timestamp createdAt; // DB의 TIMESTAMP 타입과 매칭
}