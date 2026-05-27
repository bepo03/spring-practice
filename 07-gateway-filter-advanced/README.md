# 07-gateway-filter-advanced

Spring Cloud Gateway의 `GlobalFilter`와 `GatewayFilterFactory`를 직접 구현하며 필터 체인의 실행 흐름을 확인하는 실습 프로젝트입니다.

이번 실습에서는 모든 요청에 적용되는 전역 필터와 특정 Route에만 적용되는 라우트 필터를 함께 사용하고, `Ordered`로 실행 순서를 제어하며, 인증 실패 요청을 다운스트림까지 보내지 않고 단락 처리합니다.

## 실습 목표

- `GlobalFilter`와 `GatewayFilter`의 적용 범위 차이를 확인한다.
- `chain.filter(exchange)` 앞의 코드는 Pre 단계로 실행된다는 것을 확인한다.
- `.then(...)`, `.doFinally(...)`가 Post 단계에서 실행된다는 것을 확인한다.
- `ServerWebExchange.mutate()`로 요청 헤더를 추가한다.
- `exchange.getAttributes()`로 Pre와 Post 사이에 값을 전달한다.
- `Ordered`로 GlobalFilter와 GatewayFilter의 실행 순서를 제어한다.
- `chain.filter(exchange)`를 호출하지 않고 요청을 Short-Circuit 처리한다.

## 서비스 구성

```text
07-gateway-filter-advanced/
  src/
```

| 서비스 | 포트 | 역할 |
| --- | --- | --- |
| `gateway-filter-advanced` | `8000` | Gateway, 필터 실습, 테스트용 내부 API |

## Gateway 구성요소

| 구성요소 | 이번 실습에서의 역할 |
| --- | --- |
| Route | `/api/orders/**`, `/api/secure/orders/**` 요청을 내부 API로 라우팅 |
| Predicate | `Path` 조건으로 요청 경로 검사 |
| GatewayFilter | `StripPrefix`, `PrefixPath`, `ResponseTime`으로 라우트별 요청/응답 처리 |
| GlobalFilter | `LoggingGlobalFilter`, `AuthGlobalFilter`로 모든 요청 흐름 제어 |
| Ordered | 전역 필터 실행 순서 제어 |

## 과제

### Gateway 기본 설정

Gateway 서버 포트를 `8000`으로 설정하고, Actuator의 Gateway 엔드포인트를 노출합니다.

```yaml
server:
  port: 8000

management:
  endpoint:
    gateway:
      access: read-only
  endpoints:
    web:
      exposure:
        include: gateway,health,info
```

### 테스트용 내부 API 만들기

Gateway가 라우팅할 내부 API를 레이어드 구조로 만듭니다.

```text
order/
  controller/
  service/
  dto/
```

내부 API는 다음 요청을 처리합니다.

```http
GET /internal/{id}
```

응답에는 주문 ID, 서비스 이름, Gateway에서 전달한 `X-Trace-Id` 값을 포함합니다.

### Gateway Route 구성

일반 주문 요청과 인증이 필요한 주문 요청을 각각 내부 API로 라우팅합니다.

| 요청 경로 | 라우팅 대상 | 필터 |
| --- | --- | --- |
| `/api/orders/**` | `http://localhost:8000/internal/**` | `StripPrefix=2`, `PrefixPath=/internal`, `ResponseTime` |
| `/api/secure/orders/**` | `http://localhost:8000/internal/**` | `StripPrefix=3`, `PrefixPath=/internal`, `ResponseTime` |

일반 요청 흐름:

```text
/api/orders/1
  -> StripPrefix=2
  -> /1
  -> PrefixPath=/internal
  -> /internal/1
```

인증 요청 흐름:

```text
/api/secure/orders/1
  -> StripPrefix=3
  -> /1
  -> PrefixPath=/internal
  -> /internal/1
```

### LoggingGlobalFilter 만들기

모든 요청에 적용되는 `LoggingGlobalFilter`를 만듭니다.

- 요청에 `X-Trace-Id`가 없으면 UUID를 생성한다.
- 요청에 `X-Trace-Id`가 있으면 기존 값을 유지한다.
- `ServerWebExchange.mutate()`로 요청 헤더를 추가한다.
- Pre 단계에서 `[REQ]` 로그를 출력한다.
- Post 단계에서 `[RES]` 로그를 출력한다.
- `doFinally`로 성공, 에러와 관계없이 응답 시간을 기록한다.

### ResponseTimeGatewayFilterFactory 만들기

특정 Route에만 적용되는 커스텀 `GatewayFilterFactory`를 만듭니다.

- Pre 단계에서 시작 시간을 `exchange.getAttributes()`에 저장한다.
- Post 단계에서 처리 시간을 계산한다.
- 응답 헤더에 처리 시간을 추가한다.
- YAML에서는 `ResponseTime=X-Order-Response-Time`처럼 사용한다.

응답 헤더 예시:

```http
X-Order-Response-Time: 15ms
X-Secure-Order-Response-Time: 15ms
```

### AuthGlobalFilter로 Short-Circuit 처리

`/api/secure/**` 요청은 `Authorization` 헤더가 없으면 다운스트림으로 보내지 않고 `401 Unauthorized`로 종료합니다.

```http
GET /api/secure/orders/1
```

성공 조건:

- `Authorization` 헤더가 없으면 `chain.filter(exchange)`를 호출하지 않는다.
- 응답 상태는 `401 Unauthorized`다.
- 응답 본문에는 에러 메시지가 포함된다.
- `Authorization: Bearer ...` 헤더가 있으면 내부 API까지 요청이 전달된다.

## 실행 순서

1. `gateway-filter-advanced` 실행

프로젝트 폴더에서 다음 명령으로 실행합니다.

```bash
./gradlew bootRun
```

Windows PowerShell에서는 다음 명령을 사용할 수 있습니다.

```powershell
.\gradlew.bat bootRun
```

IntelliJ에서는 `Application` 클래스를 실행해도 됩니다.

## 검증

### 내부 API 직접 확인

```bash
curl -i http://localhost:8000/internal/1
```

예상 응답:

```json
{"id":1,"service":"internal-order-api","traceId":""}
```

### Actuator 라우트 확인

```bash
curl -s http://localhost:8000/actuator/gateway/routes | python -m json.tool
```

`order-service`, `secure-order-service` 라우트가 보이면 성공입니다.

### Trace ID 자동 생성 확인

```bash
curl -i http://localhost:8000/api/orders/1
```

서비스 응답 또는 로그에서 `X-Trace-Id`가 생성되어 전달되면 성공입니다.

### Trace ID 유지 확인

```bash
curl -i -H "X-Trace-Id: test-trace-001" http://localhost:8000/api/orders/1
```

로그와 응답에서 `test-trace-001`이 유지되면 성공입니다.

### 응답 시간 헤더 확인

```bash
curl -i http://localhost:8000/api/orders/1
```

응답 헤더에 다음 값이 포함되면 성공입니다.

```http
X-Order-Response-Time: 15ms
```

### Short-Circuit 확인

인증 헤더 없이 호출합니다.

```bash
curl -i http://localhost:8000/api/secure/orders/1
```

`401 Unauthorized`가 반환되면 성공입니다.

인증 헤더를 포함해 호출합니다.

```bash
curl -i -H "Authorization: Bearer test-token" http://localhost:8000/api/secure/orders/1
```

다운스트림 API 응답과 `X-Secure-Order-Response-Time` 헤더가 반환되면 성공입니다.

### 일반 라우트 유지 확인

```bash
curl -i http://localhost:8000/api/orders/1
```

인증 헤더 없이도 일반 라우트가 정상 응답하면 성공입니다.

## 필요한 의존성

현재 프로젝트는 Gateway Filter 실습을 위해 다음 의존성을 사용합니다.

- Spring Cloud Gateway Server WebFlux
- Spring Boot Actuator
- Lombok

예시:

```gradle
implementation 'org.springframework.cloud:spring-cloud-starter-gateway-server-webflux'
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

## 구현 체크리스트

- [x] Spring Cloud Gateway 프로젝트 생성
- [x] Gateway WebFlux 의존성 추가
- [x] Actuator 의존성 추가
- [x] Gateway 포트를 `8000`으로 설정
- [x] Actuator Gateway 엔드포인트 노출
- [x] 테스트용 `/internal/{id}` API 생성
- [x] `/api/orders/**` Route 구성
- [x] `/api/secure/orders/**` Route 구성
- [x] `StripPrefix` 필터 구성
- [x] `PrefixPath` 필터 구성
- [x] `LoggingGlobalFilter` 생성
- [x] `ServerWebExchange.mutate()`로 요청 헤더 추가
- [x] Pre/Post 로그 출력
- [x] `ResponseTimeGatewayFilterFactory` 생성
- [x] `exchange.getAttributes()`로 시작 시간 전달
- [x] 응답 헤더 `X-Order-Response-Time` 추가
- [x] 응답 헤더 `X-Secure-Order-Response-Time` 추가
- [x] `AuthGlobalFilter` 생성
- [x] 인증 실패 시 Short-Circuit 처리
- [x] 정상 요청과 인증 실패 요청 검증
