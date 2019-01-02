package com.lamtev.movie_service.datagen.generator.movie;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;
import org.postgresql.util.PGmoney;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.concurrent.TimeUnit;

public class MovieTableGenerator implements TableGenerator {

    private static final short[] MOVIE_PRICES_IN_USD = new short[]{5, 5, 5, 5, 5, 7, 7, 7, 7, 10, 10, 10, 15, 15, 20, 25, 35};
    private final int movieCount;
    private final int seriesSeasonId;
    private final int seriesPrice;

    public MovieTableGenerator(int movieCount) {
        this(movieCount, 0, 0);
    }

    public MovieTableGenerator(int movieCount, int seriesSeasonId, int seriesPrice) {
        this.movieCount = movieCount;
        this.seriesSeasonId = seriesSeasonId;
        this.seriesPrice = seriesPrice;
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        try (final var statement = connection.prepareStatement(
                "INSERT INTO movie (price, release_date, imdb_rating, series_season_id) VALUES (?, ?, ?, ?)",
                PRIMARY_KEY_ID
        )) {
            for (int i = 0; i < movieCount; ++i) {
                int j = 0;
                statement.setObject(++j, new PGmoney("$" + (
                        seriesPrice == 0 ?
                                MOVIE_PRICES_IN_USD[RANDOM.nextInt(MOVIE_PRICES_IN_USD.length)]
                                : seriesPrice
                )));
                statement.setDate(++j, new java.sql.Date(FAKER.date().past(365 * 50, TimeUnit.DAYS).getTime()));
                statement.setFloat(++j, randomRating());
                if (seriesSeasonId == 0) {
                    statement.setNull(++j, Types.INTEGER);
                } else {
                    statement.setInt(++j, seriesSeasonId);
                }
                statement.addBatch();
            }
            statement.executeBatch();

            final var movieIds = TableGenerator.getIdsOfRowsInsertedWith(statement, movieCount);

            final var movieTranslation = new MovieTranslationTableGenerator(movieIds, seriesSeasonId != 0);
            movieTranslation.updateTableUsing(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private float randomRating() {
        return 5.0f + RANDOM.nextFloat() * (10.0f - 5.0f);
    }

}
