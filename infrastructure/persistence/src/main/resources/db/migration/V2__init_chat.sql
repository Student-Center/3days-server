CREATE TABLE messages
(
    id              BINARY(16)   NOT NULL COMMENT '메시지 ID',
    channel_id      BINARY(16)   NOT NULL COMMENT '채널 ID',
    sender_user_id  BINARY(16)   NULL COMMENT '발신자 사용자 ID (시스템 메시지는 NULL)',
    content         TEXT         NOT NULL COMMENT '메시지 내용',
    content_type    VARCHAR(10)  NOT NULL COMMENT '메시지 타입 (TEXT, CARD, SYSTEM)',
    card_title      VARCHAR(255) NULL COMMENT '카드 제목',
    card_color      VARCHAR(10)  NULL COMMENT '카드 색상 (BLUE, PINK)',
    system_message_type     VARCHAR(20)  NULL COMMENT '시스템 메시지 타입 (INFO, NEXT_CARD)',
    next_card_title VARCHAR(255) NULL COMMENT '다음 카드 제목',
    status          VARCHAR(10)  NOT NULL COMMENT '메시지 상태 (SENT, READ)',
    created_at      DATETIME(6)  NOT NULL COMMENT '생성일시',
    updated_at      DATETIME(6)  NULL COMMENT '수정일시',
    PRIMARY KEY (id),
    INDEX idx_messages_channel_id (channel_id),
    INDEX idx_messages_channel_id_created_at (channel_id, created_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='채팅 메시지';

CREATE TABLE channels
(
    id            BINARY(16)  NOT NULL COMMENT '채널 ID',
    connection_id BINARY(16)  NOT NULL COMMENT '커넥션 ID',
    created_at    DATETIME(6) NOT NULL COMMENT '생성 일시',
    PRIMARY KEY (id),
    INDEX idx_channels_connection_id (connection_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='채팅 채널';


CREATE TABLE chat_members
(
    id         BINARY(16)  NOT NULL COMMENT '채팅 유저 ID',
    channel_id BINARY(16)  NOT NULL COMMENT '채널 ID',
    joined_at  DATETIME(6) NOT NULL COMMENT '생성 일시',
    PRIMARY KEY (id),
    INDEX idx_chat_members_channel_id (channel_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='채팅 멤버';
