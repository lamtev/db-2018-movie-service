package com.lamtev.movie_service.datagen.generator;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public final class StorageDAO {

    private StorageDAO() {
    }

    public static StorageDAO instance() {
        return Holder.INSTANCE;
    }

    public final int count(final @NotNull Connection connection, final @NotNull String tableName) {
        int count = 0;
        try (final var statement = connection.createStatement()) {
            statement.executeQuery("SELECT COUNT(*) FROM " + tableName);
            var result = statement.getResultSet();
            if (result != null && result.next()) {
                count = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @NotNull
    public final int[] ids(final @NotNull Connection connection, final @NotNull String tableName) {
        try (final var statement = connection.createStatement()) {
            final int count = count(connection, tableName);

            final var ids = new int[count];
            statement.executeQuery("SELECT id FROM " + tableName);

            final var result = statement.getResultSet();
            if (result != null) {
                int i = 0;
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
        private static final StorageDAO INSTANCE = new StorageDAO();
    }

}
