package com.lamtev.movie_service.datagen;

import com.lamtev.movie_service.datagen.cli_args.ArgumentsParser;
import com.lamtev.movie_service.datagen.generator.LanguageTableGenerator;
import com.lamtev.movie_service.datagen.generator.StorageDAO;
import com.lamtev.movie_service.datagen.generator.category.CategoryTableGenerator;
import com.lamtev.movie_service.datagen.generator.movie.MovieTableGenerator;
import com.lamtev.movie_service.datagen.generator.series.SeriesTableGenerator;
import com.lamtev.movie_service.datagen.generator.subscription.SubscriptionMovieTableGenerator;
import com.lamtev.movie_service.datagen.generator.subscription.SubscriptionSeriesSeasonTableGenerator;
import com.lamtev.movie_service.datagen.generator.subscription.SubscriptionTableDAO;
import com.lamtev.movie_service.datagen.generator.subscription.SubscriptionTableGenerator;
import com.lamtev.movie_service.datagen.generator.user.UserMovieTableGenerator;
import com.lamtev.movie_service.datagen.generator.user.UserSeriesTableGenerator;
import com.lamtev.movie_service.datagen.generator.user.UserTableGenerator;

import java.sql.DriverManager;
import java.sql.SQLException;

final class Launcher {

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        final var argumentsParser = new ArgumentsParser(args);
        final var endpoint = argumentsParser.endpoint();
        final var parameters = argumentsParser.parameters();
        if (endpoint == null || parameters == null) {
            System.err.println("Wrong arguments!");
            return;
        }
        try (final var connection = DriverManager.getConnection(endpoint.url(), endpoint.user(), endpoint.password())) {
            final var language = new LanguageTableGenerator();
            language.updateTableUsing(connection);

            final var category = new CategoryTableGenerator();
            category.updateTableUsing(connection);

            final var movie = new MovieTableGenerator(parameters.moviesCount());
            movie.updateTableUsing(connection);

            final var series = new SeriesTableGenerator(parameters.seriesCountSeasonsEpisodes());
            series.updateTableUsing(connection);

            final var user = new UserTableGenerator(parameters.usersCount(), parameters.femalePercentage());
            user.updateTableUsing(connection);

            final var userMovie = new UserMovieTableGenerator(parameters.percentageOfUsersWhoBoughtMovies(), parameters.minMoviesPerUser(), parameters.maxMoviesPerUser());
            userMovie.updateTableUsing(connection);

            final var seriesMovie = new UserSeriesTableGenerator(parameters.percentageOfUsersWhoBoughtSeries(), parameters.minSeriesPerUser(), parameters.maxSeriesPerUser());
            seriesMovie.updateTableUsing(connection);

            final var subscription = new SubscriptionTableGenerator(parameters.usersCount(), parameters.minSubscriptionsPerUser(), parameters.maxSubscriptionsPerUser(), parameters.durationPriceNMoviesMSeasons());
            subscription.updateTableUsing(connection);

            final var subscriptionIdsNMoviesMSeasons = SubscriptionTableDAO.instance().idsNMoviesOrMSeasons(connection, parameters.durationPriceNMoviesMSeasons());
            final var subscriptionIdsNMovies = new int[2][0];
            final var subscriptionIdsMSeasons = new int[2][0];
            split(subscriptionIdsNMoviesMSeasons, parameters.moviesSubscriptionsPercentage(), subscriptionIdsNMovies, subscriptionIdsMSeasons);

            final var movieIds = StorageDAO.instance().ids(connection, "movie");
            final var subscriptionMovie = new SubscriptionMovieTableGenerator(subscriptionIdsNMovies, movieIds);
            subscriptionMovie.updateTableUsing(connection);

            final var seriesSeasonIds = StorageDAO.instance().ids(connection, "series_season");
            final var subscriptionSeriesSeason = new SubscriptionSeriesSeasonTableGenerator(subscriptionIdsMSeasons, seriesSeasonIds);
            subscriptionSeriesSeason.updateTableUsing(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void split(int[][] subscriptionIdsNMoviesMSeasons, int moviesPercentage, int[][] subscriptionIdsNMovies, int[][] subscriptionIdsMSeasons) {
        int moviesIdx = 0;
        int seasonsIdx = 0;
        int moviesLength = (int) Math.ceil((double) subscriptionIdsNMoviesMSeasons[0].length / 100) * moviesPercentage;
        int seasonsLength = subscriptionIdsNMoviesMSeasons[0].length - moviesLength;
        for (int i = 0; i < 2; ++i) {
            subscriptionIdsNMovies[i] = new int[moviesLength];
            subscriptionIdsMSeasons[i] = new int[seasonsLength];
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
