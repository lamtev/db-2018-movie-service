SELECT mc.category_id,
       count(s.id) AS subscription_count
FROM movie_category mc
            JOIN subscription_movie sm
                 ON mc.movie_id = sm.movie_id
            JOIN subscription s
                 ON sm.subscription_id = s.id
WHERE floor(extract(EPOCH FROM age(now(), s.started)) / 3600 / 24 / 365) <= 3
GROUP BY mc.category_id
ORDER BY subscription_count DESC
