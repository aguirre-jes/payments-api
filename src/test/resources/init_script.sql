CREATE TABLE PAYMENTS (
    id INTEGER PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    payer_id INTEGER NOT NULL,
    payee_id INTEGER NOT NULL,
    payment_method VARCHAR(50),
    transaction_id VARCHAR(50),
    description VARCHAR(255)
);

-- Insertar datos de ejemplo en la tabla PAYMENTS
INSERT INTO PAYMENTS (id, amount, currency, payment_date, status, payer_id, payee_id, payment_method, transaction_id, description) 
VALUES (1, 100.00, 'USD', '2025-02-26 10:00:00', 'Completed', 101, 201, 'Credit Card', 'TXN123456', 'Payment for invoice #123');
INSERT INTO PAYMENTS (id, amount, currency, payment_date, status, payer_id, payee_id, payment_method, transaction_id, description) 
VALUES (2, 250.50, 'EUR', '2025-02-26 11:00:00', 'Pending', 102, 202, 'Bank Transfer', 'TXN123457', 'Payment for invoice #124');
INSERT INTO PAYMENTS (id, amount, currency, payment_date, status, payer_id, payee_id, payment_method, transaction_id, description) 
VALUES (3, 75.75, 'GBP', '2025-02-26 12:00:00', 'Failed', 103, 203, 'PayPal', 'TXN123458', 'Payment for invoice #125');