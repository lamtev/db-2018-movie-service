package com.lamtev.movie_service.datagen.generator.movie;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;
import org.postgresql.util.PGmoney;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

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
                RETURN_GENERATED_KEYS
        )) {
            final var moviesAreSeriesSeasonEpisodes = seriesSeasonId != 0;
            final var date = TableGenerator.randomDate(50);
            final var rating = TableGenerator.randomRating();
            for (int i = 0; i < movieCount; ++i) {
                int j = 0;
                statement.setObject(++j, new PGmoney("$" + (
                        seriesPrice == 0 ?
                                MOVIE_PRICES_IN_USD[RANDOM.nextInt(MOVIE_PRICES_IN_USD.length)]
                                : seriesPrice
                )));
                if (moviesAreSeriesSeasonEpisodes) {
                    statement.setDate(++j, date);
                    statement.setFloat(++j, rating);
                    statement.setInt(++j, seriesSeasonId);
                } else {
                    statement.setDate(++j, TableGenerator.randomDate(50));
                    statement.setFloat(++j, TableGenerator.randomRating());
                    statement.setNull(++j, Types.INTEGER);
                }
                statement.addBatch();
            }
            statement.executeBatch();

            final var movieIds = TableGenerator.getIdsOfRowsInsertedWith(statement, movieCount);

            final var movieTranslation = new MovieTranslationTableGenerator(movieIds, moviesAreSeriesSeasonEpisodes);
            movieTranslation.updateTableUsing(connection);

            updateMovieCategoryTableUsing(connection, moviesAreSeriesSeasonEpisodes, movieIds);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateMovieCategoryTableUsing(@NotNull Connection connection, boolean moviesAreSeriesSeasonEpisodes, int[] movieIds) {
        try (final var categoriesStatement = connection.createStatement()) {
            categoriesStatement.executeQuery("SELECT COUNT(*) FROM category");
            var result = categoriesStatement.getResultSet();
            if (result != null && result.next()) {
                int categoriesCount = result.getInt(1);
                final var categoryIds = new int[categoriesCount - 1];
                int i = 0;
                categoriesStatement.executeQuery("SELECT id FROM category WHERE name != 'genre'");
                result = categoriesStatement.getResultSet();
                if (result != null) {
                    while (result.next()) {
                        categoryIds[i++] = result.getShort(1);
                    }
                }

                final var movieCategory = new MovieCategoryTableGenerator(movieIds, categoryIds, moviesAreSeriesSeasonEpisodes);
                movieCategory.updateTableUsing(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
