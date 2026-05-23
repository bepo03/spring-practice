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

## 구현 체크리스트

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
