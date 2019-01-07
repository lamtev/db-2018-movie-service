SELECT *
FROM "user"
WHERE sex = '1'
  AND length(login) BETWEEN 7 AND 11
  AND birthday < '2001-12-30'