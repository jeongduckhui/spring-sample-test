package com.example.demo.aop;

import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.demo.domain.query.QueryLog;
import com.example.demo.interceptor.QueryLogInterceptor; // Interceptor import
import com.example.demo.repository.query.QueryLogMapper;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class QueryLoggingAspect {

    private static final Logger queryFileLogger = LoggerFactory.getLogger("jdbc.sqltiming");
    
    // 로그 저장용 매퍼 (AOP 대상 아님, 안전)
    private final QueryLogMapper queryLogMapper; 

    /**
     * AOP 포인트컷: com.example.demo.service 패키지 및 하위의 모든 *Service 클래스 메서드를 대상
     */
    @Around("execution(* com.example.demo.service..*Service.*(..))")
    public Object logAndSaveQuery(ProceedingJoinPoint joinPoint) throws Throwable {
        
        long startTime = System.currentTimeMillis();
        
        String serviceMethodId = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        
        // 1. Service 시작 파일 로깅
        queryFileLogger.info("SERVICE_START|{}", serviceMethodId);
        
        Object result;
        try {
            // 2. Service 메서드 실행 (내부에서 Interceptor가 쿼리를 ThreadLocal에 저장)
            result = joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            int executionTimeMs = (int) (endTime - startTime);
            
            // 3. Interceptor가 저장한 모든 쿼리 정보를 획득
            ConcurrentHashMap<String, String> queryMap = QueryLogInterceptor.queryLogThreadLocal.get();
            
            // 4. DB 로그 저장
            if (queryMap != null && !queryMap.isEmpty()) {
                
                // 해당 Service 트랜잭션 내에서 실행된 모든 쿼리를 하나의 로그에 합칩니다.
                StringBuilder fullQueryLog = new StringBuilder("Service: " + serviceMethodId + " executed.\n");
                
                queryMap.forEach((mapperId, executedQuery) -> {
                    fullQueryLog.append("--- Mapper: ").append(mapperId).append(" ---\n");
                    fullQueryLog.append(executedQuery).append("\n");
                });
                
                QueryLog log = new QueryLog();
                log.setMapperId(serviceMethodId); 
                log.setExecutionTimeMs(executionTimeMs);
                log.setExecutedQuery(fullQueryLog.toString());
                
                try {
                    // ★★★ QueryLogMapper를 통해 DB에 로그 저장 ★★★
                    queryLogMapper.insertQueryLog(log);
                } catch (Exception e) {
                    System.err.println("\n-------- DB LOG SAVE FAILED ---------");
                    System.err.println("Service ID: " + serviceMethodId);
                    System.err.println("Error Message: " + e.getMessage()); 
                    e.printStackTrace();
                    System.err.println("--------------------------------------\n");
                }
            } else {
                 queryFileLogger.warn("No DB operations for service: {}", serviceMethodId);
            }
            
            // 5. AOP 실행이 끝난 후 ThreadLocal 제거 (필수 cleanup)
            QueryLogInterceptor.queryLogThreadLocal.remove();
        }
        
        return result;
    }
}