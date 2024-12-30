CREATE TABLE messages
(
    id             BINARY(16)  NOT NULL COMMENT '메시지 ID',
    channel_id     BINARY(16)  NOT NULL COMMENT '채널 ID',
    sender_user_id BINARY(16)  NOT NULL COMMENT '발신자 사용자 ID',
    content        TEXT        NOT NULL COMMENT '메시지 내용',
    content_type   VARCHAR(10) NOT NULL COMMENT '메시지 타입 (TEXT, CARD)',
    card_color     VARCHAR(10) COMMENT '카드 색상 (BLUE, PINK)',
    status         VARCHAR(10) NOT NULL COMMENT '메시지 상태 (SENT, READ)',
    created_at     DATETIME(6) NOT NULL COMMENT '생성일시',
    updated_at     DATETIME(6) COMMENT '수정일시',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='채팅 메시지';

CREATE INDEX idx_messages_channel_id ON messages (channel_id);
CREATE INDEX idx_messages_sender_user_id ON messages (sender_user_id);
