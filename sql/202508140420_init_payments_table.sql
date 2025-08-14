CREATE TABLE payments
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    payment_status VARCHAR(255) NOT NULL,
    payment_key VARCHAR(255),
    amount INT NOT NULL,
    method VARCHAR(255) NOT NULL,
    easy_pay_provider VARCHAR(255),
    receipt_url TEXT,
    requested_at DATETIME,
    approved_at DATETIME,
    CONSTRAINT fk_order
        FOREIGN KEY (order_id) REFERENCES orders(id)
);