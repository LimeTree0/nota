# 03. 책 상세 조회

> `GET /api/v1/books/{id}`

---

## 요청

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | `Long` | 책 ID |

```
GET /api/v1/books/1
```

---

## 응답

**`200 OK`**

```json
{
  "status": 200,
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

## 에러

**`404 Not Found`** — 존재하지 않는 ID

```json
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "해당 책을 찾을 수 없습니다."
}
```

---

## 테스트 항목

### 단위 테스트

- **BookServiceTest**: 존재하는 ID로 조회 시 BookResponse 반환
- **BookServiceTest**: 존재하지 않는 ID로 조회 시 예외 발생

### 통합 테스트

- **BookJpaRepositoryTest**: 저장한 Entity를 ID로 조회 성공
