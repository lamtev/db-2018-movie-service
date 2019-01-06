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

    public static void split(final @NotNull int[][] subscriptionIdsNMoviesMSeasons, int moviesPercentage, final @NotNull int[][] subscriptionIdsNMovies, final @NotNull int[][] subscriptionIdsMSeasons) {
        int moviesIdx = 0;
        int seasonsIdx = 0;
        int moviesLength = (int) Math.ceil((double) subscriptionIdsNMoviesMSeasons[0].length / 100) * moviesPercentage;
        int seasonsLength = subscriptionIdsNMoviesMSeasons[0].length - moviesLength;
        for (int i = 0; i < 2; ++i) {
            subscriptionIdsNMovies[i] = new int[moviesLength];
            subscriptionIdsMSeasons[i] = new int[seasonsLength];
        }
        for (int i = 0; i < subscriptionIdsNMoviesMSeasons[0].length; ++i) {
            if (i % 100 < moviesPercentage) {
                subscriptionIdsNMovies[0][moviesIdx] = subscriptionIdsNMoviesMSeasons[0][i];
                subscriptionIdsNMovies[1][moviesIdx] = subscriptionIdsNMoviesMSeasons[1][i];
                moviesIdx++;
            } else {
                subscriptionIdsMSeasons[0][seasonsIdx] = subscriptionIdsNMoviesMSeasons[0][i];
                subscriptionIdsMSeasons[1][seasonsIdx] = subscriptionIdsNMoviesMSeasons[2][i];
                seasonsIdx++;
            }
        }
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
