package com.example.demo.app.health;

import com.example.demo.common.system.api.ApiResponse;
import com.example.demo.common.system.web.TraceIdFilter;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/api/ping")
    public ApiResponse<String> ping() {
        String traceId = MDC.get(TraceIdFilter.MDC_KEY);
        return ApiResponse.ok("pong", traceId);
    }
}
