CREATE TABLE participant
(
    id             BINARY(16) PRIMARY KEY COMMENT '사용자 ID',
    connection_response VARCHAR(20) NOT NULL COMMENT '연결 응답 (ACCEPTED, REJECTED, WAITING)'
);

CREATE TABLE connection
(
    id              BINARY(16) PRIMARY KEY COMMENT '커넥션 ID',
    participant1_id BINARY(16)  NOT NULL COMMENT '참가자1 ID',
    participant2_id BINARY(16)  NOT NULL COMMENT '참가자2 ID',
    connected_at    DATETIME(6) NOT NULL COMMENT '커넥션 생성 일시',

    INDEX idx_connection_participant1_id (participant1_id),
    INDEX idx_connection_participant2_id (participant2_id)
);
