# 01-layered-post-api

Spring Boot 레이어드 아키텍처 실습 프로젝트입니다.

게시글 등록 API를 Controller, Service, Repository, Entity 계층으로 나누어 구현합니다.

## 수업 주제

Spring Boot 아키텍처 1편: 레이어드 아키텍처

## 과제 1. 게시글 도메인 만들기

다음 요구사항으로 게시글 등록 API를 구현합니다.

- `POST /api/posts`로 제목과 내용을 받아 저장한다.
- 제목이 비어있으면 400 에러를 반환한다.
- 같은 제목의 글이 이미 있으면 `"중복된 제목입니다"` 예외를 던진다.

## 권장 패키지 구조

```text
src/main/java/com/bepo/layeredpostapi/
  Application.java
  post/
    controller/
      PostController.java
    service/
      PostService.java
    repository/
      PostRepository.java
    entity/
      Post.java
    dto/
      PostCreateRequest.java
```

## 계층별 역할

| 계층 | 역할 |
| --- | --- |
| Controller | HTTP 요청을 받고 응답을 반환한다. |
| Service | 비즈니스 규칙을 처리한다. |
| Repository | 데이터 저장소와 통신한다. |
| Entity | 데이터베이스에 저장되는 도메인 객체를 표현한다. |
| DTO | 요청/응답 데이터를 전달한다. |

## 구현 체크리스트

- [ ] `Post` 엔티티 생성
- [ ] `PostRepository extends JpaRepository<Post, Long>` 생성
- [ ] `PostCreateRequest` DTO 생성
- [ ] `PostService.create(...)` 구현
- [ ] `PostController`에서 `POST /api/posts` 구현
- [ ] 제목 빈 값 검증 시 400 응답 처리
- [ ] 제목 중복 시 `"중복된 제목입니다"` 예외 처리

## 필요한 의존성

현재 프로젝트를 API + JPA 실습으로 진행하려면 다음 의존성이 필요합니다.

- Spring Web
- Spring Data JPA
- Validation
- H2 Database 또는 MySQL Driver

예시:

```gradle
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-validation'
runtimeOnly 'com.h2database:h2'
```

## API 명세

### 게시글 등록

```http
POST /api/posts
Content-Type: application/json

{
  "title": "첫 번째 게시글",
  "content": "레이어드 아키텍처 실습입니다."
}
```

### 성공 응답 예시

```http
HTTP/1.1 201 Created
```

## 과제 2. 계층 구분 퀴즈

다음 코드가 어느 계층에 있어야 하는지 스스로 정리합니다.

| 코드 | 예상 계층 |
| --- | --- |
| `if (member.getAge() < 19) throw new MinorException();` |  |
| `return ResponseEntity.ok(response);` |  |
| `entityManager.persist(member);` |  |
| `if (request.getEmail() == null) throw ValidationException...` |  |
