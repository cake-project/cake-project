ALTER TABLE customers
    MODIFY COLUMN password VARCHAR(255) NULL;
ALTER TABLE customers
    MODIFY COLUMN password_confirm VARCHAR(255) NULL;
ALTER TABLE customers
    ADD COLUMN provider VARCHAR(20) NOT NULL,
    ADD COLUMN provider_id BIGINT null ;
ALTER TABLE customers
    ADD COLUMN customer_key VARCHAR(255);