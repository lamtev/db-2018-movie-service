SELECT id, login, email, birthday, sex, first_name, last_name
FROM "user"
WHERE sex = '0'
  AND login LIKE 'Marie.%'