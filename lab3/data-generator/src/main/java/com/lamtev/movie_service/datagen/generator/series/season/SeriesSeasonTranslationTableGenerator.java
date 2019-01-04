package com.lamtev.movie_service.datagen.generator.series.season;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public final class SeriesSeasonTranslationTableGenerator implements TableGenerator {

    @NotNull
    private final int[] seasonIds;

    public SeriesSeasonTranslationTableGenerator(final @NotNull int[] seasonIds) {
        this.seasonIds = seasonIds;
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        try (final var statement = connection.prepareStatement(
                "INSERT INTO series_season_translation (series_season_id, language_id, name, description) " +
                        " VALUES (?, ?, ?, ?)"
        )) {
            for (int languageId = 1; languageId <= 2; ++languageId) {
                for (final var seasonId : seasonIds) {
                    int i = 0;
                    statement.setInt(++i, seasonId);
                    statement.setInt(++i, languageId);
                    statement.setString(++i, FAKER.book().title());
                    statement.setString(++i, FAKER.lorem().paragraph(10));
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
