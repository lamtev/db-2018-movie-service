INSERT INTO language (name)
VALUES ('en-US'),
       ('ru-RU');

INSERT INTO "user" (login, password_hash, email, birthday, sex, first_name, last_name)
VALUES ('login', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ123456', 'email@email.email', '1984-11-12', '0', 'Firstname', 'Lastname');

INSERT INTO series (seasons, price)
VALUES (2, 25);

INSERT INTO series_season (series_id, number)
VALUES (1, 1),
       (1, 2);

INSERT INTO series_translation (series_id, language_id, name, director, description)
VALUES (1, 1, 'Silicon Valley', 'Some Director', 'Cool series');

INSERT INTO series_season_translation (series_season_id, language_id, name, description)
VALUES (1, 1, 'First season', 'Very cool season'),
       (2, 1, 'Second season', 'Cooler season');

INSERT INTO movie (price, release_date, imdb_rating, series_season_id)
VALUES (5, '2018-03-18', 9.0, 1),
       (5, '2018-03-18', 8.0, 1),
       (5, '2018-03-18', 9.5, 2),
       (5, '2018-03-18', 8.5, 2),
       (7, '2012-03-04', 8.0, NULL);

INSERT INTO movie_translation (movie_id, language_id, name, director, description, file_url)
VALUES (1, 1, 'Episode 1', 'Some Director', 'First episode', 'https://url.to.video.com/file1'),
       (2, 1, 'Episode 2', 'Some Director', 'First episode', 'https://url.to.video.com/file2'),
       (3, 1, 'Episode 1', 'Some Director', 'First episode', 'https://url.to.video.com/file3'),
       (4, 1, 'Episode 1', 'Some Director', 'First episode', 'https://url.to.video.com/file4'),
       (5, 1, 'Departures', 'Martin Scorsese', 'Cool movie', 'https://url.to.video.com/file5');

INSERT INTO user_movie (user_id, movie_id, payment)
VALUES (1, 5, 7);

INSERT INTO subscription (user_id, started, expires, autorenewable, payment)
VALUES (1, '2018-12-22', '2019-12-22', TRUE, 15),
       (2, '2018-12-19', '2019-06-19', TRUE, 15);
