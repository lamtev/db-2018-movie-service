package com.lamtev.movie_service.datagen.generator;

import com.github.javafaker.Faker;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.Locale;
import java.util.Random;

public interface TableGenerator {
    @NotNull
    Random RANDOM = new Random(System.currentTimeMillis());
    @NotNull
    Faker FAKER = new Faker(Locale.US, RANDOM);
    @NotNull
    Utils UTILS = new Utils(RANDOM, FAKER);

    /**
     * Updates corresponding table via {@code connection} with newly generated data.
     *
     * @param connection {@link Connection} (session) with data base.
     */
    void updateTableUsing(final @NotNull Connection connection);
}
