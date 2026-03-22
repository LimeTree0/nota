# 05. 책 삭제

> `DELETE /api/v1/books/{id}`

---

## 요청

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | `Long` | 책 ID |

```
DELETE /api/v1/books/1
```

---

## 응답

**`204 No Content`** — 응답 본문 없음

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

## 삭제 정책

- 연결된 `reading_sessions`가 있는 경우: **cascade 삭제** (회독, 노트, 하이라이트 모두 삭제)
- 향후 soft delete 전환 가능성을 고려하되, MVP에서는 hard delete로 구현

---

## 테스트 항목

### 단위 테스트

- **BookServiceTest**: 정상 삭제 호출 확인
- **BookServiceTest**: 존재하지 않는 ID 삭제 시 예외 발생

### 통합 테스트

- **BookJpaRepositoryTest**: 삭제 후 조회 시 결과 없음 확인