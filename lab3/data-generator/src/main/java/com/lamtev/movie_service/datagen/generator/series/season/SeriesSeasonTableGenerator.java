package com.lamtev.movie_service.datagen.generator.series.season;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import com.lamtev.movie_service.datagen.generator.movie.MovieTableGenerator;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SeriesSeasonTableGenerator implements TableGenerator {

    @NotNull
    private final int[] seriesIds;
    @NotNull
    private final int[] seriesPrices;
    private final short seasonsCount;
    private final int episodesCount;

    public SeriesSeasonTableGenerator(final @NotNull int[] seriesIds,
                                      final @NotNull int[] seriesPrices, short seasonsCount, int episodesCount) {
        this.seriesIds = seriesIds;
        this.seriesPrices = seriesPrices;
        this.seasonsCount = seasonsCount;
        this.episodesCount = episodesCount;
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        try (final var statement = connection.prepareStatement(
                "INSERT INTO series_season (series_id, number) VALUES (?, ?)",
                RETURN_GENERATED_KEYS
        )) {
            for (final int id : seriesIds) {
                for (short season = 0; season < seasonsCount; ++season) {
                    int i = 0;
                    statement.setInt(++i, id);
                    statement.setShort(++i, season);
                    statement.addBatch();
                }
            }
            statement.executeBatch();

            final var seasonIds = TableGenerator.getIdsOfRowsInsertedWith(statement, seriesIds.length * seasonsCount);

            final var seasonTranslation = new SeriesSeasonTranslationTableGenerator(seasonIds);
            seasonTranslation.updateTableUsing(connection);

            for (int seasonIdx = 0; seasonIdx < seasonIds.length; ++seasonIdx) {
                final var episode = new MovieTableGenerator(episodesCount, seasonIds[seasonIdx], seriesPrices[seasonIdx / seasonsCount]);
                episode.updateTableUsing(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
