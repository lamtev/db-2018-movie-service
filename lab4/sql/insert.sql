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
