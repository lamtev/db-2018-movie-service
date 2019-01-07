SELECT st.name,
       max(s.seasons) as max_seasons,
       max(s.price)   as max_price
FROM series s
       JOIN series_translation st ON s.id = st.series_id
GROUP BY st.name
ORDER BY max_seasons DESC, max_price DESC
LIMIT 1