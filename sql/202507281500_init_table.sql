CREATE TABLE customers
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,

    email            VARCHAR(255) NOT NULL UNIQUE,
    password         VARCHAR(255) NOT NULL,
    password_confirm VARCHAR(255) NOT NULL,
    name             VARCHAR(255) NOT NULL,
    phone_number     VARCHAR(20)  NOT NULL,

    is_deleted       BOOLEAN      NOT NULL DEFAULT FALSE,

    created_at       DATETIME     NOT NULL,
    updated_at       DATETIME     NOT NULL
);
CREATE TABLE owners
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,

    email            VARCHAR(255) NOT NULL UNIQUE,
    password         VARCHAR(255) NOT NULL,
    password_confirm VARCHAR(255) NOT NULL,
    name             VARCHAR(255) NOT NULL,
    phone_number     VARCHAR(20)  NOT NULL,

    is_deleted       BOOLEAN      NOT NULL DEFAULT FALSE,

    created_at       DATETIME     NOT NULL,
    updated_at       DATETIME     NOT NULL
);
CREATE TABLE members
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,

    customer_id BIGINT,
    owner_id    BIGINT,

    is_deleted  BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_member_customer FOREIGN KEY (customer_id) REFERENCES customers (id),
    CONSTRAINT fk_member_owner FOREIGN KEY (owner_id) REFERENCES owners (id)
);
CREATE TABLE stores
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id        BIGINT,
    business_name   VARCHAR(255)        NOT NULL,
    name            VARCHAR(255)        NOT NULL,
    address         VARCHAR(255)        NOT NULL,
    business_number VARCHAR(255) UNIQUE NOT NULL,
    phone_number    VARCHAR(255)        NOT NULL,
    image           VARCHAR(255),
    is_active       BOOLEAN             NOT NULL,
    is_deleted      BOOLEAN             NOT NULL DEFAULT FALSE,
    created_at      DATETIME            NOT NULL,
    updated_at      DATETIME            NOT NULL,

    CONSTRAINT fk_store_owner FOREIGN KEY (owner_id) REFERENCES owners (id)
);
CREATE TABLE request_forms
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    proposal_form_id    BIGINT,
    customer_id         BIGINT       NOT NULL,
    title               VARCHAR(255) NOT NULL,
    region              VARCHAR(255) NOT NULL,
    content             TEXT         NOT NULL,
    desired_price       INT          NOT NULL,
    image               TEXT,
    desired_pickup_date DATETIME     NOT NULL,
    status              VARCHAR(50)  NOT NULL DEFAULT 'REQUESTED',
    created_at          DATETIME     NOT NULL,
    is_deleted          BOOLEAN      NOT NULL DEFAULT FALSE
);
alter table request_forms
    ADD CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers (id);
CREATE TABLE proposal_forms
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_form_id      BIGINT,
    store_id             BIGINT,
    owner_id             BIGINT,
    store_name           VARCHAR(255) NOT NULL,
    manager_name         VARCHAR(255),
    title                VARCHAR(255) NOT NULL,
    content              TEXT         NOT NULL,
    proposed_price       INT          NOT NULL,
    image                TEXT,
    proposed_pickup_date DATETIME     NOT NULL,
    created_at           DATETIME     NOT NULL,
    status               VARCHAR(50)  NOT NULL DEFAULT 'AWAITING',
    is_deleted           BOOLEAN      NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_store FOREIGN KEY (store_id) REFERENCES stores (id),
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES owners (id)
);
ALTER TABLE request_forms
    ADD CONSTRAINT fk_proposal_form FOREIGN KEY (proposal_form_id) REFERENCES proposal_forms (id);

ALTER TABLE proposal_forms
    ADD CONSTRAINT fk_request_form FOREIGN KEY (request_form_id) REFERENCES request_forms (id);

CREATE TABLE proposalform_comments
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,

    proposalForm_id BIGINT,
    customer_id     BIGINT,
    owner_id        BIGINT,

    content         VARCHAR(1000) NOT NULL,
    is_deleted      BOOLEAN       NOT NULL DEFAULT FALSE,

    created_at      DATETIME,
    updated_at      DATETIME
);
ALTER TABLE proposalform_comments
    ADD CONSTRAINT fk_proposalForm FOREIGN KEY (proposalForm_id) REFERENCES proposal_forms (id);
ALTER TABLE proposalform_comments
    ADD CONSTRAINT fk_propsoal_form_customer FOREIGN KEY (customer_id) REFERENCES customers (id);
ALTER TABLE proposalform_comments
    ADD CONSTRAINT fk_propsoal_form_owner FOREIGN KEY (owner_id) REFERENCES owners (id);

CREATE TABLE orders
(
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,

    order_number          VARCHAR(255) NOT NULL UNIQUE,

    customer_id           BIGINT       NOT NULL,
    store_id              BIGINT       NOT NULL,
    requestForm_id        BIGINT       NOT NULL,
    proposalForm_id       BIGINT       NOT NULL,

    status                VARCHAR(50)  NOT NULL,

    customer_name         VARCHAR(255) NOT NULL,
    customer_phone_number VARCHAR(50)  NOT NULL,

    store_business_name   VARCHAR(255) NOT NULL,
    store_name            VARCHAR(255) NOT NULL,
    product_name          VARCHAR(255),

    store_phone_number    VARCHAR(50)  NOT NULL,
    store_address         VARCHAR(255) NOT NULL,

    agreed_price          INT          NOT NULL,
    agreed_pickup_date    DATETIME     NOT NULL,

    final_cake_image      VARCHAR(255),
    order_created_at      DATETIME     NOT NULL,

    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customers (id),
    CONSTRAINT fk_orders_store FOREIGN KEY (store_id) REFERENCES stores (id),
    CONSTRAINT fk_orders_requestForm FOREIGN KEY (requestForm_id) REFERENCES request_forms (id),
    CONSTRAINT fk_orders_proposalForm FOREIGN KEY (proposalForm_id) REFERENCES proposal_forms (id)
);

ALTER TABLE customers RENAME COLUMN updated_at TO modified_at;
ALTER TABLE customers MODIFY COLUMN phone_number VARCHAR(255);

ALTER TABLE owners RENAME COLUMN updated_at TO modified_at;
ALTER TABLE owners MODIFY COLUMN phone_number VARCHAR(255);

ALTER TABLE stores MODIFY COLUMN image TEXT;

ALTER TABLE proposalform_comments RENAME COLUMN updated_at TO modified_at;

ALTER TABLE stores RENAME COLUMN updated_at TO modified_at;

ALTER TABLE request_forms MODIFY COLUMN status VARCHAR(255);

ALTER TABLE proposal_forms MODIFY COLUMN status VARCHAR(255);

ALTER TABLE orders MODIFY COLUMN final_cake_image TEXT;