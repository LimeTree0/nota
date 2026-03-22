# 02. 책 목록 조회

> `GET /api/v1/books`

---

## 요청

**Query Parameters**

| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `page` | `int` | X | 페이지 번호 (기본값 0) |
| `size` | `int` | X | 페이지 크기 (기본값 20) |

```
GET /api/v1/books?page=0&size=20
```

---

## 응답

**`200 OK`**

```json
{
  "status": 200,
  "data": {
    "content": [
      {
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
    ],
    "page": 0,
    "size": 20,
    "totalElements": 42,
    "totalPages": 3,
    "last": false
  }
}
```

---

## 정렬

- 기본 정렬: `createdAt DESC` (최신 등록순)

---

## 테스트 항목

### 단위 테스트

- **BookServiceTest**: 페이지네이션 파라미터 전달 확인
- **BookServiceTest**: 결과가 없을 때 빈 리스트 반환

### 통합 테스트

- **BookJpaRepositoryTest**: 다건 저장 후 페이지네이션 조회
- **BookJpaRepositoryTest**: 정렬 순서(createdAt DESC) 확인