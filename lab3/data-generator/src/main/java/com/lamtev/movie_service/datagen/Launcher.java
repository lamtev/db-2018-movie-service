package com.lamtev.movie_service.datagen;

import com.lamtev.movie_service.datagen.cli_args.ArgumentsParser;
import com.lamtev.movie_service.datagen.generator.LanguageTableGenerator;
import com.lamtev.movie_service.datagen.generator.category.CategoryTableGenerator;
import com.lamtev.movie_service.datagen.generator.movie.MovieTableGenerator;
import com.lamtev.movie_service.datagen.generator.series.SeriesTableGenerator;
import com.lamtev.movie_service.datagen.generator.user.SubscriptionTableGenerator;
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

            final var user = new UserTableGenerator(100_000L, (byte) 53);
            user.updateTableUsing(connection);

            final var userMovie = new UserMovieTableGenerator((byte) 64, 5, 10);
            userMovie.updateTableUsing(connection);

            final var seriesMovie = new UserSeriesTableGenerator((byte) 35, 2, 5);
            seriesMovie.updateTableUsing(connection);

            final var subscription = new SubscriptionTableGenerator(100_000L, 3, 8);
            subscription.updateTableUsing(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
