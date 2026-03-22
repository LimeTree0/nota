# 01. 책 등록

> `POST /api/v1/books`

---

## 요청

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `title` | `String` | O | 제목 |
| `author` | `String` | O | 저자 |
| `isbn` | `String` | X | ISBN (nullable) |
| `publisher` | `String` | X | 출판사 |
| `publishedDate` | `LocalDate` | X | 출판일 |
| `coverImageUrl` | `String` | X | 표지 이미지 URL |
| `description` | `String` | X | 책 소개 |
| `source` | `String` | X | 등록 출처, 기본값 `manual` |

```json
{
  "title": "클린 코드",
  "author": "로버트 C. 마틴",
  "isbn": "9788966261208",
  "publisher": "인사이트",
  "publishedDate": "2013-12-24",
  "coverImageUrl": "https://...",
  "description": "...",
  "source": "kakao"
}
```

---

## 응답

**`201 Created`**

```json
{
  "status": 201,
  "data": {
    "id": 1,
    "title": "클린 코드",
    "author": "로버트 C. 마틴",
    "isbn": "9788966261208",
    "publisher": "인사이트",
    "publishedDate": "2013-12-24",
    "coverImageUrl": "https://...",
    "description": "...",
    "source": "kakao",
    "createdAt": "2026-03-22T10:00:00"
  }
}
```

---

## 유효성 검증

| 필드 | 규칙 | 실패 시 |
|------|------|---------|
| `title` | 필수, 공백 불가 | `400 Bad Request` |
| `author` | 필수, 공백 불가 | `400 Bad Request` |
| `isbn` | 선택, 입력 시 13자리 숫자 형식 | `400 Bad Request` |
| `source` | `kakao`, `google`, `manual` 중 하나 | `400 Bad Request` |

---

## 테스트 항목

### 단위 테스트

- **BookTest**: 필수값(title, author)으로 Entity 생성 성공
- **BookTest**: title 누락 시 예외 발생
- **BookTest**: source 미입력 시 기본값 `MANUAL` 설정
- **BookServiceTest**: 정상 등록 시 BookResponse 반환 확인

### 통합 테스트

- **BookJpaRepositoryTest**: 저장 후 조회 시 모든 필드 일치 확인