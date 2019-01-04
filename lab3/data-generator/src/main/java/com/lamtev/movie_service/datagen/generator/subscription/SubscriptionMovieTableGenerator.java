package com.lamtev.movie_service.datagen.generator.subscription;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SubscriptionMovieTableGenerator implements TableGenerator {

    @NotNull
    private final long[][] subscriptionIdsNMoviesMSeasons;
    @NotNull
    private final int[] movieIds;

    public SubscriptionMovieTableGenerator(final @NotNull long[][] subscriptionIdsNMoviesMSeasons, final @NotNull int[] movieIds) {
        this.subscriptionIdsNMoviesMSeasons = subscriptionIdsNMoviesMSeasons;
        this.movieIds = movieIds;
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        try (final var statement = connection.prepareStatement(
                "INSERT INTO subscription_movie (subscription_id, movie_id) VALUES (?, ?)"
        )) {
            for (int j = 0; j < subscriptionIdsNMoviesMSeasons[0].length; ++j) {
                final var nMovies = (int) subscriptionIdsNMoviesMSeasons[1][j];
                final var movieIdsIdxs = nUniqueRandomInts(nMovies, movieIds.length);
                for (final var movieIdIdx : movieIdsIdxs) {
                    int i = 0;
                    statement.setLong(++i, subscriptionIdsNMoviesMSeasons[0][j]);
                    statement.setInt(++i, movieIds[movieIdIdx]);
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   private Set<Integer> nUniqueRandomInts(int n, int bound) {
       final var ints = new HashSet<Integer>(n);
       while (ints.size() != n) {
           ints.add(RANDOM.nextInt(bound));
       }

       return ints;
   }

}
