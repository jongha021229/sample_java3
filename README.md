# sample_java3

Spring Boot 기반 Java 샘플 백엔드 프로젝트 (Course API)

## 프로젝트 요약

- 목적: Spring Boot 기반의 간단한 REST API 샘플
- 도메인: Course CRUD + 검색 + Health Check
- 현재 상태: 테스트 코드 포함

## 기술 스택/버전

- Java 17 toolchain
- Spring Boot 3.4.2
- Gradle Wrapper 8.7 설정
- Validation: `jakarta.validation`

## 실행

```bash
./gradlew bootRun
```

## 테스트

```bash
./gradlew test
```

## 빌드

```bash
./gradlew build
```

## 기능 명세(API)

### GET /health

응답:

```json
{ "status": "ok" }
```

### POST /courses

요청 예시:

```json
{ "title": "Spring Boot Basics", "fee": 99.0, "description": "Beginner course" }
```

검증:

- title: 공백 불가
- fee: 양수 필수

### GET /courses

전체 목록 반환

### GET /courses/{id}

- 존재 시 200 + course
- 미존재 시 404 + `{ "error": "not found" }`

### DELETE /courses/{id}

- 존재 시 200 + `{ "status": "deleted" }`
- 미존재 시 404 + `{ "error": "not found" }`

### GET /courses/search?q=키워드

title 기준 부분일치 검색(대소문자 무시)

## 데이터 저장 방식

- DB 없음, 메모리(Map) 저장
- 앱 재시작 시 데이터 초기화

## 참고: 의도적 취약점(하)

- `application.properties`에 `server.error.include-message=always`가 설정되어 있어, 에러 발생 시 내부 메시지가 클라이언트에 노출될 수 있습니다.
- 학습용 설정이며 운영 환경에서는 권장되지 않습니다.