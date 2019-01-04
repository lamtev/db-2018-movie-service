package com.lamtev.movie_service.datagen.generator.subscription;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public final class SubscriptionMovieTableGenerator implements TableGenerator {

    @NotNull
    private final int[][] subscriptionIdsNMovies;
    @NotNull
    private final int[] movieIds;

    public SubscriptionMovieTableGenerator(final @NotNull int[][] subscriptionIdsNMovies, final @NotNull int[] movieIds) {
        this.subscriptionIdsNMovies = subscriptionIdsNMovies;
        this.movieIds = movieIds;
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        try (final var statement = connection.prepareStatement(
                "INSERT INTO subscription_movie (subscription_id, movie_id) VALUES (?, ?)"
        )) {
            for (int j = 0; j < subscriptionIdsNMovies[0].length; ++j) {
                final var nMovies = (int) subscriptionIdsNMovies[1][j];
                final var movieIdsIdxs = UTILS.nUniqueRandomInts(nMovies, movieIds.length);
                for (final var movieIdsIdx : movieIdsIdxs) {
                    int i = 0;
                    statement.setLong(++i, subscriptionIdsNMovies[0][j]);
                    statement.setInt(++i, movieIds[movieIdsIdx]);
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
