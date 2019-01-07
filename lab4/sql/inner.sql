SELECT mt.movie_id, mt.name, l.name as locale
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
)