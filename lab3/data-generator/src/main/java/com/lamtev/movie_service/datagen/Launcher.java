package com.lamtev.movie_service.datagen;

import com.lamtev.movie_service.datagen.cli_args.ArgumentsParser;
import com.lamtev.movie_service.datagen.generator.LanguageTableGenerator;
import com.lamtev.movie_service.datagen.generator.StorageDAO;
import com.lamtev.movie_service.datagen.generator.Utils;
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
            if (parameters.isGenAll()) {
                final var language = new LanguageTableGenerator();
                language.updateTableUsing(connection);
                System.out.println("language generated");

                final var category = new CategoryTableGenerator();
                category.updateTableUsing(connection);
                System.out.println("category generated");
            }

            if (parameters.isGenAll() || parameters.isGenMoviesOnly()) {
                final var movie = new MovieTableGenerator(parameters.moviesCount());
                movie.updateTableUsing(connection);
                System.out.println("movie generated");
            }

            if (parameters.isGenAll() || parameters.isGenSeriesOnly()) {
                final var series = new SeriesTableGenerator(parameters.seriesCountSeasonsEpisodes());
                series.updateTableUsing(connection);
                System.out.println("series generated");
            }

            if (parameters.isGenAll() || parameters.isGenUsersOnly()) {
                final var user = new UserTableGenerator(parameters.usersCount(), parameters.femalePercentage());
                user.updateTableUsing(connection);
                System.out.println("user generated");
            }

            if (parameters.isGenAll()) {
                final var userMovie = new UserMovieTableGenerator(parameters.percentageOfUsersWhoBoughtMovies(),
                        parameters.minMoviesPerUser(), parameters.maxMoviesPerUser());
                userMovie.updateTableUsing(connection);
                System.out.println("user_movie generated");

                final var seriesMovie = new UserSeriesTableGenerator(parameters.percentageOfUsersWhoBoughtSeries(),
                        parameters.minSeriesPerUser(), parameters.maxSeriesPerUser());
                seriesMovie.updateTableUsing(connection);
                System.out.println("user_series generated");
            }

            if (parameters.isGenAll() || parameters.isGenSubscriptionsToMoviesOnly()
                    || parameters.isGenSubscriptionsToSeriesOnly()) {
                final var subscription = new SubscriptionTableGenerator(parameters.usersCount(), parameters.minSubscriptionsPerUser(),
                        parameters.maxSubscriptionsPerUser(), parameters.durationPriceNMoviesMSeasons(), parameters.yearsSinceFirstSubscription());
                subscription.updateTableUsing(connection);
                System.out.println("subscription generated");

                final var subscriptionIdsNMoviesMSeasons = SubscriptionTableDAO.instance().idsNMoviesOrMSeasonsContainingInIds(
                        connection, parameters.durationPriceNMoviesMSeasons(), subscription.getGeneratedIds());
                final var subscriptionIdsNMovies = new int[2][0];
                final var subscriptionIdsMSeasons = new int[2][0];
                Utils.split(subscriptionIdsNMoviesMSeasons, parameters.moviesSubscriptionsPercentage(), subscriptionIdsNMovies, subscriptionIdsMSeasons);

                if (parameters.isGenAll() || parameters.isGenSubscriptionsToMoviesOnly()) {
                    final var movieIds = StorageDAO.instance().ids(connection, "movie");
                    final var subscriptionMovie = new SubscriptionMovieTableGenerator(subscriptionIdsNMovies, movieIds);
                    subscriptionMovie.updateTableUsing(connection);
                    System.out.println("subscription_movie generated");
                }

                if (parameters.isGenAll() || parameters.isGenSubscriptionsToSeriesOnly()) {
                    final var seriesSeasonIds = StorageDAO.instance().ids(connection, "series_season");
                    final var subscriptionSeriesSeason = new SubscriptionSeriesSeasonTableGenerator(subscriptionIdsMSeasons, seriesSeasonIds);
                    subscriptionSeriesSeason.updateTableUsing(connection);
                    System.out.println("subscription_series_season generated");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
