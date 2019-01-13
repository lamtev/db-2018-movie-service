CREATE VIEW like_between_in_1 AS
SELECT *
FROM "user"
WHERE sex = '1'
  AND length(login) BETWEEN 7 AND 11
  AND birthday < '2001-12-30';

CREATE VIEW like_between_in_2 AS
SELECT id, login, email, birthday, sex, first_name, last_name
FROM "user"
WHERE sex = '0'
  AND login LIKE 'Marie.%';

CREATE VIEW like_between_in_2 AS
SELECT id,
       price,
       date_part('years', age(now(), release_date)) AS years_old,
       imdb_rating
FROM movie
WHERE price IN ('$5', '$7')
  AND imdb_rating BETWEEN 7.5 AND 7.6
  AND series_season_id IS NULL
  AND date_part('month', release_date) = 10;


CREATE VIEW calculated_fields AS
SELECT id,
       user_id,
       payment,
       floor(extract(EPOCH FROM age(expires, now())) / 3600 / 24) AS expires_in_days
FROM subscription
WHERE date_part('days', age(expires, started)) = 30
  AND expires > now()
  AND autorenewable
LIMIT 5;


CREATE VIEW sorted AS
SELECT *
FROM series
ORDER BY seasons ASC, price DESC
LIMIT 5;


CREATE VIEW aggregated AS
SELECT count(*)                                             AS users_count,
       max(length(first_name))                              AS longest_firstname,
       min(length(last_name))                               AS shortest_lastname,
       round(avg(date_part('years', age(now(), birthday)))) AS avg_age,
       sum(sex::INT)                                        AS male_count
FROM "user";


CREATE VIEW join1 AS
SELECT st.name,
       max(s.seasons) AS max_seasons,
       max(s.price)   AS max_price
FROM series s
       JOIN series_translation st ON s.id = st.series_id
GROUP BY st.name
ORDER BY max_seasons DESC, max_price DESC
LIMIT 1;


CREATE VIEW join2 AS
SELECT mt.movie_id,
       mt.name,
       l.name             AS locale,
       count(um.movie_id) AS frequency
FROM movie_translation mt
       JOIN language l ON mt.language_id = l.id
       JOIN user_movie um ON mt.movie_id = um.movie_id
WHERE mt.movie_id in (SELECT id
                      FROM movie
                      WHERE id = mt.movie_id
                        AND series_season_id IS NULL)
GROUP BY mt.movie_id, mt.name, locale
ORDER BY frequency DESC
LIMIT 2;


CREATE VIEW inner_sel AS
SELECT mt.movie_id, mt.name, l.name AS locale
FROM movie_translation mt
       JOIN language l ON mt.language_id = l.id
WHERE movie_id in (SELECT movie_id
                   FROM user_movie
                   WHERE movie_id in (SELECT id
                                      FROM movie
                                      WHERE id = movie_id
                                        AND series_season_id IS NULL)
                   GROUP BY movie_id
                   ORDER BY count(movie_id) DESC
                   LIMIT 1
);


CREATE VIEW grouped AS
SELECT user_id
FROM subscription
GROUP BY user_id
HAVING count(user_id) > 42;


CREATE VIEW series_lovers AS
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
HAVING episodes_count > movie_count;


CREATE VIEW series_increasing_audience AS
SELECT series_increasing_audience.series_id, st.name
FROM (WITH series_season_audience AS (SELECT s.id                       AS series_id,
                                             ss.id                      AS series_season_id,
                                             ss.number                  AS season_number,
                                             count(DISTINCT sb.user_id) AS audience
                                      FROM series_season ss
                                             JOIN series s
                                                  ON ss.series_id = s.id
                                             JOIN subscription_series_season sss
                                                  ON ss.id = sss.series_season_id
                                             JOIN subscription sb
                                                  ON sss.subscription_id = sb.id
                                      GROUP BY s.id, ss.id
  )
  SELECT DISTINCT ssa.series_id
  FROM series_season_audience ssa
         JOIN (SELECT current_series_id,
                      count(diff) AS positive_diffs_count
               FROM (SELECT current.series_id                  AS current_series_id,
                            (next.audience - current.audience) AS diff
                     FROM series_season_audience current
                            JOIN series_season_audience next
                                 ON current.series_id = next.series_id
                                   AND current.season_number = next.season_number - 1
                     GROUP BY current_series_id, diff
                    ) AS diffs
               WHERE diff > 0
               GROUP BY current_series_id) as sd
              ON ssa.series_id = sd.current_series_id
         JOIN series
              ON ssa.series_id = series.id
  WHERE sd.positive_diffs_count = series.seasons - 1) AS series_increasing_audience
       JOIN series_translation st
            ON series_increasing_audience.series_id = st.series_id
WHERE st.language_id = 1;
