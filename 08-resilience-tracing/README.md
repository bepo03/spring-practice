# 08-resilience-tracing

Spring Boot MSA 회복탄력성과 분산 추적 실습 프로젝트입니다.

`order-service`가 `payment-service`를 호출하는 구조에서 Resilience4j Circuit Breaker를 적용하고, 이후 Micrometer Tracing과 Zipkin으로 서비스 간 호출 흐름을 확인합니다.

## 수업 주제

Spring Boot MSA 4편: 회복탄력성과 분산 추적

## 실습 목표

- Circuit Breaker의 `CLOSED`, `OPEN`, `HALF_OPEN` 상태 변화를 확인한다.
- Resilience4j로 외부 서비스 호출 실패를 빠르게 차단한다.
- fallback 메서드로 실패 상황의 대체 응답을 처리한다.
- Actuator에서 Circuit Breaker 상태를 확인한다.
- 장애 상황별 fallback 전략을 설계한다.
- Micrometer Tracing으로 `traceId`, `spanId`를 로그에 출력한다.
- Zipkin UI에서 서비스 간 호출 트리를 확인한다.

## 서비스 구성

```text
08-resilience-tracing/
  payment-service/
  order-service/
```

| 서비스 | 포트 | 역할 |
| --- | --- | --- |
| `payment-service` | `8083` | 결제 API 제공, 장애 상황 실험 |
| `order-service` | `8082` | 주문 API 제공, 결제 서비스 호출 |

## 과제 1. Circuit Breaker 동작 관찰

`order-service`가 `payment-service`를 호출하는 구조에서 다음을 실험합니다.

- `payment-service`에서 일부러 50% 확률로 예외를 던지도록 구현한다.
- `order-service`에 Circuit Breaker를 적용한다.
- 호출을 20번 정도 보내며 상태 변화를 관찰한다.
- Actuator 엔드포인트(`/actuator/health`)에서 Circuit Breaker 상태를 확인한다.

### payment-service

```http
POST /api/payments
```

결제 요청을 받으면 50% 확률로 성공 응답을 반환하고, 50% 확률로 예외를 발생시킵니다.

### order-service

```http
POST /api/orders
```

주문 생성 요청을 받으면 `payment-service`를 호출해 결제를 시도합니다.

### Circuit Breaker 설정

`paymentService` Circuit Breaker는 실패율 50%를 기준으로 `OPEN` 상태가 되도록 설정합니다.

```yaml
resilience4j:
  circuitbreaker:
    instances:
      paymentService:
        register-health-indicator: true
        sliding-window-size: 10
        minimum-number-of-calls: 5
        failure-rate-threshold: 50
        wait-duration-in-open-state: 60s
        permitted-number-of-calls-in-half-open-state: 3
        automatic-transition-from-open-to-half-open-enabled: true
```

## 과제 2. 폴백 설계 연습

다음 시나리오 각각에 대해 fallback 전략과 이유를 정리합니다.

- 회원의 등급(VIP/일반)을 조회할 때 등급 서비스가 다운된 경우
- 추천 상품 목록을 조회할 때 추천 서비스가 다운된 경우
- 회원가입 시 환영 이메일을 발송할 때 메일 서비스가 다운된 경우
- 결제 처리 중 결제 게이트웨이가 다운된 경우

## 과제 3. 분산 추적 직접 보기

Zipkin을 Docker로 띄우고, 4개 서비스에 Micrometer Tracing 의존성을 추가합니다.

```text
Client
  -> gateway-service
  -> order-service
      -> member-service
      -> payment-service
```

한 요청을 보낸 뒤 Zipkin UI에서 호출 트리를 확인합니다.

### 과제 3 서비스 구성

```text
08-resilience-tracing/
  gateway-service/
  member-service/
```

| 서비스 | 포트 | 역할 |
| --- | --- | --- |
| `gateway-service` | `8000` | 외부 요청 진입점, 주문 서비스 라우팅 |
| `member-service` | `8081` | 회원 조회 API 제공 |

### gateway-service

Gateway 라우팅 규칙은 다음과 같습니다.

| 요청 경로 | 라우팅 대상 |
| --- | --- |
| `/api/orders/**` | `http://localhost:8082` |

### member-service

```http
GET /api/members/{id}
```

회원 정보를 반환합니다.

## 실행 순서

1. `payment-service` 실행
2. `order-service` 실행

zsh에서는 프로젝트 폴더에서 다음 명령으로 실행합니다.

```bash
./gradlew :payment-service:bootRun
./gradlew :order-service:bootRun
```

IntelliJ에서는 각 서비스의 `Application` 클래스를 실행해도 됩니다.

과제 3에서는 Zipkin과 추가 서비스를 함께 실행합니다.

```bash
docker run -d -p 9411:9411 openzipkin/zipkin
./gradlew :member-service:bootRun
./gradlew :gateway-service:bootRun
```

## 검증

### payment-service 직접 호출

```bash
curl -i -X POST http://localhost:8083/api/payments
```

성공 응답과 실패 응답이 섞여 나오면 성공입니다.

### order-service 주문 API 호출

```bash
curl -i -X POST http://localhost:8082/api/orders
```

결제 성공 응답 또는 fallback 응답이 반환되면 성공입니다.

### Circuit Breaker 상태 확인

```bash
curl -s http://localhost:8082/actuator/health | python -m json.tool
```

Circuit Breaker 상태가 Actuator 응답에 포함되면 성공입니다.

### Zipkin UI 확인

```http
GET http://localhost:9411
```

Zipkin UI에서 `gateway-service`, `order-service`, `member-service`, `payment-service` 호출 흐름이 보이면 성공입니다.

## 필요한 의존성

현재 프로젝트는 회복탄력성과 분산 추적 실습을 위해 다음 의존성을 사용합니다.

- Spring Web MVC
- Spring Boot Actuator
- Spring Cloud Circuit Breaker Resilience4j
- Spring Boot AOP
- Micrometer Tracing
- Zipkin Reporter
- Lombok

예시:

```gradle
implementation 'org.springframework.boot:spring-boot-starter-webmvc'
implementation 'org.springframework.boot:spring-boot-starter-actuator'
implementation 'org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j'
implementation 'org.springframework.boot:spring-boot-starter-aop'
implementation 'io.micrometer:micrometer-tracing-bridge-brave'
implementation 'io.zipkin.reporter2:zipkin-reporter-brave'
```

## 구현 체크리스트

### 과제 1

- [x] Gradle 프로젝트 생성
- [x] `payment-service` 모듈 생성
- [x] `order-service` 모듈 생성
- [x] `payment-service` 결제 API 구현
- [x] `payment-service` 50% 실패 로직 구현
- [x] `order-service` 주문 API 구현
- [x] `order-service`에서 `payment-service` 호출
- [x] Resilience4j Circuit Breaker 적용
- [x] fallback 메서드 구현
- [x] Actuator로 Circuit Breaker 상태 확인

### 과제 2

- [ ] 회원 등급 조회 실패 fallback 전략 정리
- [ ] 추천 상품 목록 조회 실패 fallback 전략 정리
- [ ] 환영 이메일 발송 실패 fallback 전략 정리
- [ ] 결제 게이트웨이 실패 fallback 전략 정리

### 과제 3

- [ ] `gateway-service` 모듈 생성
- [ ] `member-service` 모듈 생성
- [ ] Zipkin Docker 실행
- [ ] Micrometer Tracing 의존성 추가
- [ ] 로그 패턴에 `traceId`, `spanId` 출력
- [ ] Gateway에서 주문 서비스로 라우팅
- [ ] `order-service`에서 `member-service`, `payment-service` 호출
- [ ] Zipkin UI에서 호출 트리 확인
