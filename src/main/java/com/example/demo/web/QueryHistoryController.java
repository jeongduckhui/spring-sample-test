package com.example.demo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.QueryLog;
import com.example.demo.repository.mybatis.QueryLogMapper; 
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/query")
@RequiredArgsConstructor
public class QueryHistoryController {

    private final QueryLogMapper queryLogMapper;
    
    /**
     * Service 메서드 ID로 로그를 조회합니다. (AOP가 Service 단위로 기록)
     * 예시 호출: /query/get/com.example.demo.service.impl.CustomerServiceImpl.getCustomerName
     */
    @GetMapping("/get/{serviceMethodId:.+}")
    public String getQueryByMapperId(@PathVariable("serviceMethodId") String serviceMethodId) { 
        
        QueryLog latestLog = queryLogMapper.selectLatestQueryLogByMapperId(serviceMethodId);
        
        if (latestLog == null) {
            return "DB에서 Service 메서드 ID (" + serviceMethodId + ")에 해당하는 실행 기록을 찾을 수 없습니다.";
        }
        
        return String.format(
            "--- DB LOG RESULT (Service Unit) ---\n" +
            "[%s]\n" + 
            " - Service ID: %s\n" + 
            " - 실행 시간: %d ms\n" + 
            " - 기록된 쿼리: \n%s", 
            latestLog.getCreatedAt(),
            latestLog.getMapperId(),
            latestLog.getExecutionTimeMs(),
            latestLog.getExecutedQuery()
        );
    }
}