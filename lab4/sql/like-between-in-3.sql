SELECT id,
       price,
       date_part('years', age(now(), release_date)) AS years_old,
       imdb_rating
FROM movie
WHERE price IN ('$5', '$7')
  AND imdb_rating BETWEEN 7.5 AND 7.6
  AND series_season_id IS NULL
  AND date_part('month', release_date) = 10