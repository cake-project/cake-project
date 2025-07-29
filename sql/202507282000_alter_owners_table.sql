ALTER TABLE owners RENAME COLUMN updated_at TO modified_at;
ALTER TABLE owners MODIFY COLUMN phone_number VARCHAR(255);