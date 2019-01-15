CREATE OR REPLACE FUNCTION check_for_duplicates()
  RETURNS TRIGGER
  LANGUAGE plpgsql
AS
$$
DECLARE
  subscription_user_id BIGINT;
  subscription_id      BIGINT;
BEGIN
  subscription_user_id := (SELECT user_id
                           FROM subscription
                           WHERE id = NEW.subscription_id
                           LIMIT 1);
  subscription_id :=
      (SELECT s.id
       FROM subscription s
              JOIN subscription_series_season sss
                   ON s.user_id = subscription_user_id
       WHERE sss.series_season_id = NEW.series_season_id
       LIMIT 1);
  IF subscription_id IS NOT NULL THEN
    RAISE EXCEPTION 'User (id=%) already has subscription (id=%) which contains series season (id=%)', subscription_user_id, subscription_id, NEW.series_season_id;
  END IF;
  RETURN NEW;
END;
$$;

CREATE TRIGGER check_for_duplicates
  BEFORE INSERT OR UPDATE
  ON subscription_series_season
  FOR EACH ROW
EXECUTE PROCEDURE check_for_duplicates();

SELECT *
FROM subscription_series_season
LIMIT 1;

-- subscription_id | series_season_id
--------------------------------------
--       26        |      1123

SELECT user_id
FROM subscription
WHERE id = 26;

-- user_id
-----------
--  8733

INSERT INTO subscription (user_id, started, expires, autorenewable, payment)
VALUES (8733, now(), now(), TRUE, '$55.00');

SELECT *
FROM subscription
WHERE user_id = 8733
  AND payment = '$55.00';
--   id   | user_id |   started    |   expires   |  autorenewable | payment
----------------------------------------------------------------------------
-- 160331 |   8733  | '2019-01-15' | '2019-01-15'|     TRUE      | '$55.00'

INSERT INTO subscription_series_season (subscription_id, series_season_id)
VALUES (160331, 1123);

-- ERROR: User (id=8733) already has subscription (id=18) which contains series season (id=1123)
-- Where: PL/pgSQL function check_for_duplicates() line 18 at RAISE
