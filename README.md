# 3days-server

소개팅 매칭 서비스 백엔드 서버

## 기술 스택

- Kotlin, JDK 21
- Spring Boot 3.x
- JPA (Hibernate), MySQL, Flyway
- Redis (인증/세션/채팅 Pub-Sub)
- AWS S3 (프로필 이미지)
- WebSocket/STOMP (실시간 채팅)

## 아키텍처

헥사고날 아키텍처(Ports and Adapters) 기반 멀티모듈 구조

```
3days-server
├── domain/              # 도메인 모델 (Entity, VO, Repository Interface)
├── application/         # 유스케이스/서비스 (Port Inbound/Outbound)
├── infrastructure/      # 외부 시스템 어댑터
│   ├── persistence/     # JPA (MySQL)
│   ├── redis/           # Redis
│   ├── aws/             # S3
│   ├── rest/            # HTTP Client (Discord 알림)
│   └── sms/             # SMS (CoolSMS)
├── bootstrap/
│   └── api/             # REST API 진입점
└── support/
    └── common/          # 공통 유틸리티 (JWT, Exception 등)
```

## 주요 도메인

| 도메인 | 설명 |
|--------|------|
| `auth` | SMS OTP 인증, JWT 토큰 발급/갱신 |
| `user` | 사용자 프로필, 희망 상대 조건, 프로필 이미지 |
| `connection` | 매칭 연결, 연결 시도(ConnectionAttempt) |
| `chat` | 실시간 채팅 (채널, 메시지, 세션) |

## 실행

```bash
# 로컬 인프라 실행
docker compose -f bootstrap/api/compose.yaml up -d

# 빌드 및 실행
./gradlew :bootstrap:api:bootRun
```

## 테스트

```bash
./gradlew test
```
