SELECT count(*)                                             AS users_count,
       max(length(first_name))                              AS longest_firstname,
       min(length(last_name))                               AS shortest_lastname,
       round(avg(date_part('years', age(now(), birthday)))) AS avg_age,
       sum(sex::INT)                                        AS male_count
FROM "user"