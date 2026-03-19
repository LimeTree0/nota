# DB 스키마 설계

> v0.1 · 2026년 3월  
> PostgreSQL + pgvector

---

## 1. 테이블 관계 요약

```
books
  └── reading_sessions (1권 N회 읽기)
        ├── notes        (회차별 노트)
        └── highlights   (회차별 하이라이트)

notes ↔ tags (note_tags 접속 테이블, 다대다)
```

---

## 2. 테이블 상세

### books

책 정보 저장. ISBN 기준으로 중복 없이 관리.

| 컬럼 | 타입 | 설명 |
|------|------|------|
| `id` | `bigserial` PK | 자동 증가 PK |
| `isbn` | `varchar` | ISBN (nullable — 없는 책도 등록 가능) |
| `title` | `varchar` | 제목 |
| `author` | `varchar` | 저자 |
| `publisher` | `varchar` | 출판사 |
| `published_date` | `date` | 출판일 |
| `cover_image_url` | `text` | 표지 이미지 URL |
| `description` | `text` | 책 소개 |
| `source` | `varchar` | 등록 출처 (`kakao` / `google` / `manual`) |
| `created_at` | `timestamp` | 생성일시 |

---

### reading_sessions

같은 책을 읽을 때마다 새 회독 생성. 2회독/3회독을 `read_count`로 구분.

| 컬럼 | 타입 | 설명 |
|------|------|------|
| `id` | `bigserial` PK | 자동 증가 PK |
| `book_id` | `bigint` FK | `books.id` 참조 |
| `read_count` | `int` | 몇 번째 읽기 (1, 2, 3...) |
| `status` | `varchar` | `reading` / `completed` / `want` |
| `started_at` | `date` | 읽기 시작일 |
| `finished_at` | `date` | 완독일 (nullable) |
| `memo` | `text` | 회독 전체 메모 (nullable) |
| `created_at` | `timestamp` | 생성일시 |

> `status = want`인 경우 노트/하이라이트 생성 불가 (앱 레벨에서 제어)

---

### notes

회독에 연결된 노트 또는 책 없는 자유 노트.

| 컬럼 | 타입 | 설명 |
|------|------|------|
| `id` | `bigserial` PK | 자동 증가 PK |
| `reading_session_id` | `bigint` FK | `reading_sessions.id` 참조 (nullable — 자유 노트) |
| `title` | `varchar` | 노트 제목 |
| `content` | `text` | 마크다운 본문 |
| `note_type` | `varchar` | `free` / `template` / `tech` |
| `embedding` | `vector` | AI 임베딩 벡터 (3단계 이후 활성화) |
| `created_at` | `timestamp` | 생성일시 |
| `updated_at` | `timestamp` | 수정일시 |

> `reading_session_id = null` → 자유 노트

---

### highlights

회독 중 인용구와 내 코멘트 저장.

| 컬럼 | 타입 | 설명 |
|------|------|------|
| `id` | `bigserial` PK | 자동 증가 PK |
| `reading_session_id` | `bigint` FK | `reading_sessions.id` 참조 |
| `content` | `text` | 인용구 본문 |
| `page_number` | `int` | 페이지 번호 (nullable) |
| `my_comment` | `text` | 내 생각/코멘트 (nullable) |
| `embedding` | `vector` | AI 임베딩 벡터 (3단계 이후 활성화) |
| `created_at` | `timestamp` | 생성일시 |

---

### tags

노트에 자유롭게 붙이는 분류 레이블.

| 컬럼 | 타입 | 설명 |
|------|------|------|
| `id` | `bigserial` PK | 자동 증가 PK |
| `name` | `varchar` | 태그 이름 (unique) |
| `created_at` | `timestamp` | 생성일시 |

---

### note_tags

노트 ↔ 태그 다대다 접속 테이블.

| 컬럼 | 타입 | 설명 |
|------|------|------|
| `note_id` | `bigint` FK | `notes.id` 참조 |
| `tag_id` | `bigint` FK | `tags.id` 참조 |

> PK = (`note_id`, `tag_id`) 복합키

---

## 3. 관계 정리

| 관계 | 설명 |
|------|------|
| `books` → `reading_sessions` | 1:N — 한 책을 여러 번 읽을 수 있음 |
| `reading_sessions` → `notes` | 1:N — 한 회독에 노트 여러 개 |
| `reading_sessions` → `highlights` | 1:N — 한 회독에 하이라이트 여러 개 |
| `notes` ↔ `tags` | N:M — `note_tags` 접속 테이블로 연결 |

---

## 4. 설계 결정 사항

- **2회독/3회독 지원**: `books`에 중복 저장하지 않고 `reading_sessions`로 회독 분리
- **자유 노트**: `notes.reading_session_id = null`로 책 없는 노트 처리
- **pgvector 준비**: `notes.embedding`, `highlights.embedding` 컬럼 미리 추가 — 3단계 AI/RAG 때 활성화
- **태그는 노트에만**: 현재는 노트에만 태그 연결 (책에는 미적용)
- **source 구분**: 카카오/구글/수동 등록을 `books.source`로 추적

---

## 5. 로드맵별 DB 변경 계획

| 단계 | 변경 내용 |
|------|-----------|
| 1단계 (MVP) | 위 스키마 그대로 (`embedding` 컬럼은 생성만, 미사용) |
| 2단계 (고도화) | AI 대화 저장용 `chat_sessions` 테이블 추가 고려 |
| 3단계 (AI/RAG) | `embedding` 컬럼 실제 사용, pgvector 인덱스 생성 |
| 4단계 (멀티유저) | 모든 테이블에 `user_id` FK 추가, RLS(Row Level Security) 적용 |
