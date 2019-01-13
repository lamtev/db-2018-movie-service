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
LIMIT 2
