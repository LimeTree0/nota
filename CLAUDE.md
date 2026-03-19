# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Nota (독방)** — A personal reading notes and knowledge base application. Users record books, organize markdown notes, track highlights, and (future) build a RAG-based AI knowledge base. Solo developer project.

Detailed specs live in `docs/` (requirements.md, db_schema.md, api_endpoints.md, nota-naming.md).

## Monorepo Structure

```
apps/
  web/   — React 19 + TypeScript 5.9 + Vite 8 (frontend)
  api/   — Spring Boot 4.0.3 + Java 25 + Gradle (backend)
docs/    — Project specifications and schema design
```

This is **not** a managed monorepo (no Nx/Turbo) — each app is built independently.

## Build & Run Commands

### Web (`apps/web`)
```bash
npm install          # install dependencies
npm run dev          # Vite dev server (localhost:5173)
npm run build        # tsc -b && vite build
npm run lint         # eslint .
npm run preview      # preview production build
```

### API (`apps/api`)
```bash
./gradlew bootRun    # Spring Boot dev server (localhost:8080)
./gradlew build      # compile + test + JAR
./gradlew test       # run tests only
```

## Architecture & Key Decisions

- **API base path:** `/api/v1` — all endpoints follow REST conventions defined in `docs/api_endpoints.md`
- **Common response format:** All API responses use `ApiResponse<T>` wrapper with `status` + `data` (success) or `status` + `error` + `message` (error)
- **Database:** PostgreSQL (prod) / H2 (dev/test), profile-separated via `application-{profile}.yml`
- **DB schema:** pgvector columns pre-added for future Phase 3 AI features. Tables: books, reading_sessions, notes, highlights, tags, note_tags
- **Backend package:** `com.limecoding.api`
- **Frontend:** React with hooks, React Compiler enabled, strict TypeScript

## Language

Project documentation and commit messages are written in Korean. Code (variable names, comments in code) follows English conventions.
