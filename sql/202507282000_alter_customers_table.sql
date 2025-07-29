ALTER TABLE customers RENAME COLUMN updated_at TO modified_at;
ALTER TABLE customers MODIFY COLUMN phone_number VARCHAR(255);