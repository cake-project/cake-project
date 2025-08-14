CREATE TABLE chat_rooms (
                            id VARCHAR(255) NOT NULL, -- UUID 문자열
                            proposal_form_id BIGINT,  -- 견적서와 1:1 매핑
                            PRIMARY KEY (id),
                            CONSTRAINT fk_chat_rooms_proposal_form
                                FOREIGN KEY (proposal_form_id) REFERENCES proposal_forms(id)
                                    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
