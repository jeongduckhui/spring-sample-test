package com.example.demo.common.business.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * Nexcore에서 흔히 보이는 "업무공통 ServiceBase" 역할.
 * - 여기엔 공통 트랜잭션 정책 / 공통 검증 / 공통 로깅 등을 모으기 좋음.
 */
@Transactional
public abstract class BusinessService {
    // 공통 기능을 필요한 만큼 추가
}
