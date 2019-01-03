package com.lamtev.movie_service.datagen.generator;

import com.github.javafaker.Faker;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Random;

public interface TableGenerator {

    @NotNull
    Random RANDOM = new Random(System.currentTimeMillis());
    @NotNull
    Faker FAKER = new Faker(Locale.US, RANDOM);

    @NotNull
    static int[] getIdsOfRowsInsertedWith(final @NotNull Statement statement, int ofLength) {
        final var keys = new int[ofLength];
        int i = 0;
        try (final var generatedKeys = statement.getGeneratedKeys()) {
            while (generatedKeys.next()) {
                keys[i++] = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return keys;
    }

    void updateTableUsing(final @NotNull Connection connection);
}
