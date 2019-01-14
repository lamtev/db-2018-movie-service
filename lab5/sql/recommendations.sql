CREATE OR REPLACE FUNCTION up_to_n_user_favourite_categories(n INTEGER, id BIGINT)
  RETURNS TABLE
          (
            user_id         BIGINT,
            category_id     SMALLINT,
            max_movie_count BIGINT
          )
  LANGUAGE SQL AS
$$
WITH user_category_movie_count AS (
  WITH user_movie_category AS (
    SELECT s.user_id,
           mc.movie_id,
           mc.category_id
    FROM subscription_movie sm
           JOIN subscription s
                ON sm.subscription_id = s.id
           JOIN movie_category mc
                ON sm.movie_id = mc.movie_id
    GROUP BY s.user_id, mc.movie_id, mc.category_id
    )
    SELECT user_id,
           category_id,
           count(movie_id) AS movie_count
    FROM user_movie_category
    GROUP BY user_id, category_id
)
SELECT user_id,
       category_id,
       max(movie_count) AS max_movie_count
FROM user_category_movie_count
WHERE user_id = id
GROUP BY user_id, category_id
ORDER BY max_movie_count DESC
LIMIT n
$$;


CREATE OR REPLACE FUNCTION categories_with_maximum_audience()
  RETURNS TABLE
          (
            category_id SMALLINT,
            movie_id    INTEGER,
            audience    BIGINT
          )
  LANGUAGE SQL AS
$$
SELECT mc.category_id,
       mc.movie_id,
       count(DISTINCT s.user_id) AS audience
FROM movie_category mc
       JOIN subscription_movie sm
            ON mc.movie_id = sm.movie_id
       JOIN movie m
            ON sm.movie_id = m.id
       JOIN subscription s
            ON sm.subscription_id = s.id
WHERE m.series_season_id IS NULL
GROUP BY mc.category_id, mc.movie_id
ORDER BY audience DESC
$$;


CREATE OR REPLACE FUNCTION up_to_n_movie_recommendations_for_user(n INTEGER, u_id BIGINT)
  RETURNS TABLE
          (
            movie_id    INTEGER,
            category_id SMALLINT
          )
  LANGUAGE SQL AS
$$
SELECT DISTINCT cma.movie_id, cma.category_id
FROM categories_with_maximum_audience() cma
WHERE cma.category_id IN (SELECT fc.category_id
                          FROM up_to_n_user_favourite_categories(n, u_id) fc)
LIMIT n
$$;


SELECT *
FROM up_to_n_movie_recommendations_for_user(10, 123)
