# API 엔드포인트 설계

> v0.1 · 2026년 3월  
> Base URL: `/api/v1`

---

## 1. Books

| Method | URL | 설명 |
|--------|-----|------|
| `GET` | `/books` | 책 목록 조회 (필터: status, 페이지네이션) |
| `GET` | `/books/{id}` | 책 상세 조회 |
| `POST` | `/books` | 책 등록 (수동 입력) |
| `PUT` | `/books/{id}` | 책 정보 수정 |
| `DELETE` | `/books/{id}` | 책 삭제 |
| `GET` | `/books/search/external` | 외부 API 검색 (카카오 → Google fallback) |

### Query Parameters
- `GET /books` → `?status=reading\|completed\|want` `&page=0&size=20`
- `GET /books/search/external` → `?q=클린코드` `&type=title\|isbn`

### Request/Response 예시

**POST /books**
```json
{
  "isbn": "9788966261208",
  "title": "클린 코드",
  "author": "로버트 C. 마틴",
  "publisher": "인사이트",
  "published_date": "2013-12-24",
  "cover_image_url": "https://...",
  "description": "...",
  "source": "kakao"
}
```

---

## 2. Reading Sessions

| Method | URL | 설명 |
|--------|-----|------|
| `GET` | `/books/{bookId}/sessions` | 특정 책의 회독 목록 조회 |
| `GET` | `/sessions/{id}` | 회독 상세 조회 |
| `POST` | `/books/{bookId}/sessions` | 새 회독 시작 |
| `PUT` | `/sessions/{id}` | 회독 정보 수정 (상태, 날짜 등) |
| `DELETE` | `/sessions/{id}` | 회독 삭제 |

### Request/Response 예시

**POST /books/{bookId}/sessions**
```json
{
  "status": "reading",
  "started_at": "2026-03-19"
}
```

**PUT /sessions/{id}** (완독 처리)
```json
{
  "status": "completed",
  "finished_at": "2026-03-25",
  "memo": "두 번째 읽으니 의존성 부분이 더 잘 이해됐다"
}
```

---

## 3. Notes

| Method | URL | 설명 |
|--------|-----|------|
| `GET` | `/notes` | 전체 노트 목록 (필터: type, tag, sessionId) |
| `GET` | `/notes/{id}` | 노트 상세 조회 |
| `POST` | `/notes` | 노트 생성 (자유 노트 or 회독 연결) |
| `PUT` | `/notes/{id}` | 노트 수정 |
| `DELETE` | `/notes/{id}` | 노트 삭제 |
| `GET` | `/sessions/{sessionId}/notes` | 특정 회독의 노트 목록 |

### Query Parameters
- `GET /notes` → `?type=free\|template\|tech` `&tagId=1` `&sessionId=3` `&page=0&size=20`

### Request/Response 예시

**POST /notes**
```json
{
  "reading_session_id": 1,
  "title": "함수는 한 가지만 해야 한다",
  "content": "## 핵심 요약\n함수는 한 가지 일만 해야 한다...\n```java\npublic void sendEmail() { ... }\n```",
  "note_type": "tech",
  "tag_ids": [1, 3]
}
```

> `reading_session_id`가 null이면 자유 노트로 저장

---

## 4. Highlights

| Method | URL | 설명 |
|--------|-----|------|
| `GET` | `/highlights` | 전체 하이라이트 목록 (필터: sessionId) |
| `GET` | `/highlights/{id}` | 하이라이트 상세 조회 |
| `POST` | `/highlights` | 하이라이트 저장 |
| `PUT` | `/highlights/{id}` | 하이라이트 수정 |
| `DELETE` | `/highlights/{id}` | 하이라이트 삭제 |
| `GET` | `/sessions/{sessionId}/highlights` | 특정 회독의 하이라이트 목록 |

### Request/Response 예시

**POST /highlights**
```json
{
  "reading_session_id": 1,
  "content": "나쁜 코드에 주석을 달지 마라. 새로 짜라.",
  "page_number": 68,
  "my_comment": "주석보다 코드 자체를 읽기 쉽게 만드는 게 먼저다"
}
```

---

## 5. Tags

| Method | URL | 설명 |
|--------|-----|------|
| `GET` | `/tags` | 태그 전체 목록 |
| `POST` | `/tags` | 태그 생성 |
| `DELETE` | `/tags/{id}` | 태그 삭제 |
| `POST` | `/notes/{noteId}/tags/{tagId}` | 노트에 태그 추가 |
| `DELETE` | `/notes/{noteId}/tags/{tagId}` | 노트에서 태그 제거 |

### Request/Response 예시

**POST /tags**
```json
{
  "name": "클린코드"
}
```

---

## 6. 검색

| Method | URL | 설명 |
|--------|-----|------|
| `GET` | `/search` | 통합 키워드 검색 (책 + 노트 + 하이라이트) |
| `GET` | `/search/notes` | 노트만 키워드 검색 |
| `GET` | `/search/highlights` | 하이라이트만 키워드 검색 |

### Query Parameters
- `GET /search` → `?q=의존성 주입` `&type=notes\|highlights\|books`

### Response 예시

**GET /search?q=의존성 주입**
```json
{
  "notes": [
    {
      "id": 3,
      "title": "의존성 역전 원칙",
      "content_preview": "...고수준 모듈은 저수준 모듈에 의존하면 안 된다...",
      "book_title": "클린 코드",
      "read_count": 1
    }
  ],
  "highlights": [
    {
      "id": 7,
      "content": "추상화에 의존하라. 구체화에 의존하지 마라.",
      "page_number": 150,
      "book_title": "클린 코드"
    }
  ]
}
```

---

## 7. 공통 응답 형식

### 성공
```json
{
  "status": 200,
  "data": { ... }
}
```

### 실패
```json
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "해당 노트를 찾을 수 없습니다."
}
```

### 페이지네이션
```json
{
  "status": 200,
  "data": {
    "content": [ ... ],
    "page": 0,
    "size": 20,
    "total_elements": 42,
    "total_pages": 3,
    "last": false
  }
}
```

---

## 8. HTTP 상태 코드 규칙

| 상황 | 코드 |
|------|------|
| 조회 성공 | `200 OK` |
| 생성 성공 | `201 Created` |
| 삭제 성공 | `204 No Content` |
| 유효성 오류 | `400 Bad Request` |
| 리소스 없음 | `404 Not Found` |
| 서버 오류 | `500 Internal Server Error` |

---

> 2단계 이후 추가 예정: `POST /notes/{id}/chat` (AI 소크라테스식 대화), `GET /search/semantic` (벡터 기반 의미 검색)
