# Book 도메인 기능 목록

> 책 정보 관리 관련 API 스펙

| 번호 | 기능 | 파일 | 상태 |
|------|------|------|------|
| 01 | 책 등록 | [01-book-create.md](01-book-create.md) | 작성 완료 |
| 02 | 책 목록 조회 | [02-book-list.md](02-book-list.md) | 작성 완료 |
| 03 | 책 상세 조회 | [03-book-detail.md](03-book-detail.md) | 작성 완료 |
| 04 | 책 수정 | [04-book-update.md](04-book-update.md) | 작성 완료 |
| 05 | 책 삭제 | [05-book-delete.md](05-book-delete.md) | 작성 완료 |

## 공통 사항

- **Base URL:** `/api/v1`
- **응답 형식:** `ApiResponse<T>` 래퍼
- **DB 테이블:** `books`

### BookResponse (공통 응답 DTO)

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | `Long` | 책 ID |
| `title` | `String` | 제목 |
| `author` | `String` | 저자 |
| `isbn` | `String` | ISBN (nullable) |
| `publisher` | `String` | 출판사 (nullable) |
| `publishedDate` | `LocalDate` | 출판일 (nullable) |
| `coverImageUrl` | `String` | 표지 이미지 URL (nullable) |
| `description` | `String` | 책 소개 (nullable) |
| `source` | `String` | 등록 출처 (아래 표 참고) |
| `createdAt` | `LocalDateTime` | 생성일시 |

### BookSource (등록 출처)

책 정보가 어떤 경로로 등록되었는지 구분한다.

| 값 | 설명 | 적용 시점 |
|------|------|-----------|
| `manual` | 사용자가 직접 수동 입력 | 1단계 (MVP) |
| `kakao` | 카카오 책 검색 API로 자동 입력 | 2단계 (고도화) |
| `google` | Google Books API로 자동 입력 | 2단계 (고도화) |

- 1단계에서는 `manual`만 사용된다.
- Enum(`BookSource`)으로 관리하여 잘못된 값 유입을 방지한다.

### DB 스키마 (books)

| 컬럼 | 타입 | 제약 | 설명 |
|------|------|------|------|
| `id` | `bigserial` | PK | 자동 증가 |
| `isbn` | `varchar(13)` | nullable | ISBN |
| `title` | `varchar(255)` | NOT NULL | 제목 |
| `author` | `varchar(255)` | NOT NULL | 저자 |
| `publisher` | `varchar(255)` | nullable | 출판사 |
| `published_date` | `date` | nullable | 출판일 |
| `cover_image_url` | `text` | nullable | 표지 이미지 URL |
| `description` | `text` | nullable | 책 소개 |
| `source` | `varchar(20)` | NOT NULL, default `manual` | 등록 출처 |
| `created_at` | `timestamp` | NOT NULL | 생성일시 |

### 구현 파일 목록

```
com.limecoding.api.book
├── presentation/
│   ├── BookController.java
│   ├── BookCreateRequest.java
│   ├── BookUpdateRequest.java
│   └── BookResponse.java
├── application/
│   └── BookService.java
├── domain/
│   ├── Book.java
│   ├── BookSource.java         # Enum (KAKAO, GOOGLE, MANUAL)
│   └── BookRepository.java
└── infrastructure/
    └── BookJpaRepository.java
```
