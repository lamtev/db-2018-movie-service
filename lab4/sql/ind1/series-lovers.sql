SELECT u.id, u.first_name, u.last_name, episodes_count, movie_count
FROM (SELECT um.user_id,
             count(DISTINCT um.movie_id) AS movie_count
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
           ) AS um
      GROUP BY um.user_id
     ) AS user_movie_count
       FULL JOIN (SELECT ue.user_id                                  AS user_id,
                         count(DISTINCT ue.series_season_episode_id) AS episodes_count
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
                       ) AS ue
                  GROUP BY ue.user_id) AS user_episode_count
                 ON user_movie_count.user_id = user_episode_count.user_id
       JOIN "user" u
            ON user_movie_count.user_id = u.id OR user_episode_count.user_id = u.id
GROUP BY u.id, episodes_count, movie_count
HAVING episodes_count > movie_count