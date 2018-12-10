CREATE TABLE IF NOT EXISTS "language"
(
  "id"   SERIAL PRIMARY KEY,
  "name" CHAR(5) NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS "series"
(
  "id"      SERIAL PRIMARY KEY,
  "seasons" SMALLINT NOT NULL,
  "price"   MONEY    NOT NULL
);


CREATE TABLE IF NOT EXISTS "series_season"
(
  "id"        SERIAL PRIMARY KEY,
  "series_id" INTEGER  NOT NULL,
  "number"    SMALLINT NOT NULL,

  FOREIGN KEY ("series_id") REFERENCES "series" ("id")
);


CREATE TABLE IF NOT EXISTS "series_translation"
(
  "series_id"   INTEGER       NOT NULL,
  "language_id" INTEGER       NOT NULL,
  "name"        VARCHAR(100)  NOT NULL,
  "director"    VARCHAR(100)  NOT NULL,
  "description" VARCHAR(1000) NOT NULL,

  CONSTRAINT "series_translation_pk" PRIMARY KEY ("series_id", "language_id"),
  FOREIGN KEY ("series_id") REFERENCES "series" ("id"),
  FOREIGN KEY ("language_id") REFERENCES "language" ("id")
);


CREATE TABLE IF NOT EXISTS "series_season_translation"
(
  "series_season_id" INTEGER       NOT NULL,
  "language_id"      INTEGER       NOT NULL,
  "name"             VARCHAR(100)  NOT NULL,
  "description"      VARCHAR(1000) NOT NULL,

  CONSTRAINT "series_season_translation_pk" PRIMARY KEY ("series_season_id", "language_id"),
  FOREIGN KEY ("series_season_id") REFERENCES "series_season" ("id"),
  FOREIGN KEY ("language_id") REFERENCES "language" ("id")
);


CREATE TABLE IF NOT EXISTS "movie"
(
  "id"               SERIAL PRIMARY KEY,
  "price"            MONEY NOT NULL,
  "release_date"     DATE  NOT NULL,
  "imdb_rating"      REAL  NOT NULL,
  "series_season_id" INTEGER,

  FOREIGN KEY ("series_season_id") REFERENCES "series_season" ("id")
);


CREATE TABLE IF NOT EXISTS "movie_translation"
(
  "movie_id"    INTEGER       NOT NULL,
  "language_id" INTEGER       NOT NULL,
  "name"        VARCHAR(100)  NOT NULL,
  "director"    VARCHAR(100)  NOT NULL,
  "description" VARCHAR(1000) NOT NULL,
  "file_url"    VARCHAR(100)  NOT NULL,

  CONSTRAINT "movie_translation_pk" PRIMARY KEY ("movie_id", "language_id"),
  FOREIGN KEY ("movie_id") REFERENCES "movie" ("id"),
  FOREIGN KEY ("language_id") REFERENCES "language" ("id")
);

CREATE TABLE IF NOT EXISTS "user"
(
  "id"            BIGSERIAL PRIMARY KEY,
  "login"         VARCHAR(50) NOT NULL UNIQUE,
  "password_hash" CHAR(32)    NOT NULL,
  "email"         VARCHAR(50) NOT NULL UNIQUE,
  "birthday"      DATE        NOT NULL,
  "sex"           CHAR(1)     NOT NULL,
  "first_name"    VARCHAR(50) NOT NULL,
  "last_name"     VARCHAR(50) NOT NULL
);


CREATE TABLE IF NOT EXISTS "user_movie"
(
  "user_id"  BIGINT  NOT NULL,
  "movie_id" INTEGER NOT NULL,
  "payment"  MONEY   NOT NULL,

  CONSTRAINT "user_movie_pk" PRIMARY KEY ("user_id", "movie_id"),
  FOREIGN KEY ("user_id") REFERENCES "user" ("id"),
  FOREIGN KEY ("movie_id") REFERENCES "movie" ("id")
);


CREATE TABLE IF NOT EXISTS "user_series"
(
  "user_id"   BIGINT  NOT NULL,
  "series_id" INTEGER NOT NULL,
  "payment"   MONEY   NOT NULL,

  CONSTRAINT "user_series_pk" PRIMARY KEY ("user_id", "series_id"),
  FOREIGN KEY ("user_id") REFERENCES "user" ("id"),
  FOREIGN KEY ("series_id") REFERENCES "series" ("id")
);


CREATE TABLE IF NOT EXISTS "subscription"
(
  "id"            SERIAL PRIMARY KEY,
  "user_id"       BIGINT  NOT NULL,
  "started"       DATE    NOT NULL,
  "expires"       DATE    NOT NULL,
  "autorenewable" BOOLEAN NOT NULL,
  "payment"       MONEY   NOT NULL
);


CREATE TABLE IF NOT EXISTS "subscription_movie"
(
  "subscription_id" BIGINT  NOT NULL,
  "movie_id"        INTEGER NOT NULL,

  CONSTRAINT "subscription_movie_pk" PRIMARY KEY ("subscription_id", "movie_id"),
  FOREIGN KEY ("subscription_id") REFERENCES "subscription" ("id"),
  FOREIGN KEY ("movie_id") REFERENCES "movie" ("id")
);


CREATE TABLE IF NOT EXISTS "subscription_series_season"
(
  "subscription_id"  BIGINT  NOT NULL,
  "series_season_id" INTEGER NOT NULL,

  CONSTRAINT "subscription_series_season_pk" PRIMARY KEY ("subscription_id", "series_season_id"),
  FOREIGN KEY ("subscription_id") REFERENCES "subscription" ("id"),
  FOREIGN KEY ("series_season_id") REFERENCES "series_season" ("id")
);


CREATE TABLE IF NOT EXISTS "category"
(
  "id"               SERIAL PRIMARY KEY,
  "name"             VARCHAR(50) NOT NULL,
  "supercategory_id" SMALLINT,

  FOREIGN KEY ("supercategory_id") REFERENCES "category" ("id")
);


CREATE TABLE IF NOT EXISTS "movie_category"
(
  "movie_id"    SERIAL   NOT NULL,
  "category_id" SMALLINT NOT NULL,

  CONSTRAINT "movie_category_pk" PRIMARY KEY ("movie_id", "category_id"),
  FOREIGN KEY ("movie_id") REFERENCES "movie" ("id"),
  FOREIGN KEY ("category_id") REFERENCES "category" ("id")
);


CREATE TABLE IF NOT EXISTS "category_translation"
(
  "category_id" SMALLINT    NOT NULL,
  "language_id" SMALLINT    NOT NULL,
  "translation" VARCHAR(50) NOT NULL,

  CONSTRAINT "category_translation_pk" PRIMARY KEY ("category_id", "language_id"),
  FOREIGN KEY ("category_id") REFERENCES "category" ("id"),
  FOREIGN KEY ("language_id") REFERENCES "language" ("id")
);
