SELECT st.name,
       max(s.seasons) AS max_seasons,
       max(s.price)   AS max_price
FROM series s
       JOIN series_translation st ON s.id = st.series_id
GROUP BY st.name
ORDER BY max_seasons DESC, max_price DESC
LIMIT 1
