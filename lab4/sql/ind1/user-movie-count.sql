SELECT res.user_id,
       count(DISTINCT res.movie_id) AS movie_count
FROM (SELECT user_id, movie_id
      FROM user_movie
      WHERE (SELECT series_season_id
             FROM movie
             WHERE id = movie_id) IS NULL
      UNION
      SELECT s.user_id, sm.movie_id
      FROM subscription s
             JOIN subscription_movie sm
                  ON s.id = sm.subscription_id
     ) AS res
GROUP BY res.user_id;