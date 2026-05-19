# Spring Practice

Spring Boot 수업과 실습 과제를 정리하는 저장소입니다.

각 실습은 독립적인 Spring Boot 프로젝트로 관리합니다. 하나의 GitHub repository 안에 과제별 프로젝트 폴더를 추가하는 방식입니다.

## 실습 목록

| 번호 | 프로젝트 | 주제 |
| --- | --- | --- |
| 01 | [01-layered-post-api](./01-layered-post-api) | 레이어드 아키텍처로 게시글 등록 API 만들기 |
| 02 | [02-facade-post-like](./02-facade-post-like) | Facade 패턴으로 게시글 좋아요 흐름 만들기 |
| 03 | [03-hexagonal-post-api](./03-hexagonal-post-api) | 헥사고날 아키텍처로 게시글 등록 API 만들기 |

## 관리 방식

- 각 실습 폴더는 별도의 `build.gradle`, `settings.gradle`, `src`를 가진 독립 프로젝트입니다.
- 실습별 요구사항과 배운 내용은 각 프로젝트의 `README.md`에 정리합니다.

## 폴더 구조

```text
spring-practice/
  README.md
  01-layered-post-api/
    build.gradle
    settings.gradle
    src/
  02-facade-post-like/
    build.gradle
    settings.gradle
    src/
  03-hexagonal-post-api/
    build.gradle
    settings.gradle
    src/
```
