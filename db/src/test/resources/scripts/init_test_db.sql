-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS test_db;

-- Create user if not exists and grant privileges
CREATE USER IF NOT EXISTS 'test_user'@'localhost' IDENTIFIED BY 'test_pass';
GRANT ALL PRIVILEGES ON test_db.* TO 'test_user'@'localhost';
FLUSH PRIVILEGES;

-- Use the database for next statements
USE test_db;

-- Create tables
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product VARCHAR(100) NOT NULL,
    amount DECIMAL(10,2),
    order_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Insert sample users
INSERT INTO users (username, email) VALUES
('alice', 'alice@example.com'),
('bob', 'bob@example.com');

-- Insert sample orders
INSERT INTO orders (user_id, product, amount, order_date) VALUES
(1, 'Laptop', 1200.00, '2025-07-04'),
(1, 'Mouse', 25.50, '2025-07-03'),
(2, 'Keyboard', 75.00, '2025-07-02');