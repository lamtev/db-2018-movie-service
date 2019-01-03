package com.lamtev.movie_service.datagen;

import com.lamtev.movie_service.datagen.cli_args.ArgumentsParser;
import com.lamtev.movie_service.datagen.generator.LanguageTableGenerator;
import com.lamtev.movie_service.datagen.generator.category.CategoryTableGenerator;
import com.lamtev.movie_service.datagen.generator.movie.MovieTableGenerator;
import com.lamtev.movie_service.datagen.generator.series.SeriesTableGenerator;

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
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
