package com.lamtev.movie_service.datagen;

import com.lamtev.movie_service.datagen.cli_args.ArgumentsParser;
import com.lamtev.movie_service.datagen.generator.LanguageTableGenerator;
import com.lamtev.movie_service.datagen.generator.category.CategoryTableGenerator;
import com.lamtev.movie_service.datagen.generator.movie.MovieTable;
import com.lamtev.movie_service.datagen.generator.movie.MovieTableGenerator;
import com.lamtev.movie_service.datagen.generator.series.SeriesTableGenerator;
import com.lamtev.movie_service.datagen.generator.series.season.SeriesSeasonTable;
import com.lamtev.movie_service.datagen.generator.subscription.SubscriptionMovieTableGenerator;
import com.lamtev.movie_service.datagen.generator.subscription.SubscriptionSeriesSeasonTableGenerator;
import com.lamtev.movie_service.datagen.generator.subscription.SubscriptionTable;
import com.lamtev.movie_service.datagen.generator.subscription.SubscriptionTableGenerator;
import com.lamtev.movie_service.datagen.generator.user.UserMovieTableGenerator;
import com.lamtev.movie_service.datagen.generator.user.UserSeriesTableGenerator;
import com.lamtev.movie_service.datagen.generator.user.UserTableGenerator;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Launcher {

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        final var generatorParams = new ArgumentsParser().parseArguments(args);
        final var endpoint = generatorParams.getEndpoint();
        try (final var connection = DriverManager.getConnection(endpoint.url(), endpoint.user(), endpoint.password())) {
            final var language = new LanguageTableGenerator();
            language.updateTableUsing(connection);

            final var category = new CategoryTableGenerator();
            category.updateTableUsing(connection);

            final var movie = new MovieTableGenerator(1_000);
            movie.updateTableUsing(connection);

            final var series = new SeriesTableGenerator(new int[][]{{100, 3, 15}, {200, 2, 25}});
            series.updateTableUsing(connection);

            final var user = new UserTableGenerator(10_000L, (byte) 53);
            user.updateTableUsing(connection);

            final var userMovie = new UserMovieTableGenerator((byte) 64, 5, 10);
            userMovie.updateTableUsing(connection);

            final var seriesMovie = new UserSeriesTableGenerator((byte) 35, 2, 5);
            seriesMovie.updateTableUsing(connection);

            final var subscription = new SubscriptionTableGenerator(10_000L, 3, 8);
            subscription.updateTableUsing(connection);

            final var subscriptionIdsNMoviesMSeasons = SubscriptionTable.instance().idsNMoviesOrMSeasons(connection);
            final var subscriptionIdsNMovies = new long[2][0];
            final var subscriptionIdsMSeasons = new long[2][0];
            split(subscriptionIdsNMoviesMSeasons, 35, subscriptionIdsNMovies, subscriptionIdsMSeasons);

            final var movieIds = MovieTable.instance().ids(connection);
            final var subscriptionMovie = new SubscriptionMovieTableGenerator(subscriptionIdsNMovies, movieIds);
            subscriptionMovie.updateTableUsing(connection);

            final var seriesSeasonIds = SeriesSeasonTable.instance().ids(connection);
            final var subscriptionSeriesSeason = new SubscriptionSeriesSeasonTableGenerator(subscriptionIdsMSeasons, seriesSeasonIds);
            subscriptionSeriesSeason.updateTableUsing(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void split(long[][] subscriptionIdsNMoviesMSeasons, int moviesPercentage, long[][] subscriptionIdsNMovies, long[][] subscriptionIdsMSeasons) {
        int moviesIdx = 0;
        int seasonsIdx = 0;
        int moviesLength = (int) Math.ceil((double) subscriptionIdsNMoviesMSeasons[0].length / 100) * moviesPercentage;
        int seasonsLength = subscriptionIdsNMoviesMSeasons[0].length - moviesLength;
        for (int i = 0; i < 2; ++i) {
            subscriptionIdsNMovies[i] = new long[moviesLength];
            subscriptionIdsMSeasons[i] = new long[seasonsLength];
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

}
