# 공통 API 설정 태스크

> Spring Boot API 프로젝트의 공통 설정 항목을 정리한 문서

---

## 1. 공통 응답 포맷 (Common Response Wrapper)

### 목적
모든 API 응답을 통일된 형태로 감싸서 프론트엔드에서 일관되게 처리할 수 있도록 한다.

### 응답 구조

```json
{
  "success": true,
  "data": { ... },
  "error": null
}
```

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "BOOK_NOT_FOUND",
    "message": "해당 책을 찾을 수 없습니다."
  }
}
```

### 구현 항목
- [ ] `ApiResponse<T>` 제네릭 응답 클래스 생성
- [ ] 성공 응답 정적 팩토리 메서드 (`ApiResponse.success(data)`)
- [ ] 실패 응답 정적 팩토리 메서드 (`ApiResponse.error(code, message)`)

---

## 2. 글로벌 예외 처리 (Global Exception Handler)

### 목적
예외 발생 시 일관된 에러 응답을 반환하고, 예외 처리 로직을 컨트롤러에서 분리한다.

### 구현 항목
- [ ] `@RestControllerAdvice` 기반 `GlobalExceptionHandler` 클래스 생성
- [ ] 커스텀 비즈니스 예외 클래스 (`BusinessException`) 생성
- [ ] 에러 코드 Enum (`ErrorCode`) 정의
- [ ] 주요 예외 처리 매핑

| 예외 | HTTP 상태 | 설명 |
|------|-----------|------|
| `BusinessException` | 에러 코드에 따라 다름 | 비즈니스 로직 예외 |
| `MethodArgumentNotValidException` | 400 | `@Valid` 검증 실패 |
| `HttpRequestMethodNotSupportedException` | 405 | 지원하지 않는 HTTP 메서드 |
| `Exception` | 500 | 예상치 못한 서버 에러 |

---

## 3. CORS 설정

### 목적
React 프론트엔드에서 API 서버로의 cross-origin 요청을 허용한다.

### 구현 항목
- [ ] `WebMvcConfigurer` 구현을 통한 CORS 설정
- [ ] 환경별 허용 Origin 분리 (dev: `localhost:5173`, prod: 실제 도메인)
- [ ] 허용 메서드: `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `OPTIONS`
- [ ] 허용 헤더: `Authorization`, `Content-Type` 등
- [ ] `allowCredentials` 설정

---

## 4. 프로필별 설정 분리

### 목적
개발/운영 환경에 따라 설정을 분리하여 관리한다.

### 파일 구조

```
src/main/resources/
├── application.yml          # 공통 설정
├── application-dev.yml      # 개발 환경 (H2, 로컬 설정)
├── application-prod.yml     # 운영 환경 (PostgreSQL, 실제 도메인)
└── application-test.yml     # 테스트 환경
```

### 구현 항목
- [ ] `application.properties` → `application.yml`로 전환
- [ ] `application-dev.yml` 생성 (H2 인메모리 DB, 로그 레벨 DEBUG)
- [ ] `application-prod.yml` 생성 (PostgreSQL, 로그 레벨 INFO)
- [ ] `application-test.yml` 생성 (테스트용 H2 설정)
- [ ] 기본 활성 프로필: `dev`

### 환경별 주요 설정

| 설정 항목 | dev | prod |
|-----------|-----|------|
| DB | H2 (인메모리) | PostgreSQL |
| DDL 전략 | `create-drop` | `validate` |
| 로그 레벨 | DEBUG | INFO |
| H2 Console | 활성화 | 비활성화 |

---

## 5. Swagger / OpenAPI 문서

### 목적
API 명세를 자동 생성하여 프론트엔드 개발자(또는 본인)가 API를 쉽게 파악할 수 있도록 한다.

### 구현 항목
- [ ] `springdoc-openapi` 의존성 추가
- [ ] OpenAPI 설정 클래스 생성 (API 제목, 설명, 버전)
- [ ] Swagger UI 접근 경로: `/swagger-ui.html`
- [ ] API 그룹 설정 (향후 도메인별 분리 대비)

### Swagger 설정 예시

| 항목 | 값 |
|------|-----|
| 제목 | Nota API |
| 설명 | 독서 기록 & 지식 베이스 API |
| 버전 | v1.0 |
| 기본 경로 | `/api/v1` |

---

## 6. 로깅 설정

### 목적
API 요청/응답을 로깅하여 디버깅과 모니터링을 용이하게 한다.

### 구현 항목
- [ ] 요청/응답 로깅 필터 또는 인터셉터 구현
- [ ] 로깅 항목: HTTP 메서드, URI, 상태 코드, 처리 시간
- [ ] 환경별 로그 레벨 설정
- [ ] 민감 정보(비밀번호 등) 로깅 제외

### 로그 포맷 예시

```
[REQUEST]  POST /api/v1/books - 200 OK (45ms)
[REQUEST]  GET  /api/v1/notes?bookId=1 - 200 OK (12ms)
[ERROR]    GET  /api/v1/books/999 - 404 Not Found (8ms)
```

---

## 구현 순서 (권장)

| 순서 | 항목 | 이유 |
|------|------|------|
| 1 | 프로필별 설정 분리 | 이후 설정들이 환경별로 달라지므로 먼저 구성 |
| 2 | 공통 응답 포맷 | 모든 API의 기본 틀 |
| 3 | 글로벌 예외 처리 | 공통 응답 포맷과 함께 에러 처리 통일 |
| 4 | CORS 설정 | 프론트엔드 연동 준비 |
| 5 | Swagger / OpenAPI | API 개발 시 즉시 문서 확인 가능 |
| 6 | 로깅 설정 | 개발 편의를 위한 마무리 |

---

> 이 문서는 구현 진행에 따라 체크리스트를 업데이트합니다.