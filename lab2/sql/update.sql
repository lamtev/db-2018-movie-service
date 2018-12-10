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
