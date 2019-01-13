SELECT res.user_id                                  AS user_id,
       count(DISTINCT res.series_season_episode_id) AS episodes_count
FROM (SELECT us.user_id AS user_id,
             m.id       AS series_season_episode_id
      FROM user_series us
             JOIN series_season ss
                  ON us.series_id = ss.series_id
             JOIN movie m
                  ON ss.id = m.series_season_id
      UNION
      SELECT s.user_id, sm.movie_id
      FROM subscription s
             JOIN subscription_movie sm
                  ON s.id = sm.subscription_id
     ) AS res
GROUP BY user_id
