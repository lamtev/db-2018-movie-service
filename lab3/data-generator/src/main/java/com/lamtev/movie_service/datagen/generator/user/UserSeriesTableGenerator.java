package com.lamtev.movie_service.datagen.generator.user;

import com.lamtev.movie_service.datagen.generator.StorageDAO;
import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.postgresql.util.PGmoney;

import java.sql.Connection;
import java.sql.SQLException;

public final class UserSeriesTableGenerator implements TableGenerator {

    private final int percentageOfUsersWhoBoughtSeries;
    private final int minSeries;
    private final int maxSeries;

    public UserSeriesTableGenerator(int percentageOfUsersWhoBoughtSeries, int minSeries, int maxSeries) {
        this.percentageOfUsersWhoBoughtSeries = percentageOfUsersWhoBoughtSeries;
        this.minSeries = minSeries;
        this.maxSeries = maxSeries;
    }

    //TODO: get rid of duplicates
    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        final var userIds = StorageDAO.instance().ids(connection, "\"user\"");
        final var seriesIdsPrices = seriesIdsPrices(connection);
        if (userIds.length == 0 || seriesIdsPrices == null) {
            return;
        }
        try (final var statement = connection.prepareStatement(
                "INSERT INTO user_series (user_id, series_id, payment) VALUES (?, ?, ?)"
        )) {
            for (final var userId : userIds) {
                if (userId % 100 < percentageOfUsersWhoBoughtSeries) {
                    final var nSeries = RANDOM.nextInt(maxSeries - minSeries + 1) + minSeries;
                    final var seriesIdx = RANDOM.nextInt(seriesIdsPrices[0].length - nSeries);
                    for (int i = 0; i < nSeries; ++i) {
                        int j = 0;
                        statement.setLong(++j, userId);
                        statement.setInt(++j, seriesIdsPrices[0][seriesIdx + i]);
                        statement.setObject(++j, new PGmoney("$" + seriesIdsPrices[1][seriesIdx + i]));
                        statement.addBatch();
                    }
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private int[][] seriesIdsPrices(final @NotNull Connection connection) {
        try (final var statement = connection.createStatement()) {
            int count = StorageDAO.instance().count(connection, "series");
            int[][] seriesIdsPrices = new int[2][count];
            statement.executeQuery("SELECT id, price FROM series");
            final var result = statement.getResultSet();
            int i = 0;
            if (result != null) {
                while (result.next()) {
                    seriesIdsPrices[0][i] = result.getInt(1);
                    seriesIdsPrices[1][i] = result.getInt(2);
                    i++;
                }
            }

            return seriesIdsPrices;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
