# 03-hexagonal-post-api

Spring Boot 헥사고날 아키텍처 실습 프로젝트입니다.

게시글 등록 기능을 Domain, Application Port, Adapter로 나누어 구현합니다.

## 수업 주제

Spring Boot 아키텍처 3편: 헥사고날 아키텍처

## 과제 1. 게시글 도메인을 헥사고날로 구현하기

다음 요구사항으로 게시글 등록 기능을 구현합니다.

- 제목과 내용을 받아 게시글을 저장한다.
- 같은 제목의 글이 이미 있으면 `"중복된 제목입니다"` 예외를 던진다.
- Application Service는 JPA 구현체가 아니라 `PostRepository` 출력 포트에 의존한다.

## 권장 패키지 구조

```text
src/main/java/com/bepo/hexagonalpostapi/
  Application.java
  post/
    domain/
      Post.java
    application/
      port/
        in/
          CreatePostUseCase.java
        out/
          PostRepository.java
      service/
        CreatePostService.java
    adapter/
      in/
        web/
          PostController.java
          PostCreateRequest.java
          PostResponse.java
      out/
        persistence/
          PostJpaEntity.java
          PostJpaRepository.java
          PostPersistenceAdapter.java
```

## 계층별 역할

| 계층 | 역할 |
| --- | --- |
| Domain | 핵심 도메인 객체와 규칙을 표현한다. |
| In Port | 외부에서 호출할 유스케이스 계약을 정의한다. |
| Out Port | 애플리케이션이 외부 저장소에 기대하는 계약을 정의한다. |
| Application Service | 유스케이스 흐름과 비즈니스 규칙을 처리한다. |
| In Adapter | HTTP 요청을 받아 In Port를 호출한다. |
| Out Adapter | JPA 같은 외부 기술을 Out Port에 맞게 연결한다. |

## 헥사고날 흐름

```text
HTTP 요청
  -> PostController
  -> CreatePostUseCase
  -> CreatePostService
  -> PostRepository
  -> PostPersistenceAdapter
  -> PostJpaRepository
```

## 구현 체크리스트

- [x] `Post` 도메인 객체 생성
- [x] `CreatePostUseCase` 입력 포트 생성
- [x] `PostRepository` 출력 포트 생성
- [x] `CreatePostService.create(...)` 구현
- [x] `PostJpaEntity` 생성
- [x] `PostJpaRepository extends JpaRepository<PostJpaEntity, Long>` 생성
- [x] `PostPersistenceAdapter`에서 출력 포트 구현
- [x] `PostController`에서 `POST /api/posts` 구현
- [x] 제목 중복 시 `"중복된 제목입니다"` 예외 처리

## 필요한 의존성

현재 프로젝트는 API + JPA 실습을 위해 다음 의존성을 사용합니다.

- Spring Web MVC
- Spring Data JPA
- Validation
- H2 Database
- Lombok

예시:

```gradle
implementation 'org.springframework.boot:spring-boot-starter-webmvc'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-validation'
implementation 'org.springframework.boot:spring-boot-h2console'
runtimeOnly 'com.h2database:h2'
compileOnly 'org.projectlombok:lombok'
annotationProcessor 'org.projectlombok:lombok'
```

## API 명세

### 게시글 등록

```http
POST /api/posts
Content-Type: application/json

{
  "title": "첫 번째 게시글",
  "content": "헥사고날 아키텍처 실습입니다."
}
```

### 성공 응답 예시

```http
HTTP/1.1 201 Created
```

## 과제 2. 메모리 어댑터 만들어 테스트하기

`PostRepository` 출력 포트의 메모리 기반 구현체를 만들어 DB와 Spring 없이 `CreatePostService`를 단위 테스트합니다.

```text
src/test/java/com/bepo/hexagonalpostapi/post/
  InMemoryPostRepository.java
  CreatePostServiceTest.java
```

## 과제 3. 비교 보고서 작성

레이어드, 파사드, 헥사고날 아키텍처를 비교해 README에 정리합니다.

- 각 아키텍처의 한 줄 요약
- 각 아키텍처가 잘 어울리는 상황
- 다음 토이 프로젝트에 적용하고 싶은 아키텍처와 이유
