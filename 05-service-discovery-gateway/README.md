# 05-service-discovery-gateway

Spring Cloud Eureka와 Gateway를 사용해 서비스 디스커버리 기반 MSA 라우팅을 실습하는 프로젝트입니다.

## 실습 목표

Eureka Server, 두 개의 마이크로서비스, API Gateway를 구성하고 Gateway에서 Eureka에 등록된 서비스로 요청을 라우팅합니다.

- `eureka-server`는 서비스 디스커버리 서버 역할을 한다.
- `member-service`는 회원 조회 API를 제공하고 Eureka에 등록된다.
- `product-service`는 상품 조회 API를 제공하고 Eureka에 등록된다.
- `api-gateway`는 외부 요청을 받아 각 서비스로 라우팅한다.
- `member-service`를 두 인스턴스로 실행해 Gateway 로드밸런싱을 확인한다.

## 서비스 구성

```text
05-service-discovery-gateway/
  eureka-server/
  member-service/
  product-service/
  api-gateway/
```

| 서비스 | 포트 | 역할 |
| --- | --- | --- |
| `eureka-server` | `8761` | Eureka 대시보드와 서비스 디스커버리 서버 |
| `api-gateway` | `8080` | 외부 요청 진입점, 서비스 라우팅 |
| `member-service` | `8081` | 회원 조회 API 제공 |
| `member-service` 2번 인스턴스 | `8091` | 로드밸런싱 확인용 추가 인스턴스 |
| `product-service` | `8082` | 상품 조회 API 제공 |

## 과제 1. Eureka + 두 서비스 + Gateway 전체 구성

다음 4개 프로젝트를 구성합니다.

- `eureka-server`
- `member-service`
- `product-service`
- `api-gateway`

### member-service

```http
GET /api/members/{id}
```

회원 정보를 반환합니다.

### product-service

```http
GET /api/products/{id}
```

상품 정보를 반환합니다.

### api-gateway

Gateway 라우팅 규칙은 다음과 같습니다.

| 요청 경로 | 라우팅 대상 |
| --- | --- |
| `/api/members/**` | `lb://member-service` |
| `/api/products/**` | `lb://product-service` |

## 실행 순서

1. `eureka-server` 실행
2. `member-service` 실행
3. `product-service` 실행
4. `api-gateway` 실행

## 검증

### Eureka 대시보드 확인

```http
GET http://localhost:8761
```

Eureka 대시보드에서 다음 서비스가 등록되어 있어야 합니다.

- `MEMBER-SERVICE`
- `PRODUCT-SERVICE`
- `API-GATEWAY`

### Gateway를 통한 member-service 호출

```bash
curl http://localhost:8080/api/members/1
```

### Gateway를 통한 product-service 호출

```bash
curl http://localhost:8080/api/products/1
```

### member-service 두 인스턴스 실행

첫 번째 인스턴스는 기본 포트 `8081`로 실행합니다.

두 번째 인스턴스는 `8091` 포트로 실행합니다.

```bash
./gradlew :member-service:bootRun --args='--server.port=8091'
```

Windows PowerShell에서는 실행 단계에서 따옴표 처리를 확인합니다.

### 로드밸런싱 확인

Gateway로 회원 조회 API를 여러 번 호출합니다.

```bash
curl http://localhost:8080/api/members/1
curl http://localhost:8080/api/members/1
curl http://localhost:8080/api/members/1
```

응답에 현재 서버 포트를 포함하면 `8081`, `8091` 응답이 번갈아 오는지 확인할 수 있습니다.

## 과제 2. 글로벌 필터로 추적 ID 부여

API Gateway의 글로벌 필터에서 모든 요청에 `X-Trace-Id` 헤더를 부여하고, 각 마이크로서비스에서 해당 헤더를 로그로 출력합니다.

### TraceIdGlobalFilter

Gateway로 들어온 요청에 `X-Trace-Id` 헤더가 없으면 UUID를 생성해 추가합니다.
이미 `X-Trace-Id` 헤더가 있으면 기존 값을 유지합니다.

```text
Client
  -> api-gateway
  -> member-service 또는 product-service
```

### 로그 출력

각 서비스 컨트롤러에서 `X-Trace-Id` 헤더를 받아 로그에 출력합니다.

```java
log.info("[traceId={}] findById id={}", traceId, id);
```

### 검증

Gateway를 통해 요청합니다.

```bash
curl -s http://localhost:8080/api/members/1 | python -m json.tool
```

직접 Trace ID를 전달해서 확인할 수도 있습니다.

```bash
curl -s -H "X-Trace-Id: test-trace-001" http://localhost:8080/api/members/1 | python -m json.tool
```

`member-service` 로그에서 다음과 같은 로그가 출력되면 성공입니다.

```text
[traceId=test-trace-001] findById id=1
```

## 과제 3. Predicate 응용

Gateway Predicate를 사용해 요청 경로와 헤더에 따라 서로 다른 서비스 클러스터로 라우팅합니다.

- `/api/v1/products/**` 요청은 `product-service-v1`로 라우팅한다.
- `/api/v2/products/**` 요청은 `product-service-v2`로 라우팅한다.
- `X-Beta=true` 헤더가 있는 요청은 `beta-cluster`로 라우팅한다.

## 추가 서비스 구성

```text
05-service-discovery-gateway/
  product-service-v1/
  product-service-v2/
  beta-cluster/
```

| 서비스 | 포트 | 역할 |
| --- | --- | --- |
| `product-service-v1` | `8083` | v1 상품 API 제공 |
| `product-service-v2` | `8084` | v2 상품 API 제공 |
| `beta-cluster` | `8085` | 베타 요청 처리 |

### Gateway Predicate 라우팅

`X-Beta=true` 요청은 모든 요청보다 먼저 `beta-cluster`로 라우팅되어야 하므로 가장 위에 둡니다.

| 조건 | 라우팅 대상 |
| --- | --- |
| Header `X-Beta=true` | `lb://beta-cluster` |
| Path `/api/v1/products/**` | `lb://product-service-v1` |
| Path `/api/v2/products/**` | `lb://product-service-v2` |

### 검증

v1 상품 API를 Gateway로 호출합니다.

```bash
curl -s http://localhost:8080/api/v1/products/1 | python -m json.tool
```

v2 상품 API를 Gateway로 호출합니다.

```bash
curl -s http://localhost:8080/api/v2/products/1 | python -m json.tool
```

베타 헤더를 포함해 요청하면 `beta-cluster`로 라우팅됩니다.

```bash
curl -s -H "X-Beta: true" http://localhost:8080/api/v1/products/1 | python -m json.tool
```

`X-Beta=true` 헤더는 경로보다 우선 적용되므로, v1 경로로 요청해도 베타 응답이 오면 성공입니다.

## 구현 체크리스트

### 과제 1

- [x] Gradle 멀티모듈 프로젝트 구성
- [x] `eureka-server` 모듈 생성
- [x] `member-service` 모듈 생성
- [x] `product-service` 모듈 생성
- [x] `api-gateway` 모듈 생성
- [x] `member-service`, `product-service` Eureka Client 등록
- [x] Gateway에서 `lb://member-service` 라우팅 구성
- [x] Gateway에서 `lb://product-service` 라우팅 구성
- [x] `member-service` 두 인스턴스 실행
- [x] Gateway 로드밸런싱 확인

### 과제 2

- [x] `api-gateway`에 `TraceIdGlobalFilter` 추가
- [x] 요청에 `X-Trace-Id`가 없으면 UUID 생성
- [x] 요청에 `X-Trace-Id`가 있으면 기존 값 유지
- [x] `member-service`에서 `X-Trace-Id` 로그 출력
- [x] `product-service`에서 `X-Trace-Id` 로그 출력
- [x] Gateway 요청 후 서비스 로그에서 Trace ID 확인

### 과제 3

- [x] `product-service-v1` 모듈 생성
- [x] `product-service-v2` 모듈 생성
- [x] `beta-cluster` 모듈 생성
- [x] `product-service-v1` Eureka Client 등록
- [x] `product-service-v2` Eureka Client 등록
- [x] `beta-cluster` Eureka Client 등록
- [x] Gateway에서 `/api/v1/products/**` 라우팅 구성
- [x] Gateway에서 `/api/v2/products/**` 라우팅 구성
- [x] Gateway에서 `X-Beta=true` 헤더 라우팅 구성
- [x] Header Predicate가 Path Predicate보다 먼저 적용되는지 확인
