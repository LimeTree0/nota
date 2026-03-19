# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test

```bash
./gradlew bootRun          # run dev server (localhost:8080)
./gradlew build            # compile + test + package JAR
./gradlew test             # run all tests
./gradlew test --tests "com.limecoding.api.SomeTest"  # run single test class
./gradlew test --tests "com.limecoding.api.SomeTest.methodName"  # run single test method
```

## Tech Stack

- Spring Boot 4.0.3, Java 25, Gradle 9.3.1
- PostgreSQL + pgvector (prod) / H2 (dev/test)
- Lombok for boilerplate reduction

## Architecture

- **Base package:** `com.limecoding.api`
- **API base path:** `/api/v1`
- **Common response format:** All endpoints return `ApiResponse<T>` wrapper
- **Profiles:** `dev` (H2), `prod` (PostgreSQL), `test` (H2) — config in `application-{profile}.yml`
- **DB schema:** pgvector columns included from the start for future AI/RAG features

## Required References

모든 요청을 처리할 때 반드시 다음 문서를 참고한다:
- `docs/requirements.md` (프로젝트 루트) — 요구사항 명세
- `docs/common/architecture.md` — DDD 아키텍처 및 패키지 구조

## Design Principles

- Currently single-user, but all DB/API design must support future multi-user expansion
- Follow phased roadmap: MVP (CRUD) → enhancement → AI/RAG → multi-user
- Project docs and commits in Korean; code identifiers in English
- Reference project specs in `docs/` (root level) for requirements, DB schema, and API endpoint definitions