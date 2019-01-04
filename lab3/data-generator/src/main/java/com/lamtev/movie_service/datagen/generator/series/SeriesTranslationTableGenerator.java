package com.lamtev.movie_service.datagen.generator.series;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public final class SeriesTranslationTableGenerator implements TableGenerator {

    @NotNull
    private final int[] seriesIds;

    public SeriesTranslationTableGenerator(final @NotNull int[] seriesIds) {
        this.seriesIds = seriesIds;
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        try (final var statement = connection.prepareStatement(
                "INSERT INTO series_translation (series_id, language_id, name, director, description) VALUES (?, ?, ?, ? , ?)"
        )) {
            for (final var seriesId : seriesIds) {
                for (int languageId = 1; languageId <= 2; ++languageId) {
                    int i = 0;
                    statement.setInt(++i, seriesId);
                    statement.setInt(++i, languageId);
                    final var series = FAKER.book();
                    statement.setString(++i, series.title());
                    statement.setString(++i, series.author());
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
