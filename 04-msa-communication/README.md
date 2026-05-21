# 04-msa-communication

## 실습 목표

MSA 구조에서 두 개의 Spring Boot 서비스를 분리해 실행하고, 서비스 간 HTTP 통신을 구현한다.

- `member-service`는 회원 조회 API를 제공한다.
- `order-service`는 주문 생성 요청을 받고, 주문 생성 과정에서 `member-service`를 호출한다.
- OpenFeign으로 서비스 간 통신을 구현한다.
- `member-service` 응답 지연 상황에서 OpenFeign read timeout 동작을 확인한다.
- 같은 회원 조회 기능을 RestClient로도 구현하고 OpenFeign과 비교한다.

## 서비스 구성

```text
04-msa-communication/
  member-service/
  order-service/
```

| 서비스 | 포트 | 역할 |
| --- | --- | --- |
| `member-service` | `8081` | 회원 조회 API 제공 |
| `order-service` | `8082` | 주문 생성 API 제공, 회원 서비스 호출 |

## 과제 1. 두 서비스 통신 구현

### member-service

```http
GET /api/members/{id}
```

`MemberResponse`를 반환한다.

### order-service

```http
POST /api/orders
```

주문 생성 요청을 받으면 `member-service`에서 회원 정보를 조회한 뒤 주문을 생성한다.

## 과제 2. 타임아웃 실험

`member-service`의 회원 조회 컨트롤러에서 일부러 지연을 발생시킨다.

```java
Thread.sleep(10000);
```

`order-service`의 OpenFeign `read-timeout`을 3초로 설정한 뒤 다음 내용을 관찰한다.

- 어떤 예외가 발생하는지
- 사용자에게 어떤 응답으로 보이는지
- 예외 처리를 하면 어떻게 개선할 수 있는지

## 과제 3. RestClient vs OpenFeign 비교

같은 회원 조회 기능을 RestClient로도 구현한다.

비교 관점:

- 코드 구조
- 가독성
- 설정 방식
- 예외 처리
- MSA 내부 서비스 통신에 더 적합한 방식
