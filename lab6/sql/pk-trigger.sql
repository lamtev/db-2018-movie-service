CREATE OR REPLACE FUNCTION language_pk()
  RETURNS TRIGGER
  LANGUAGE plpgsql
AS
$$
BEGIN
  SELECT nextval('language_id_seq') INTO NEW.id;
  RETURN NEW;
END;
$$;

CREATE TRIGGER language_pk
  BEFORE INSERT
  ON language
  FOR EACH ROW
EXECUTE PROCEDURE language_pk();

SELECT *
FROM language
WHERE id = (SELECT MAX(id)
            FROM language);
-- id | name
--------------
-- 3  | es-ES

INSERT INTO language
VALUES (DEFAULT, 'it-IT');

SELECT *
FROM language
WHERE id = (SELECT MAX(id)
            FROM language);
-- id | name
--------------
-- 4  | it-IT
