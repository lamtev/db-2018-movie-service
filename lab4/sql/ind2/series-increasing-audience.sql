SELECT series_increasing_audience.series_id, st.name
FROM (WITH series_season_audience AS (SELECT s.id                       AS series_id,
                                             ss.id                      AS series_season_id,
                                             ss.number                  AS season_number,
                                             count(DISTINCT sb.user_id) AS audience
                                      FROM series_season ss
                                             JOIN series s
                                                  ON ss.series_id = s.id
                                             JOIN subscription_series_season sss
                                                  ON ss.id = sss.series_season_id
                                             JOIN subscription sb
                                                  ON sss.subscription_id = sb.id
                                      GROUP BY s.id, ss.id
  )
  SELECT DISTINCT ssa.series_id
  FROM series_season_audience ssa
         JOIN (SELECT current_series_id,
                      count(diff) AS positive_diffs_count
               FROM (SELECT current.series_id                  AS current_series_id,
                            (next.audience - current.audience) AS diff
                     FROM series_season_audience current
                            JOIN series_season_audience next
                                 ON current.series_id = next.series_id
                                   AND current.season_number = next.season_number - 1
                     GROUP BY current_series_id, diff
                    ) AS diffs
               WHERE diff > 0
               GROUP BY current_series_id) as sd
              ON ssa.series_id = sd.current_series_id
         JOIN series
              ON ssa.series_id = series.id
  WHERE sd.positive_diffs_count = series.seasons - 1) AS series_increasing_audience
       JOIN series_translation st
            ON series_increasing_audience.series_id = st.series_id
WHERE st.language_id = 1
