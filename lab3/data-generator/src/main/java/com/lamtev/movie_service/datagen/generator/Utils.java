package com.lamtev.movie_service.datagen.generator;

import com.github.javafaker.Faker;
import gnu.trove.set.hash.TIntHashSet;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public final class Utils {

    @NotNull
    private final Random random;
    @NotNull
    private final Faker faker;


    public Utils(@NotNull Random random, @NotNull Faker faker) {
        this.random = random;
        this.faker = faker;
    }

    @NotNull
    public int[] getIdsOfRowsInsertedWith(final @NotNull Statement statement, int ofLength) {
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

    @NotNull
    public Date randomDate(int maxYearsAgo) {
        return new Date(faker.date().past(365 * maxYearsAgo, TimeUnit.DAYS).getTime());
    }

    public float randomRating() {
        return 5.0f + random.nextFloat() * (10.0f - 5.0f);
    }

    @NotNull
    public int[] nUniqueRandomInts(int n, int bound) {
        final var ints = new TIntHashSet(n);
        while (ints.size() != n) {
            ints.add(random.nextInt(bound));
        }

        return ints.toArray();
    }

}
