DELETE
FROM movie
WHERE id IN (SELECT movie_id
             FROM movie_translation
             WHERE language_id NOT IN (1, 2))
