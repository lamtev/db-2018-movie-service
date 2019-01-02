package com.lamtev.movie_service.datagen.generator.movie;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class MovieTranslationTableGenerator implements TableGenerator {

    private static final String VIDEO_URL_TEMPLATE = "https://blob.movie-service.lamtev.com/?vid=";

    @NotNull
    private final int[] movieIds;
    private final boolean moviesAreSeriesEpisodes;

    public MovieTranslationTableGenerator(final @NotNull int[] movieIds, boolean moviesAreSeriesEpisodes) {
        this.movieIds = movieIds;
        this.moviesAreSeriesEpisodes = moviesAreSeriesEpisodes;
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        try (final var statement = connection.prepareStatement(
                "INSERT INTO movie_translation (movie_id, language_id, name, director, description, file_url) " +
                        "VALUES (?, ?, ?, ?, ?, ?)"
        )) {
            final var director = moviesAreSeriesEpisodes ? FAKER.artist().name() : null;
            for (int movieIdIdx = 0; movieIdIdx < movieIds.length; ++movieIdIdx) {
                for (int languageId = 1; languageId <= 2; ++languageId) {
                    int i = 0;
                    statement.setInt(++i, movieIds[movieIdIdx]);
                    statement.setInt(++i, languageId);
                    if (moviesAreSeriesEpisodes) {
                        statement.setString(++i, "Episode " + movieIdIdx);
                        statement.setString(++i, director);
                    } else {
                        final var movie = FAKER.book();
                        statement.setString(++i, movie.title());
                        statement.setString(++i, movie.author());
                    }
                    statement.setString(++i, FAKER.lorem().paragraph(10));
                    statement.setString(++i, randomUrl());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String randomUrl() {
        return VIDEO_URL_TEMPLATE + RANDOM.nextInt(Integer.MAX_VALUE);

    }

}
