# 06-spring-cloud-gateway-mini-msa

Spring Cloud Gateway로 외부 요청을 받아 다운스트림 서비스로 라우팅하는 미니 MSA 실습 프로젝트입니다.

Route, Predicate, Filter를 사용해 `/echo/**` 요청을 `httpbin.org`로 전달하고, 서버 재시작 없이 라우트를 추가/삭제합니다.

## 실습 목표

Spring Cloud Gateway 프로젝트를 생성하고, YAML 설정과 동적 라우팅 API로 `httpbin.org` echo API에 요청을 라우팅합니다.

- Gateway는 외부 요청 진입점 역할을 한다.
- YAML로 `/echo/**` 정적 라우트를 구성한다.
- `RewritePath` 필터로 요청 경로를 재작성한다.
- `AddRequestHeader` 필터로 다운스트림 요청 헤더를 추가한다.
- Actuator로 등록된 Gateway 라우트를 확인한다.
- `RouteDefinitionWriter`와 `RefreshRoutesEvent`로 런타임에 라우트를 추가/삭제한다.

## 서비스 구성

```text
06-spring-cloud-gateway-mini-msa/
  src/
```

| 서비스 | 포트 | 역할 |
| --- | --- | --- |
| `spring-cloud-gateway-mini-msa` | `8000` | 외부 요청 진입점, httpbin 라우팅 |

## Gateway 구성요소

| 구성요소 | 이번 실습에서의 역할 |
| --- | --- |
| Route | `/echo/**` 요청을 `https://httpbin.org`로 보내는 라우팅 규칙 |
| Predicate | `Path=/echo/**` 조건으로 요청 경로를 검사 |
| Filter | `RewritePath`, `AddRequestHeader`로 요청을 변경 |

## 과제 1. Gateway 정적 라우팅 구성

Gateway 라우팅 규칙은 다음과 같습니다.

| 요청 경로 | 라우팅 대상 | 필터 |
| --- | --- | --- |
| `/echo/**` | `https://httpbin.org` | `RewritePath`, `AddRequestHeader` |

### RewritePath

`/echo/**` 요청에서 `/echo` prefix를 제거하고 httpbin으로 전달합니다.

```http
GET http://localhost:8000/echo/headers
```

Gateway 내부에서 다음 요청으로 변환됩니다.

```http
GET https://httpbin.org/headers
```

### AddRequestHeader

Gateway가 다운스트림 서비스로 요청을 보낼 때 다음 헤더를 추가합니다.

```http
X-Gateway: spring-cloud-gateway
```

## 과제 2. 동적 라우팅 API 만들기

`DynamicRouteController`를 만들고, `RouteDefinitionWriter`로 라우트를 추가/삭제합니다.

```text
Client
  -> POST /admin/routes
  -> RouteDefinitionWriter.save(...)
  -> RefreshRoutesEvent
  -> Gateway route cache refresh
```

### 라우트 추가

```http
POST /admin/routes
Content-Type: application/json
```

요청 본문은 `RouteDefinition` JSON 형식으로 전달합니다.

```json
{
  "id": "httpbin-dynamic",
  "uri": "https://httpbin.org",
  "predicates": [
    {
      "name": "Path",
      "args": {
        "_genkey_0": "/echo/**"
      }
    }
  ],
  "filters": [
    {
      "name": "RewritePath",
      "args": {
        "_genkey_0": "/echo/(?<seg>.*)",
        "_genkey_1": "/${seg}"
      }
    },
    {
      "name": "AddRequestHeader",
      "args": {
        "_genkey_0": "X-Gateway",
        "_genkey_1": "spring-cloud-gateway"
      }
    }
  ]
}
```

### 라우트 삭제

```http
DELETE /admin/routes/{id}
```

## 실행 순서

1. `spring-cloud-gateway-mini-msa` 실행

zsh에서는 프로젝트 폴더에서 다음 명령으로 실행합니다.

```bash
./gradlew bootRun
```

IntelliJ에서는 `Application` 클래스를 실행해도 됩니다.

## 검증

### 정적 라우팅 확인

Gateway를 통해 httpbin echo API를 호출합니다.

```bash
curl -s http://localhost:8000/echo/headers | python -m json.tool
```

응답의 `headers`에 다음 값이 포함되면 Gateway 필터가 정상 동작한 것입니다.

```json
{
  "X-Gateway": "spring-cloud-gateway"
}
```

### Actuator 라우트 확인

등록된 Gateway 라우트는 Actuator로 확인할 수 있습니다.

```bash
curl -s http://localhost:8000/actuator/gateway/routes | python -m json.tool
```

### 동적 라우트 추가

정적 라우트를 비운 상태에서 먼저 404를 확인합니다.

```bash
curl -s http://localhost:8000/echo/get | python -m json.tool
```

`/admin/routes` API로 라우트를 추가합니다.

```bash
curl -X POST http://localhost:8000/admin/routes \
  -H "Content-Type: application/json" \
  -d '{
    "id": "httpbin-dynamic",
    "uri": "https://httpbin.org",
    "predicates": [
      { "name": "Path", "args": { "_genkey_0": "/echo/**" } }
    ],
    "filters": [
      { "name": "RewritePath", "args": { "_genkey_0": "/echo/(?<seg>.*)", "_genkey_1": "/${seg}" } },
      { "name": "AddRequestHeader", "args": { "_genkey_0": "X-Gateway", "_genkey_1": "spring-cloud-gateway" } }
    ]
  }'
```

등록된 라우트를 확인합니다.

```bash
curl -s http://localhost:8000/actuator/gateway/routes/httpbin-dynamic | python -m json.tool
```

서버 재시작 없이 다시 호출합니다.

```bash
curl -s http://localhost:8000/echo/get | python -m json.tool
```

### 동적 라우트 삭제

동적으로 추가한 라우트를 삭제합니다.

```bash
curl -X DELETE http://localhost:8000/admin/routes/httpbin-dynamic
```

라우트 목록이 비었는지 확인합니다.

```bash
curl -s http://localhost:8000/actuator/gateway/routes | python -m json.tool
```

## 필요한 의존성

현재 프로젝트는 Spring Cloud Gateway 실습을 위해 다음 의존성을 사용합니다.

- Spring Cloud Gateway Server WebFlux
- Spring Boot Actuator

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
- [x] `/echo/**` 라우트 구성
- [x] `RewritePath` 필터 구성
- [x] `AddRequestHeader` 필터 구성
- [x] Gateway 실행 검증
- [x] Actuator 라우트 조회 검증
- [x] `DynamicRouteController` 생성
- [x] `RouteDefinitionWriter`로 라우트 추가 구현
- [x] `RefreshRoutesEvent`로 라우트 캐시 갱신 구현
- [x] 동적 라우트 추가 검증
- [x] 동적 라우트 삭제 검증
