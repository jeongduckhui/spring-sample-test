# nexcore-migration-sample (Spring Boot 3.2.5 multi-module)

이 샘플은 Nexcore에서 보통 분리하는 레이어를 Spring Boot 멀티모듈로 옮길 때의 뼈대 예시입니다.

## Modules

- common-system: 시스템공통 (에러코드/예외/traceId/MDC/logback 기본)
- common-business: 업무공통 (업무 BaseService, 공통 도메인 구조)
- infra-batch: 기반공통(배치) - Spring Batch
- infra-mail: 기반공통(메일) - spring-boot-starter-mail
- infra-webservice: 기반공통(웹서비스/SOAP) - Spring Web Services
- app-api: 실행 애플리케이션(REST API)

## Run

```bash
./gradlew :app-api:bootRun
curl http://localhost:8080/api/ping
```
