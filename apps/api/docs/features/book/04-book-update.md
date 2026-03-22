# 04. 책 수정

> `PUT /api/v1/books/{id}`

---

## 요청

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | `Long` | 책 ID |

**Request Body** — 전체 덮어쓰기 (PUT)

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `title` | `String` | O | 제목 |
| `author` | `String` | O | 저자 |
| `isbn` | `String` | X | ISBN |
| `publisher` | `String` | X | 출판사 |
| `publishedDate` | `LocalDate` | X | 출판일 |
| `coverImageUrl` | `String` | X | 표지 이미지 URL |
| `description` | `String` | X | 책 소개 |
| `source` | `String` | X | 등록 출처 |

```json
{
  "title": "클린 코드 (수정판)",
  "author": "로버트 C. 마틴",
  "isbn": "9788966261208",
  "publisher": "인사이트",
  "publishedDate": "2013-12-24",
  "coverImageUrl": "https://...",
  "description": "수정된 설명",
  "source": "manual"
}
```

---

## 응답

**`200 OK`** — 수정된 BookResponse 반환

```json
{
  "status": 200,
  "data": {
    "id": 1,
    "title": "클린 코드 (수정판)",
    "author": "로버트 C. 마틴",
    "isbn": "9788966261208",
    "publisher": "인사이트",
    "publishedDate": "2013-12-24",
    "coverImageUrl": "https://...",
    "description": "수정된 설명",
    "source": "manual",
    "createdAt": "2026-03-22T10:00:00"
  }
}
```

---

## 에러

**`404 Not Found`** — 존재하지 않는 ID

```json
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "해당 책을 찾을 수 없습니다."
}
```

**`400 Bad Request`** — 유효성 검증 실패 (title, author 누락 등)

---

## 유효성 검증

책 등록(`01-book-create.md`)과 동일한 규칙 적용.

---

## 테스트 항목

### 단위 테스트

- **BookServiceTest**: 정상 수정 시 변경된 BookResponse 반환
- **BookServiceTest**: 존재하지 않는 ID 수정 시 예외 발생

### 통합 테스트

- **BookJpaRepositoryTest**: 수정 후 조회 시 변경된 값 확인