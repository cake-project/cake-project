CREATE TABLE chat_messages (
                               id BIGINT NOT NULL AUTO_INCREMENT,
                               room_id VARCHAR(64) NOT NULL,
                               sender_name VARCHAR(64) NOT NULL,
                               content TEXT NOT NULL,
                               created_at DATETIME(6), -- @CreatedDate → microsecond precision
                               PRIMARY KEY (id),
                               KEY idx_room_created (room_id, created_at),
                               CONSTRAINT fk_chat_messages_room
                                   FOREIGN KEY (room_id) REFERENCES chat_rooms(id)
                                       ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
