package com.lamtev.movie_service.datagen.generator.subscription;


import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class SubscriptionTable {

    private static final int[][] DURATION_PRICE = {
            {30, 5}, {60, 9}, {90, 13}, {180, 25}, {365, 40}, // only 5 movies or only 1 series season
            {30, 7}, {60, 13}, {90, 25}, {180, 45}, {365, 75}, // 10 movies or 2 series seasons
            {30, 10}, {60, 18}, {90, 35}, {180, 65}, {365, 100}, // 15 movies or 3 series seasons
            {30, 12}, {60, 21}, {90, 40}, {180, 70}, {365, 120}, // 30 movies 5 or series seasons
    };

    private SubscriptionTable() {

    }

    public static SubscriptionTable instance() {
        return Holder.INSTANCE;
    }

    @NotNull
    public long[][] idsNMoviesOrMSeasons(final @NotNull Connection connection) {
        try (final var statement = connection.createStatement()) {
            statement.executeQuery("SELECT COUNT(*) FROM subscription");
            var result = statement.getResultSet();
            int count = 1;
            if (result != null && result.next()) {
                count = result.getInt(1);
            }
            final var idsNMoviesOrMSeasons = new long[3][count];
            statement.executeQuery("SELECT id, (expires - started), payment FROM subscription GROUP BY id");
            result = statement.getResultSet();
            int i = 0;
            if (result != null) {
                while (result.next()) {
                    idsNMoviesOrMSeasons[0][i] = result.getLong(1);
                    final var nm = nMoviesMSeasons(result.getInt(2), result.getInt(3));
                    idsNMoviesOrMSeasons[1][i] = nm[0];
                    idsNMoviesOrMSeasons[2][i] = nm[1];
                    i++;
                }
            }
            return idsNMoviesOrMSeasons;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new long[0][0];
    }

    private int[] nMoviesMSeasons(int duration, int payment) {
        int n = 0;
        int m = 0;
        for (int i = 0; i < DURATION_PRICE.length; ++i) {
            for (int j = 0; j < 2; ++j) {
                if (DURATION_PRICE[i][0] == duration && DURATION_PRICE[i][1] == payment) {
                    if (i < 5) {
                        n = 5;
                        m = 1;
                    } else if (i < 10) {
                        n = 10;
                        m = 2;
                    } else if (i < 15) {
                        n = 15;
                        m = 3;
                    } else if (i < 20){
                        n = 30;
                        m = 5;
                    }
                }
            }
        }

        return new int[]{n, m};
    }

    private static final class Holder {
        private static final SubscriptionTable INSTANCE = new SubscriptionTable();
    }

}
