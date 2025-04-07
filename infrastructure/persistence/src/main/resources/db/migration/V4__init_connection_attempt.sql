CREATE TABLE connection_attempt
(
    id            BINARY(16) PRIMARY KEY COMMENT '매칭 시도 ID',
    user_id       BINARY(16)  NOT NULL COMMENT '사용자 ID',
    connection_id BINARY(16)  NULL COMMENT '연결 ID',
    status        VARCHAR(20) NOT NULL COMMENT '매칭 시도 상태',
    attempt_date   DATE        NOT NULL COMMENT '매칭 시도 일시',
    created_at     DATETIME(6) NOT NULL COMMENT '생성 일시',

    INDEX idx_connection_attempt_user_id_and_attempt_date (user_id, attempt_date),
    INDEX idx_connection_attempt_attempt_date (attempt_date)
);

CREATE TABLE connection_cancellation
(
    id            BINARY(16) PRIMARY KEY COMMENT '연결 취소 ID',
    user_id       BINARY(16)   NOT NULL COMMENT '취소를 발생시킨 사용자 ID',
    reason        VARCHAR(255) NOT NULL COMMENT '취소 사유',
    detail        VARCHAR(255) NULL COMMENT '취소 사유 디테일',
    created_at    DATETIME(6)  NOT NULL COMMENT '생성 시간',

    INDEX idx_connection_cancellation_user_id (user_id)
);

ALTER TABLE connection ADD COLUMN cancellation_id BINARY(16) NULL COMMENT '연결 취소 ID';
