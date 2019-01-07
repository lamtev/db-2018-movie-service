DELETE
FROM movie
WHERE price = (SELECT max(price) FROM movie)
   OR imdb_rating = (SELECT max(imdb_rating) FROM movie)