package com.lamtev.movie_service.datagen.generator.user;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public final class UserTable {

    private UserTable() {

    }

    public static UserTable instance() {
        return Holder.INSTANCE;
    }

    @NotNull
    public long[] ids(final @NotNull Connection connection) {
        try (final var statement = connection.createStatement()) {
            statement.executeQuery("SELECT COUNT(*) FROM \"user\"");
            var result = statement.getResultSet();
            int count = 1;
            if (result != null && result.next()) {
                count = result.getInt(1);
            }
            final var ids = new long[count];
            statement.executeQuery("SELECT id FROM \"user\"");
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
        return new long[0];
    }

    private static final class Holder {
        private static final UserTable INSTANCE = new UserTable();
    }

}
