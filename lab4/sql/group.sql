SELECT user_id
FROM subscription
GROUP BY user_id
HAVING count(user_id) > 42
