SELECT id,
       user_id,
       payment,
       floor(extract(EPOCH FROM age(expires, now())) / 3600 / 24) AS expires_in_days
FROM subscription
WHERE date_part('days', age(expires, started)) = 30
  AND expires > now()
  AND autorenewable
LIMIT 5