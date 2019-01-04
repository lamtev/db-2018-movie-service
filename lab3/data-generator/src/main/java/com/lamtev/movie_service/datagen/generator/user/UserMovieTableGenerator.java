package com.lamtev.movie_service.datagen.generator.user;

import com.lamtev.movie_service.datagen.generator.StorageDAO;
import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.postgresql.util.PGmoney;

import java.sql.Connection;
import java.sql.SQLException;

public final class UserMovieTableGenerator implements TableGenerator {

    private final int percentageOfUsersWhoBoughtMovies;
    private final int minMovies;
    private final int maxMovies;

    public UserMovieTableGenerator(int percentageOfUsersWhoBoughtMovies, int minMovies, int maxMovies) {
        this.percentageOfUsersWhoBoughtMovies = percentageOfUsersWhoBoughtMovies;
        this.minMovies = minMovies;
        this.maxMovies = maxMovies;
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        final var userIds = StorageDAO.instance().ids(connection, "\"user\"");
        final var movieIdsPrices = movieIdsPrices(connection);
        if (userIds.length == 0 || movieIdsPrices == null) {
            return;
        }

        try (final var statement = connection.prepareStatement(
                "INSERT INTO user_movie (user_id, movie_id, payment) VALUES (?, ?, ?)"
        )) {
            for (final var userId : userIds) {
                if (userId % 100 < percentageOfUsersWhoBoughtMovies) {
                    final var nMovies = RANDOM.nextInt(maxMovies - minMovies + 1) + minMovies;
                    final var movieIdx = RANDOM.nextInt(movieIdsPrices[0].length - nMovies);
                    for (int i = 0; i < nMovies; ++i) {
                        int j = 0;
                        statement.setLong(++j, userId);
                        statement.setInt(++j, movieIdsPrices[0][movieIdx + i]);
                        statement.setObject(++j, new PGmoney("$" + movieIdsPrices[1][movieIdx + i]));
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
    private int[][] movieIdsPrices(final @NotNull Connection connection) {
        try (final var statement = connection.createStatement()) {
            int count = StorageDAO.instance().count(connection, "movie");
            int[][] movieIdsPrices = new int[2][count];
            statement.executeQuery("SELECT id, price FROM movie");
            final var result = statement.getResultSet();
            int i = 0;
            if (result != null) {
                while (result.next()) {
                    movieIdsPrices[0][i] = result.getInt(1);
                    movieIdsPrices[1][i] = result.getInt(2);
                    i++;
                }
            }

            return movieIdsPrices;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
