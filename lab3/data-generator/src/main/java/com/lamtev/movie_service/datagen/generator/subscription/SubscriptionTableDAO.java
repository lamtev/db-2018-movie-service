package com.lamtev.movie_service.datagen.generator.subscription;


import com.lamtev.movie_service.datagen.generator.StorageDAO;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public final class SubscriptionTableDAO {

    private SubscriptionTableDAO() {
    }

    public static SubscriptionTableDAO instance() {
        return Holder.INSTANCE;
    }

    @NotNull
    public int[][] idsNMoviesOrMSeasons(final @NotNull Connection connection, final @NotNull int[][] durationPriceNMoviesMSeasons) {
        try (final var statement = connection.createStatement()) {
            int count = StorageDAO.instance().count(connection, "subscription");
            final var idsNMoviesOrMSeasons = new int[3][count];
            statement.executeQuery("SELECT id, (expires - started), payment FROM subscription GROUP BY id");
            final var result = statement.getResultSet();
            int i = 0;
            if (result != null) {
                while (result.next()) {
                    idsNMoviesOrMSeasons[0][i] = result.getInt(1);
                    final var nm = nMoviesMSeasons(result.getInt(2), result.getInt(3), durationPriceNMoviesMSeasons);
                    idsNMoviesOrMSeasons[1][i] = nm[0];
                    idsNMoviesOrMSeasons[2][i] = nm[1];
                    i++;
                }
            }
            return idsNMoviesOrMSeasons;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new int[0][0];
    }

    @NotNull
    private int[] nMoviesMSeasons(int duration, int payment, final @NotNull int[][] durationPriceNMoviesMSeasons) {
        int n = 0;
        int m = 0;
        for (int[] durationPriceNMoviesMSeason : durationPriceNMoviesMSeasons) {
            if (durationPriceNMoviesMSeason[0] == duration && durationPriceNMoviesMSeason[1] == payment) {
                n = durationPriceNMoviesMSeason[2];
                m = durationPriceNMoviesMSeason[3];
                break;
            }
        }

        return new int[]{n, m};
    }

    private static final class Holder {
        private static final SubscriptionTableDAO INSTANCE = new SubscriptionTableDAO();
    }

}
