CREATE TABLE IF NOT EXISTS player
(
  id   SERIAL PRIMARY KEY,
  name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS team
(
  id         SERIAL PRIMARY KEY,
  name       TEXT    NOT NULL,
  captain_id INTEGER NOT NULL REFERENCES player
);

CREATE OR REPLACE FUNCTION on_player_removed()
  RETURNS TRIGGER
  LANGUAGE plpgsql
AS
$$
BEGIN
  IF (TG_OP = 'DELETE') THEN
    DELETE FROM team WHERE captain_id = OLD.id;
    RETURN OLD;
  END IF;
  RETURN NULL;
END;
$$;

CREATE TRIGGER on_player_removed
  BEFORE UPDATE OR DELETE
  ON player
  FOR EACH ROW
EXECUTE PROCEDURE on_player_removed();

INSERT INTO player (name)
VALUES ('Rashford');

INSERT INTO team (name, captain_id)
VALUES ('Manchester United', 1);

SELECT *
FROM team;
-- id|         name      | captain_id
--------------------------------------
-- 1 | Manchester United |	  1

DELETE
FROM player
where name = 'Rashford';

SELECT *
FROM team;

-- Empty table
