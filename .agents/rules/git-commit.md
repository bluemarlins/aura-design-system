---
trigger: always_on
---

# Git Commit Guidelines (mobile-design-system)

이 저장소에서 Git Commit을 생성할 때는 반드시 아래의 **카테고리별 분리 원칙**과 **Conventional Commits 1.0.0 규칙**을 준수한다.

## 1. 카테고리별 분리 커밋 원칙 (Atomic Category Commits)

한 번의 커밋에 서로 다른 카테고리의 변경사항을 섞지 않는다. 작업 완료 후 여러 영역의 파일이 수정되었다면 반드시 아래 5가지 카테고리로 스테이징(`git add`)을 분리하여 각각 별도의 커밋으로 작성한다.

| 카테고리 | 대상 경로 및 파일 | 권장 `<type>(<scope>)` 예시 |
| :--- | :--- | :--- |
| **Docs** | `docs/**`, `README.md`, 기타 마크다운 문서 | `docs(foundation): ...`, `docs(component): ...`, `docs: ...` |
| **Design System** | `design-system/**` (토큰, 테마, 컴포넌트, 모듈 테스트) | `feat(design-system): ...`, `fix(design-system): ...`, `refactor(design-system): ...`, `test(design-system): ...` |
| **Design System Preview** | `design-system-preview/**` (프리뷰 앱, 카탈로그, 프리뷰 화면 및 테스트) | `feat(preview): ...`, `fix(preview): ...`, `refactor(preview): ...`, `test(preview): ...` |
| **Env** | `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`, `gradle/**`, `gradlew*`, `.gitignore`, CI 설정 | `build(env): ...`, `chore(env): ...`, `ci(env): ...` |
| **Etc** | `.agents/**`, `AGENTS.md`, 기타 위 분류에 속하지 않는 파일 | `chore(etc): ...` |

> 세부 컴포넌트나 파운데이션 단위로 더 잘게 나누는 경우에도 서로 다른 카테고리(`docs/`, `design-system/`, `design-system-preview/`, `Env`, `Etc`)의 파일이 한 커밋에 함께 포함되지 않도록 한다.

## 2. 권장 커밋 순서

여러 카테고리의 변경사항이 동시에 존재할 경우 아래 순서로 커밋한다.

1. **Env** — 빌드 스크립트, 의존성, 환경 설정
2. **Docs** — 디자인 시스템 스펙 및 아키텍처 문서
3. **Design System** — `:design-system` 모듈 구현 및 테스트
4. **Design System Preview** — `:design-system-preview` 앱 반영 및 테스트
5. **Etc** — 에이전트 규칙 및 기타 파일

## 3. Conventional Commits 1.0.0 준수

```text
<type>(<scope>): <description>

[optional body]

[optional footer(s)]
```

- **Header**:
  - `<type>`과 `<scope>`는 항상 영문 소문자를 사용한다.
  - `<description>`은 간결한 명령형(imperative mood)으로 작성하며, 끝에 마침표(`.`)를 찍지 않는다.
- **Commit Types**:
  - `feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`, `build`, `ci`, `chore`, `revert`
- **Body (선택)**:
  - 비자명한 변경사항의 경우 헤더와 빈 줄로 구분하여 변경 배경(WHY)과 주요 내용을 기술한다.
- **Breaking Changes**:
  - 호환성이 깨지는 변경은 `<type>(<scope>)!:` 또는 Footer의 `BREAKING CHANGE:`로 명시한다.

## 4. 커밋 전 체크리스트

1. `git status`로 전체 변경 및 미추적 파일을 확인하고 5개 카테고리로 분류한다.
2. 특정 카테고리의 파일만 지정하여 스테이징한다 (`git add -A` 또는 `git add .`로 여러 카테고리를 한꺼번에 스테이징하지 않는다).
3. 커밋 실행 전 `git diff --cached --name-only`로 스테이징된 파일들이 단일 카테고리에만 속하는지 검증한다.
