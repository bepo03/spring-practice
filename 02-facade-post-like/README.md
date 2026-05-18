# 02-facade-post-like

Spring Boot Facade 패턴 실습 프로젝트입니다.

게시글 좋아요 API를 여러 도메인 서비스로 나누고, `PostLikeFacade`에서 하나의 흐름으로 조합합니다.

## 수업 주제

Spring Boot 아키텍처 2편: Facade 패턴

## 과제 1. 게시글 좋아요 흐름 만들기

다음 시나리오로 게시글 좋아요 기능을 구현합니다.

- 회원이 활성 상태인지 확인한다. (`MemberService`)
- 게시글이 존재하는지 확인한다. (`PostService`)
- 좋아요 중복 검사 후 추가한다. (`LikeService`)
- 게시글 작성자에게 알림을 발송한다. (`NotificationService`)

## 권장 패키지 구조

```text
src/main/java/com/bepo/facadepostlike/
  Application.java
  post/
    controller/
      PostLikeController.java
    facade/
      PostLikeFacade.java
    service/
      PostService.java
    entity/
      Post.java
    repository/
      PostRepository.java
  member/
    service/
      MemberService.java
    entity/
      Member.java
    repository/
      MemberRepository.java
  like/
    service/
      LikeService.java
    entity/
      PostLike.java
    repository/
      PostLikeRepository.java
  notification/
    service/
      NotificationService.java
```

## 계층별 역할

| 계층 | 역할 |
| --- | --- |
| Controller | HTTP 요청을 받고 Facade를 호출한다. |
| Facade | 여러 도메인 서비스를 조합해 하나의 유스케이스를 완성한다. |
| Service | 각 도메인의 비즈니스 규칙을 처리한다. |
| Repository | 데이터 저장소와 통신한다. |
| Entity | 데이터베이스에 저장되는 도메인 객체를 표현한다. |

## Facade 흐름

```java
@Service
@RequiredArgsConstructor
public class PostLikeFacade {

    private final MemberService memberService;
    private final PostService postService;
    private final LikeService likeService;
    private final NotificationService notificationService;

    @Transactional
    public void like(Long memberId, Long postId) {
        memberService.validateActiveMember(memberId);
        Post post = postService.getPost(postId);
        likeService.addLike(memberId, postId);
        notificationService.sendLikeNotification(post.getMemberId(), postId);
    }
}
```

## 구현 체크리스트

- [x] `Member` 엔티티 생성
- [x] `Post` 엔티티 생성
- [x] `PostLike` 엔티티 생성
- [x] `MemberRepository`, `PostRepository`, `PostLikeRepository` 생성
- [x] `MemberService.validateActiveMember(...)` 구현
- [x] `PostService.getPost(...)` 구현
- [x] `LikeService.addLike(...)` 구현
- [x] `NotificationService.sendLikeNotification(...)` 구현
- [x] `PostLikeFacade.like(...)`에서 전체 흐름 조합
- [x] `PostLikeController`에서 `POST /api/posts/{postId}/likes` 구현

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

### 게시글 좋아요

```http
POST /api/posts/1/likes
Content-Type: application/json

{
  "memberId": 1
}
```

### 성공 응답 예시

```http
HTTP/1.1 200 OK
```

## 예외 상황

| 상황 | 예외 메시지 예시 |
| --- | --- |
| 회원이 존재하지 않음 | `"존재하지 않는 회원입니다"` |
| 회원이 비활성 상태 | `"비활성 회원입니다"` |
| 게시글이 존재하지 않음 | `"존재하지 않는 게시글입니다"` |
| 이미 좋아요를 누름 | `"이미 좋아요를 눌렀습니다"` |
