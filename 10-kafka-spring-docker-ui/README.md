# 10-kafka-spring-docker-ui

Spring Boot와 Docker Compose로 Kafka 단일 노드와 Kafka UI 관리 콘솔을 실행하고, Producer와 Consumer를 직접 구현해 메시지 발행, 수신, Retry, DLT, 트랜잭션 기초 흐름을 확인하는 실습 프로젝트입니다.

## 수업 주제

Apache Kafka 입문 6편: Spring Boot + Docker + 관리 콘솔

## 실습 목표

- Docker Compose로 Kafka KRaft 단일 노드와 Kafka UI를 함께 실행한다.
- Kafka UI에서 토픽을 생성하고 메시지, Consumer Group, Lag을 확인한다.
- Spring Boot에서 `KafkaTemplate`으로 메시지를 발행한다.
- `@KafkaListener`와 수동 ack로 메시지를 처리한다.
- 같은 Key가 같은 파티션에 순서대로 저장되는지 확인한다.
- Retry Topic과 DLT 흐름을 Spring Kafka로 실험한다.
- Kafka 트랜잭션 설정의 기본 구조를 확인한다.

## 서비스 구성

```text
10-kafka-spring-docker-ui/
  docker-compose.yml
  src/
```

| 서비스 | 포트 | 역할 |
| --- | --- | --- |
| `kafka` | `9092` | Kafka KRaft 단일 브로커 |
| `kafka-ui` | `8081` | Kafka 관리 콘솔 |
| `kafka-spring-docker-ui` | `8080` | 메시지 발행 API와 Kafka Consumer |

## Kafka 구성

| 구성요소 | 이번 실습에서의 역할 |
| --- | --- |
| Topic | `orders` 메시지 저장 |
| Partition | Key 기반 메시지 분산과 순서 확인 |
| Producer | `KafkaTemplate`으로 메시지 발행 |
| Consumer | `@KafkaListener`로 메시지 수신 |
| Consumer Group | `order-group`으로 offset과 lag 관리 |
| Kafka UI | 토픽, 메시지, Consumer Group, offset 확인 |

## 실습 단계

### 1. 실습 준비: Docker Compose로 Kafka + Kafka UI 실행

- `docker-compose.yml`을 작성한다.
- Kafka와 Kafka UI 컨테이너를 실행한다.
- Kafka UI에 접속해 클러스터 연결 상태를 확인한다.

### 2. Kafka UI로 토픽 관리하기

- Kafka UI 화면 구성을 확인한다.
- `orders` 토픽을 파티션 3개로 생성한다.
- 토픽 설정, 메시지 조회, 메시지 발행, Consumer Group 화면을 확인한다.

### 3. Spring Boot 프로젝트 설정

- Spring Web과 Spring Kafka 의존성을 사용한다.
- Producer, Consumer, Listener 설정을 작성한다.
- 수동 커밋과 Consumer concurrency를 설정한다.

### 4. Producer: 메시지 보내기

- `OrderProducer`를 구현한다.
- `KafkaTemplate`으로 `orders` 토픽에 Key와 메시지를 전송한다.
- 전송 성공 시 partition과 offset을 로그로 확인한다.

### 5. Consumer: 메시지 받기

- `OrderConsumer`를 구현한다.
- `@KafkaListener`로 `orders` 토픽을 구독한다.
- 처리 성공 후 수동 ack로 offset을 commit한다.

### 6. 테스트용 컨트롤러

- `OrderController`를 구현한다.
- `POST /orders` API로 테스트 메시지를 발행한다.
- Consumer 로그에서 Key, partition, offset, 메시지 내용을 확인한다.

### 7. 직접 확인하는 실습 흐름

- curl 요청부터 Kafka 저장, Consumer 처리까지의 흐름을 확인한다.
- Kafka UI에서 메시지와 Consumer Group 상태를 함께 확인한다.

### 8. 에러 처리: Retry + DLT

- `@RetryableTopic`으로 Retry Topic을 구성한다.
- 실패 메시지를 DLT로 보내고 `@DltHandler`에서 확인한다.
- Kafka UI에서 `orders-retry-*`, `orders-dlt` 토픽을 추적한다.

### 9. 트랜잭션

- Kafka Producer 트랜잭션 설정 구조를 확인한다.
- DB 저장과 Kafka 발행을 함께 처리하는 서비스 구조를 실습한다.

### 10. 더 해보면 좋은 실험

- 같은 Key로 여러 메시지를 보내 같은 파티션에 들어가는지 확인한다.
- Key 없이 메시지를 보내 파티션 분산을 확인한다.
- Consumer를 2개 실행해 파티션 할당을 확인한다.
- Consumer 중지 후 재시작 시 offset부터 이어 읽는지 확인한다.
- FAIL 메시지와 offset reset, rebalance를 실험한다.

### 11. 자주 만나는 문제와 해결

- Docker 미실행이나 bootstrap server 오타로 인한 연결 실패를 확인한다.
- 토픽 미생성, group-id 변경, commit 누락, listener 설정 문제를 점검한다.
- `ADVERTISED_LISTENERS` 설정과 호스트/컨테이너 네트워크 차이를 정리한다.

### 12. 요약

- Kafka UI, Producer, Consumer, Retry, DLT, 트랜잭션의 핵심 흐름을 정리한다.
- 실습 결과와 검증 명령어를 README에 반영한다.

## 실행 순서

1. Kafka와 Kafka UI를 실행한다.
2. Kafka UI에서 `orders` 토픽을 생성한다.
3. Spring Boot 애플리케이션을 실행한다.
4. `/orders` API로 메시지를 발행한다.
5. 애플리케이션 로그와 Kafka UI에서 메시지 흐름을 확인한다.

## 검증

### Kafka 컨테이너 실행 확인

```bash
docker compose ps
```

### Kafka 브로커 동작 확인

```bash
docker exec -it kafka /opt/kafka/bin/kafka-broker-api-versions.sh \
  --bootstrap-server localhost:9092
```

### Kafka UI 접속

```http
GET http://localhost:8081
```

### 메시지 발행 API 호출

```bash
curl -X POST "http://localhost:8080/orders?userId=userA&message=chicken"
```

## 필요한 의존성

현재 프로젝트는 Kafka 실습을 위해 다음 의존성을 사용합니다.

- Spring Web MVC
- Spring for Apache Kafka
- Lombok

예시:

```gradle
implementation 'org.springframework.boot:spring-boot-starter-webmvc'
implementation 'org.springframework.boot:spring-boot-starter-kafka'
```

## 구현 체크리스트

### 1. 실습 준비

- [ ] Spring Boot 프로젝트 생성
- [ ] `docker-compose.yml` 작성
- [ ] Kafka 컨테이너 실행
- [ ] Kafka UI 접속 확인

### 2. Kafka UI 토픽 관리

- [ ] `orders` 토픽 생성
- [ ] 파티션 수 3개 확인
- [ ] Messages 탭 확인
- [ ] Consumers 메뉴 확인

### 3. Spring Boot 설정

- [ ] Kafka Producer 설정 작성
- [ ] Kafka Consumer 설정 작성
- [ ] Listener 수동 ack 설정 작성

### 4. Producer

- [ ] `OrderProducer` 구현
- [ ] 전송 성공 로그 확인

### 5. Consumer

- [ ] `OrderConsumer` 구현
- [ ] 수동 ack 처리
- [ ] Consumer 로그 확인

### 6. 테스트 컨트롤러

- [ ] `OrderController` 구현
- [ ] curl로 메시지 발행
- [ ] 같은 Key의 partition 순서 확인

### 7. 흐름 확인

- [ ] API 요청부터 Consumer 처리까지 검증
- [ ] Kafka UI에서 메시지와 Lag 확인

### 8. Retry + DLT

- [ ] `@RetryableTopic` 적용
- [ ] 실패 메시지 발생
- [ ] Retry Topic 확인
- [ ] DLT 확인

### 9. 트랜잭션

- [ ] Kafka 트랜잭션 설정 작성
- [ ] 트랜잭션 서비스 구조 확인

### 10. 추가 실험

- [ ] 같은 Key 반복 발행 확인
- [ ] Key 없는 메시지 분산 확인
- [ ] Consumer 2개 실행 확인
- [ ] Consumer 중지 후 이어 읽기 확인
- [ ] offset reset 확인
- [ ] rebalance 확인
