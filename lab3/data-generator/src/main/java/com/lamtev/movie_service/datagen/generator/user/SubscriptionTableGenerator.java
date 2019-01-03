package com.lamtev.movie_service.datagen.generator.user;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;
import org.postgresql.util.PGmoney;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

public class SubscriptionTableGenerator implements TableGenerator {

    private static final int[][] DURATION_PRICE = {
            {30, 5}, {60, 9}, {90, 13}, {180, 25}, {365, 40}, // only movies or only 1 series season
            {30, 7}, {60, 13}, {90, 25}, {180, 45}, {365, 75}, // 2 series seasons
            {30, 10}, {60, 18}, {90, 35}, {180, 65}, {365, 100}, // 3 series seasons
            {30, 12}, {60, 21}, {90, 40}, {180, 70}, {365, 120}, // 5 series seasons
    };

    private final long userCount;
    private final int minSubscriptionsPerUser;
    private final int maxSubscriptionsPerUser;

    public SubscriptionTableGenerator(long userCount, int minSubscriptionsPerUser, int maxSubscriptionsPerUser) {
        this.userCount = userCount;
        this.minSubscriptionsPerUser = minSubscriptionsPerUser;
        this.maxSubscriptionsPerUser = maxSubscriptionsPerUser;
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        final var userIds = UserTable.instance().ids(connection);
        try (final var statement = connection.prepareStatement(
                "INSERT INTO subscription (user_id, started, expires, autorenewable, payment) VALUES (?, ?, ?, ?, ?)"
        )) {
            RANDOM.ints(userCount, 0, userIds.length).forEach(idx -> {
                final var nSubscriptions = RANDOM.nextInt(maxSubscriptionsPerUser - minSubscriptionsPerUser + 1) + minSubscriptionsPerUser;
                for (int j = 0; j < nSubscriptions; ++j) {
                    int i = 0;
                    try {
                        statement.setLong(++i, userIds[idx]);
                        final var started = TableGenerator.randomDate(1);
                        statement.setObject(++i, started);
                        final var calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(started.getTime());
                        final var durationPrice = DURATION_PRICE[RANDOM.nextInt(DURATION_PRICE.length)];
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
