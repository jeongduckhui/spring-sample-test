package com.example.demo.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
@Component
public class QueryLogInterceptor implements Interceptor {

    // Key: MapperId, Value: ExecutedSql (바인딩된 최종 쿼리)
    public static final ThreadLocal<ConcurrentHashMap<String, String>> queryLogThreadLocal = new ThreadLocal<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);

        String finalSql = getFinalSql(mappedStatement.getConfiguration(), boundSql, parameter);
        String mapperId = mappedStatement.getId();
        
        ConcurrentHashMap<String, String> logMap = queryLogThreadLocal.get();
        if (logMap == null) {
            logMap = new ConcurrentHashMap<>();
            queryLogThreadLocal.set(logMap);
        }
        logMap.put(mapperId, finalSql);

        // 실제 DB 작업 실행
        return invocation.proceed();
    }

    /** 파라미터가 바인딩된 최종 SQL 쿼리 문자열을 만듭니다. (핵심 로직) **/
    private String getFinalSql(Configuration configuration, BoundSql boundSql, Object parameterObject) {
        String sql = boundSql.getSql().replaceAll("\\s+", " ").trim();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        
        if (parameterMappings != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            
            for (ParameterMapping parameterMapping : parameterMappings) {
                if (parameterMapping.getMode() != org.apache.ibatis.mapping.ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }
                    
                    String paramValue = getParameterValue(value);
                    // SQL의 첫 번째 '?'를 치환된 값으로 대체 (정규 표현식 사용)
                    sql = sql.replaceFirst("\\?", paramValue); 
                }
            }
        }
        return sql;
    }
    
    /** 파라미터 값에 따라 SQL 문자열로 변환 (따옴표 처리) **/
    private String getParameterValue(Object obj) {
        if (obj == null) {
            return "NULL";
        }
        String value = obj.toString();
        
        if (obj instanceof String || obj instanceof java.util.Date || obj instanceof java.sql.Date || obj instanceof java.time.LocalDateTime) {
            // 문자열 및 날짜/시간 타입은 따옴표로 감쌈
            return "'" + value.replaceAll("'", "''") + "'";
        }
        return value; // 숫자 타입 등은 그대로 반환
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // NOP
    }
}