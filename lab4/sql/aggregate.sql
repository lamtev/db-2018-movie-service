SELECT count(*)                                             as users_count,
       max(length(first_name))                              as longest_firstname,
       min(length(last_name))                               as shortest_lastname,
       round(avg(date_part('years', age(now(), birthday)))) as avg_age,
       sum(sex::INT)                                        as male_count
FROM "user"