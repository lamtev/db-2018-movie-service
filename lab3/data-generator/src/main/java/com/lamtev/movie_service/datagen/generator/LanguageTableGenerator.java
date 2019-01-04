package com.lamtev.movie_service.datagen.generator;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public final class LanguageTableGenerator implements TableGenerator {

    @NotNull
    private final String[] languages;

    public LanguageTableGenerator(@NotNull String[] languages) {
        this.languages = languages;
    }

    public LanguageTableGenerator() {
        this(new String[]{"en-US", "ru-RU"});
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        try (final var statement = connection.prepareStatement(
                "INSERT INTO language (name) VALUES (?)"
        )) {
            for (final var language : languages) {
                statement.setString(1, language);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
