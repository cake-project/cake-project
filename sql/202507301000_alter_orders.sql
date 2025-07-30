ALTER TABLE orders
    CHANGE COLUMN order_created_at created_at DATETIME NOT NULL;

ALTER TABLE orders
    ADD COLUMN modified_at DATETIME NOT NULL;