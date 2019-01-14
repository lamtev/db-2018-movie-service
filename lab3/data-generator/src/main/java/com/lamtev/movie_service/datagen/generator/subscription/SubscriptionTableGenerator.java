package com.lamtev.movie_service.datagen.generator.subscription;

import com.lamtev.movie_service.datagen.generator.StorageDAO;
import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;
import org.postgresql.util.PGmoney;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

public final class SubscriptionTableGenerator implements TableGenerator {

    private final long usersCount;
    private final int minSubscriptionsPerUser;
    private final int maxSubscriptionsPerUser;
    @NotNull
    private final int[][] durationPriceNMoviesMSeasons;
    private final int yearsSinceFirstSubscription;

    public SubscriptionTableGenerator(long usersCount, int minSubscriptionsPerUser,
                                      int maxSubscriptionsPerUser, final @NotNull int[][] durationPriceNMoviesMSeasons,
                                      int yearsSinceFirstSubscription) {
        this.usersCount = usersCount;
        this.minSubscriptionsPerUser = minSubscriptionsPerUser;
        this.maxSubscriptionsPerUser = maxSubscriptionsPerUser;
        this.durationPriceNMoviesMSeasons = durationPriceNMoviesMSeasons;
        this.yearsSinceFirstSubscription = yearsSinceFirstSubscription;
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        final var userIds = StorageDAO.instance().ids(connection, "\"user\"");
        try (final var statement = connection.prepareStatement(
                "INSERT INTO subscription (user_id, started, expires, autorenewable, payment) VALUES (?, ?, ?, ?, ?)"
        )) {
            RANDOM.ints(usersCount, 0, userIds.length).forEach(idx -> {
                final var nSubscriptions = RANDOM.nextInt(maxSubscriptionsPerUser - minSubscriptionsPerUser + 1) + minSubscriptionsPerUser;
                for (int j = 0; j < nSubscriptions; ++j) {
                    int i = 0;
                    try {
                        statement.setLong(++i, userIds[idx]);
                        final var started = UTILS.randomDate(yearsSinceFirstSubscription);
                        statement.setObject(++i, started);
                        final var calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(started.getTime());
                        final var durationPrice = durationPriceNMoviesMSeasons[RANDOM.nextInt(durationPriceNMoviesMSeasons.length)];
                        calendar.add(Calendar.DATE, durationPrice[0]);
                        statement.setObject(++i, new Date(calendar.getTimeInMillis()));
                        statement.setBoolean(++i, RANDOM.nextBoolean());
                        statement.setObject(++i, new PGmoney("$" + durationPrice[1]));
                        statement.addBatch();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
