package com.example.demo.common.business.domain;

import java.time.Instant;

public record AuditFields(
        String createdBy,
        Instant createdAt,
        String updatedBy,
        Instant updatedAt
) {
    public static AuditFields empty() {
        return new AuditFields(null, null, null, null);
    }
}
