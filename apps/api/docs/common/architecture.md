# 아키텍처 설계 — DDD (Domain-Driven Design)

> Nota API 프로젝트의 패키지 구조 및 레이어 설계

---

## 1. 레이어 구조

```
Presentation (Controller)
    ↓ DTO
Application (Service)
    ↓ Domain Object
Domain (Entity, Repository Interface, Domain Service)
    ↓
Infrastructure (Repository 구현, 외부 API 연동)
```

| 레이어 | 역할 | 포함 요소 |
|--------|------|-----------|
| **Presentation** | HTTP 요청/응답 처리 | Controller, Request/Response DTO |
| **Application** | 유스케이스 조율, 트랜잭션 관리 | Application Service |
| **Domain** | 핵심 비즈니스 로직 | Entity, Value Object, Repository Interface, Domain Service |
| **Infrastructure** | 기술 구현 세부사항 | JPA Repository 구현, 외부 API 클라이언트 |

---

## 2. 의존성 방향

```
Presentation → Application → Domain ← Infrastructure
```

- **Domain은 어떤 레이어에도 의존하지 않는다** (가장 안쪽)
- Infrastructure는 Domain의 Repository 인터페이스를 구현한다 (의존성 역전)
- Presentation은 Application만 호출하고, Domain을 직접 참조하지 않는다

---

## 3. 패키지 구조

도메인별로 패키지를 나누고, 각 도메인 안에서 레이어를 분리한다.

```
com.limecoding.api
├── common/                          # 공통 모듈
│   ├── response/                    # ApiResponse, ErrorResponse
│   ├── exception/                   # GlobalExceptionHandler, BusinessException, ErrorCode
│   └── config/                      # CORS, Swagger 등 설정
│
├── book/                            # 책 도메인
│   ├── presentation/
│   │   ├── BookController.java
│   │   ├── BookCreateRequest.java
│   │   └── BookResponse.java
│   ├── application/
│   │   └── BookService.java
│   ├── domain/
│   │   ├── Book.java                # Entity
│   │   ├── ReadingStatus.java       # Value Object (Enum)
│   │   └── BookRepository.java      # Interface
│   └── infrastructure/
│       └── BookJpaRepository.java   # JPA 구현체
│
├── note/                            # 노트 도메인
│   ├── presentation/
│   ├── application/
│   ├── domain/
│   └── infrastructure/
│
├── highlight/                       # 하이라이트 도메인
│   ├── presentation/
│   ├── application/
│   ├── domain/
│   └── infrastructure/
│
└── tag/                             # 태그 도메인
    ├── presentation/
    ├── application/
    ├── domain/
    └── infrastructure/
```

---

## 4. 바운디드 컨텍스트 (Bounded Context)

현재 프로젝트의 도메인 경계를 다음과 같이 정의한다.

```
┌─────────────────────────────────────────────┐
│              독서 관리 컨텍스트               │
│                                             │
│   Book ──< Note                             │
│     │                                       │
│     └──< Highlight                          │
│                                             │
│   Tag >──< Note (다대다)                     │
│   Tag >──< Book (다대다)                     │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│         AI 대화 컨텍스트 (2단계 이후)          │
│                                             │
│   Conversation ──< Message                  │
│        │                                    │
│        └── → Note 저장                       │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│         사용자 컨텍스트 (4단계 이후)           │
│                                             │
│   User ── Authentication                    │
└─────────────────────────────────────────────┘
```

- 1단계(MVP)에서는 **독서 관리 컨텍스트**에 집중한다
- 컨텍스트 간 통신은 Application Service 레벨에서 처리한다

---

## 5. 레이어별 규칙

### Presentation
- Controller는 Application Service만 호출한다
- Request DTO → Domain 변환은 Application Service에서 수행한다
- 모든 응답은 `ApiResponse<T>`로 감싼다

### Application
- 트랜잭션 경계를 관리한다 (`@Transactional`)
- 도메인 객체를 조율하고 유스케이스를 수행한다
- 다른 도메인의 Application Service를 호출할 수 있다

### Domain
- 비즈니스 로직은 Entity와 Domain Service에 위치한다
- Repository는 **인터페이스만** 정의한다
- Spring 등 프레임워크 의존성을 최소화한다 (JPA 어노테이션은 허용)

### Infrastructure
- Domain의 Repository 인터페이스를 JPA로 구현한다
- 외부 API 연동 (ISBN 검색 등)을 담당한다

---

## 6. 네이밍 컨벤션

| 구분 | 패턴 | 예시 |
|------|------|------|
| Entity | 단수 명사 | `Book`, `Note` |
| Repository Interface | `{Entity}Repository` | `BookRepository` |
| JPA 구현체 | `{Entity}JpaRepository` | `BookJpaRepository` |
| Application Service | `{Entity}Service` | `BookService` |
| Controller | `{Entity}Controller` | `BookController` |
| Request DTO | `{Entity}{Action}Request` | `BookCreateRequest` |
| Response DTO | `{Entity}Response` | `BookResponse` |
| Value Object / Enum | 의미를 나타내는 명사 | `ReadingStatus` |

---

> 이 문서는 프로젝트 진행에 따라 업데이트합니다.