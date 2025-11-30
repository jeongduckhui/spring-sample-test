package com.example.demo.repository.query;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.query.QueryLog;

@Mapper
public interface QueryLogMapper {
    /** 로그 저장 **/
    void insertQueryLog(QueryLog log);
    
    /** 특정 매퍼 ID의 가장 최근 로그 조회 (Service ID로 조회) **/
    QueryLog selectLatestQueryLogByMapperId(String mapperId);
}