package com.lamtev.movie_service.datagen.generator.movie;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class MovieTable {

    private MovieTable() {

    }

    public static MovieTable instance() {
        return Holder.INSTANCE;
    }

    @NotNull
    public int[] ids(final @NotNull Connection connection) {
        try (final var statement = connection.createStatement()) {
            statement.executeQuery("SELECT COUNT(*) FROM movie");
            var result = statement.getResultSet();
            int count = 1;
            if (result != null && result.next()) {
                count = result.getInt(1);
            }
            final var ids = new int[count];
            statement.executeQuery("SELECT id FROM movie");
            result = statement.getResultSet();
            int i = 0;
            if (result != null) {
                while (result.next()) {
                    ids[i++] = result.getInt(1);
                }
            }
            return ids;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new int[0];
    }

    private static final class Holder {
        private static final MovieTable INSTANCE = new MovieTable();
    }

}
