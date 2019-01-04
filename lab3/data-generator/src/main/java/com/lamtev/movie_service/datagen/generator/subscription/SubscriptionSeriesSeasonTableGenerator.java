package com.lamtev.movie_service.datagen.generator.subscription;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public final class SubscriptionSeriesSeasonTableGenerator implements TableGenerator {

    @NotNull
    private final int[][] subscriptionIdsMSeasons;
    @NotNull
    private final int[] seriesSeasonIds;

    public SubscriptionSeriesSeasonTableGenerator(final @NotNull int[][] subscriptionIdsMSeasons, final @NotNull int[] seriesSeasonIds) {
        this.subscriptionIdsMSeasons = subscriptionIdsMSeasons;
        this.seriesSeasonIds = seriesSeasonIds;
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        try (final var statement = connection.prepareStatement(
                "INSERT INTO subscription_series_season (subscription_id, series_season_id) VALUES (?, ?)"
        )) {
            for (int j = 0; j < subscriptionIdsMSeasons[0].length; ++j) {
                final var nSeriesSeasons = (int) subscriptionIdsMSeasons[1][j];
                final var seriesIdsIdxs = UTILS.nUniqueRandomInts(nSeriesSeasons, seriesSeasonIds.length);
                for (final var seriesIdsIdx : seriesIdsIdxs) {
                    int i = 0;
                    statement.setLong(++i, subscriptionIdsMSeasons[0][j]);
                    statement.setInt(++i, seriesSeasonIds[seriesIdsIdx]);
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
