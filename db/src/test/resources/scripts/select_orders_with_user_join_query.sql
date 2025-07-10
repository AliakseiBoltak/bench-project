SELECT o.id AS order_id, u.username, u.email, o.product, o.amount, o.order_date
FROM orders o
JOIN users u
ON o.user_id = u.id