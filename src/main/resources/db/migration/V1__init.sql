CREATE TABLE categories
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(150) NOT NULL,
    type  VARCHAR(20)  NOT NULL CHECK ( type IN ('INCOME', 'EXPENSE') ),
    color VARCHAR(30)
);

CREATE TABLE transactions
(
    id          BIGSERIAL PRIMARY KEY,
    amount      NUMERIC(15, 2) NOT NULL,
    date        DATE           NOT NULL,
    description VARCHAR(150),
    category_id BIGINT         NOT NULL,
    created_at  TIMESTAMP      NOT NULL,
    updated_at  TIMESTAMP      NOT NULL,
    CONSTRAINT fk_transactions_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE RESTRICT
);

CREATE TABLE budgets
(
    id          BIGSERIAL PRIMARY KEY,
    category_id BIGINT         NOT NULL,
    month       INTEGER        NOT NULL,
    year        INTEGER        NOT NULL,
    amount      NUMERIC(15, 2) NOT NULL,
    CONSTRAINT uq_budget_category_date UNIQUE (category_id, month, year),
    CONSTRAINT fk_budgets_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE RESTRICT
);

CREATE INDEX idx_transactions_date ON transactions(date);
CREATE INDEX idx_transactions_category_id ON transactions(category_id);