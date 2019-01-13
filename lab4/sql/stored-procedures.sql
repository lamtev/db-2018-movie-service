CREATE PROCEDURE insert_one_value_to_all_tables()
  LANGUAGE SQL
AS
$$
INSERT INTO language (name)
VALUES ('sp-Ar');
INSERT INTO "user" (login, password_hash, email, birthday, sex, first_name, last_name)
VALUES ('null', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ123456', 'email@email.email', '1984-11-12', '0', 'Firstname', 'Lastname');
INSERT INTO series (seasons, price)
VALUES (1, 25);
INSERT INTO series_season (series_id, number)
VALUES (1, 1);
INSERT INTO series_translation (series_id, language_id, name, director, description)
VALUES (1, 3, 'Silicon Valley', 'Some Director', 'Cool series');
INSERT INTO series_season_translation (series_season_id, language_id, name, description)
VALUES (1, 3, 'First season', 'Very cool season');
INSERT INTO movie (price, release_date, imdb_rating, series_season_id)
VALUES (5, '2018-03-18', 9.0, 1);
INSERT INTO movie_translation (movie_id, language_id, name, director, description, file_url)
VALUES (1, 3, 'Episode 1', 'Some Director', 'First episode', 'https://url.to.video.com/file1');
INSERT INTO user_movie (user_id, movie_id, payment)
VALUES (1, 1, 5);
INSERT INTO subscription (user_id, started, expires, autorenewable, payment)
VALUES (1, '2018-12-22', '2019-12-22', FALSE, 15);
INSERT INTO subscription_movie (subscription_id, movie_id)
VALUES (1, 1);
INSERT INTO subscription_series_season (subscription_id, series_season_id)
VALUES (1, 1);
INSERT INTO category (name, supercategory_id)
VALUES ('somegenre', NULL);
INSERT INTO movie_category (movie_id, category_id)
VALUES (1, 1);
INSERT INTO category_translation (category_id, language_id, translation)
VALUES (1, 3, 'somegenre');
$$;
CALL insert_one_value_to_all_tables();


CREATE PROCEDURE update_login_to_new_login(old_login VARCHAR(50), new_login VARCHAR(50))
  LANGUAGE SQL
AS
$$
UPDATE "user"
SET login = old_login
WHERE login = new_login
$$;
CALL insert_one_value_to_all_tables('null', 'notnull');


CREATE PROCEDURE delete_movie_with_max_price_or_max_rating()
  LANGUAGE SQL
AS
$$
DELETE
FROM movie
WHERE price = (SELECT max(price) FROM movie)
   OR imdb_rating = (SELECT max(imdb_rating) FROM movie)
$$;
CALL delete_movie_with_max_price_or_max_rating();

CREATE PROCEDURE delete_movie_if_it_has_not_got_translations(tr1 INTEGER, tr2 INTEGER)
  LANGUAGE SQL
AS
$$
DELETE
FROM movie
WHERE id IN (SELECT movie_id
             FROM movie_translation
             WHERE language_id NOT IN (tr1, tr2))
$$;
CALL delete_movie_if_it_has_not_got_translations(1, 2);
