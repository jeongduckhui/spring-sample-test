package com.example.demo.web;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.query.QueryLog;
import com.example.demo.repository.query.QueryLogMapper;

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
    @GetMapping("/getbydb/{serviceMethodId:.+}")
    public String getQueryByDb(@PathVariable("serviceMethodId") String serviceMethodId) { 
        
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
    
    @GetMapping("/get/{mapperId:.+}") 
    public String getQueryByFile(@PathVariable("mapperId") String mapperId) {
        
        return findRecentQueryInLogFile("logs/query_history.log", mapperId);
    }
    
    /**
     * 로그 파일 전체를 역순 탐색하여 특정 매퍼 ID와 연결된 가장 최근의 바인딩된 쿼리를 반환합니다.
     */
    private String findRecentQueryInLogFile(String logFilePath, String targetMapperId) {
        File logFile = new File(logFilePath);
        
        if (!logFile.exists()) {
            return "쿼리 로그 파일이 존재하지 않습니다: " + logFilePath;
        }

        try {
            List<String> allLines = FileUtils.readLines(logFile, "UTF-8");
            
            // 파일 전체를 역순으로 탐색합니다.
            for (int i = allLines.size() - 1; i >= 0; i--) {
                String currentLine = allLines.get(i);
                
                // 1. 현재 줄이 AOP로 기록된 Mapper ID 시작점인지 확인
                if (currentLine.contains("MAPPER_START|") && currentLine.contains(targetMapperId)) {
                    
                    // 2. 다음 줄 (i+1)이 Log4jdbc 쿼리 로그인지 확인
                    if (i + 1 < allLines.size()) {
                        String queryLog = allLines.get(i + 1);
                        
                        // 쿼리 로그가 "select" 등의 키워드를 포함하는지 최종 확인 (로그 포맷 오염 방지)
                        if (queryLog.contains("select ") || queryLog.contains("update ") || queryLog.contains("insert ")) {
                            
                            // 3. 시간 정보를 제거하고 쿼리 메시지 부분만 반환
                            int separatorIndex = queryLog.indexOf('|');
                            if (separatorIndex != -1 && separatorIndex + 1 < queryLog.length()) {
                                return queryLog.substring(separatorIndex + 1).trim();
                            }
                            return queryLog.trim();
                        }
                    }
                    // AOP 로그는 찾았지만 쿼리가 바로 다음에 오지 않았을 경우
                    return "매퍼 ID는 찾았으나, 다음 줄에서 유효한 쿼리 로그를 찾을 수 없습니다.";
                }
            }
            
            return "로그 파일에서 매퍼 ID (" + targetMapperId + ")에 해당하는 실행 기록을 찾을 수 없습니다.";
            
        } catch (IOException e) {
            e.printStackTrace(); 
            return "쿼리 로그 파일 읽기 오류: " + e.getMessage();
        }
    }
}






