CREATE OR REPLACE FUNCTION on_subscription_added()
  RETURNS TRIGGER
  LANGUAGE plpgsql
AS
$$
BEGIN
  PERFORM up_to_n_movie_recommendations_for_user(10, NEW.user_id);
  RETURN NEW;
END;
$$;

CREATE TRIGGER on_subscription_added
  AFTER INSERT
  ON subscription
  FOR EACH ROW
EXECUTE PROCEDURE on_subscription_added();

INSERT INTO subscription (user_id, started, expires, autorenewable, payment)
VALUES (123, now(), now(), TRUE, '$55.0');
