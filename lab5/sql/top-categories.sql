CREATE OR REPLACE FUNCTION year_category_subscription_count(year_ago SMALLINT)
  RETURNS TABLE
          (
            year               SMALLINT,
            category_id        SMALLINT,
            subscription_count BIGINT,
            place              SMALLINT
          )
  LANGUAGE SQL
AS
$$
SELECT year_ago       AS year,
       mc.category_id AS category_id,
       count(s.id)    AS subscription_count,
       (row_number() OVER (ORDER BY count(s.id) DESC))::SMALLINT
FROM movie_category mc
       JOIN subscription_movie sm
            ON mc.movie_id = sm.movie_id
       JOIN subscription s
            ON sm.subscription_id = s.id
WHERE floor(extract(EPOCH FROM age(now(), s.started)) / 3600 / 24 / 365) = year_ago
GROUP BY mc.category_id
ORDER BY subscription_count DESC
LIMIT 10
$$;


CREATE OR REPLACE FUNCTION top_10_categories_within_3_last_years()
  RETURNS TABLE
          (
            category_id                    SMALLINT,
            curr_place                     SMALLINT,
            prev_place                     SMALLINT,
            curr_prev_place_delta          SMALLINT,
            curr_subscr_cnt                BIGINT,
            prev_subscr_cnt                BIGINT,
            curr_prev_subscr_cnt_delta     BIGINT,
            curr_prev_subscr_cnt_rel       NUMERIC,
            prevprev_place                 SMALLINT,
            curr_prevprev_place_delta      SMALLINT,
            prevprev_subscr_cnt            BIGINT,
            curr_prevprev_subscr_cnt_delta BIGINT,
            curr_prevprev_subscr_cnt_rel   NUMERIC
          )
  LANGUAGE SQL
AS
$$
SELECT curr.category_id                                        AS category_id,
       curr.place                                              AS curr_place,
       prev.place                                              AS prev_place,
       (-curr.place + prev.place)                              AS curr_prev_place_delta,
       curr.subscription_count                                 AS curr_subscr_cnt,
       prev.subscription_count                                 AS prev_subscr_cnt,
       (curr.subscription_count - prev.subscription_count)     AS curr_prev_subscr_cnt_delta,
       round((((curr.subscription_count -
                prev.subscription_count)::FLOAT)
         / prev.subscription_count::FLOAT)::NUMERIC, 2)        AS curr_prev_subscr_cnt_rel,
       prevprev.place                                          AS prevprev_place,
       (-curr.place + prevprev.place)                          AS curr_prevprev_place_delta,
       prevprev.subscription_count                             AS prevprev_subscr_cnt,
       (curr.subscription_count - prevprev.subscription_count) AS curr_prevprev_subscr_cnt_delta,
       round((((curr.subscription_count -
                prevprev.subscription_count)::FLOAT)
         / prevprev.subscription_count::FLOAT)::NUMERIC, 2)    AS curr_prevprev_subscr_cnt_rel

FROM year_category_subscription_count(0::SMALLINT) curr
       LEFT JOIN year_category_subscription_count(1::SMALLINT) prev
                 ON curr.category_id = prev.category_id
       LEFT JOIN year_category_subscription_count(2::SMALLINT) prevprev
                 ON curr.category_id = prevprev.category_id
ORDER BY curr_place
$$;

SELECT *
FROM top_10_categories_within_3_last_years()
