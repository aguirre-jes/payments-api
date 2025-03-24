CREATE TABLE PAYMENTS (
    id NUMBER,
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    payment_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    payer_id NUMBER NOT NULL,
    payee_id NUMBER NOT NULL,
    payment_method VARCHAR(50),
    transaction_id VARCHAR(50),
    description VARCHAR(255),
    CONSTRAINT pk_payments PRIMARY KEY (id)
);

-- Insert some data
INSERT INTO PAYMENTS (id, amount, currency, payment_date, status, payer_id, payee_id, payment_method, transaction_id, description) 
VALUES (1, 100.00, 'USD', SYSDATE, 'Completed', 101, 201, 'Credit Card', 'TXN123456', 'Payment for invoice #123');
INSERT INTO PAYMENTS (id, amount, currency, payment_date, status, payer_id, payee_id, payment_method, transaction_id, description) 
VALUES (2, 250.50, 'EUR', SYSDATE, 'Pending', 102, 202, 'Bank Transfer', 'TXN123457', 'Payment for invoice #124');
INSERT INTO PAYMENTS (id, amount, currency, payment_date, status, payer_id, payee_id, payment_method, transaction_id, description) 
VALUES (3, 75.75, 'GBP', SYSDATE, 'Failed', 103, 203, 'PayPal', 'TXN123458', 'Payment for invoice #125');
INSERT INTO PAYMENTS (id, amount, currency, payment_date, status, payer_id, payee_id, payment_method, transaction_id, description) 
VALUES (4, 75.75, 'GBP', SYSDATE, 'Completed', 103, 203, 'PayPal', 'TXN123459', 'Payment for invoice #124');
INSERT INTO PAYMENTS (id, amount, currency, payment_date, status, payer_id, payee_id, payment_method, transaction_id, description) 
VALUES (5, 75.75, 'GBP', SYSDATE, 'Completed', 103, 203, 'PayPal', 'TXN123460', 'Payment for invoice #125');
INSERT INTO PAYMENTS (id, amount, currency, payment_date, status, payer_id, payee_id, payment_method, transaction_id, description) 
VALUES (6, 75.75, 'GBP', SYSDATE, 'Completed', 103, 203, 'PayPal', 'TXN123461', 'Payment for invoice #126');
INSERT INTO PAYMENTS (id, amount, currency, payment_date, status, payer_id, payee_id, payment_method, transaction_id, description) 
VALUES (7, 75.75, 'GBP', SYSDATE, 'Pending', 103, 203, 'PayPal', 'TXN123462', 'Payment for invoice #127');
