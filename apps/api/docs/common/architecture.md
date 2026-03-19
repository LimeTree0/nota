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

## 6. 테스트 전략 (TDD)

Red-Green-Refactor 사이클을 따른다.

```
Red    → 실패하는 테스트를 먼저 작성
Green  → 테스트를 통과하는 최소한의 코드 작성
Refactor → 코드를 개선하면서 테스트가 계속 통과하는지 확인
```

### 단위 테스트

비즈니스 로직이 포함된 레이어에 대해서만 단위 테스트를 작성한다.

| 레이어 | 단위 테스트 | 이유 |
|--------|:-----------:|------|
| **Domain** (Entity, Domain Service) | O | 핵심 비즈니스 로직 |
| **Application** (Service) | O | 유스케이스 로직, 의존성은 Mock 처리 |
| Presentation (Controller) | X | |
| Infrastructure | X | |

### 통합 테스트

실제 외부 시스템과의 연동이 필요한 경우에만 통합 테스트를 작성한다.

| 대상 | 통합 테스트 | 이유 |
|------|:-----------:|------|
| **Repository** | O | DB 저장/조회는 실제 연결해야 검증 가능 |
| 외부 API 클라이언트 | O | 실제 통신 결과 확인 필요 시 |

### 테스트 패키지 구조

프로덕션 코드와 동일한 패키지 구조를 따른다.

```
src/test/java/com/limecoding/api
├── book/
│   ├── application/
│   │   └── BookServiceTest.java          # 단위 테스트
│   ├── domain/
│   │   └── BookTest.java                 # 단위 테스트
│   └── infrastructure/
│       └── BookJpaRepositoryTest.java    # 통합 테스트
└── ...
```

---

## 7. 네이밍 컨벤션

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