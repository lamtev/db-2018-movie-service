package com.lamtev.movie_service.datagen.generator.movie;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MovieCategoryTableGenerator implements TableGenerator {

    @NotNull
    private final int[] movieIds;
    @NotNull
    private final List<Integer> categoryIds;
    private final boolean sameCategoriesForAllMovies;

    public MovieCategoryTableGenerator(final @NotNull int[] movieIds, final @NotNull int[] categoryIds, boolean sameCategoriesForAllMovies) {
        this.movieIds = movieIds;
        this.categoryIds = new ArrayList<>(categoryIds.length);
        Arrays.stream(categoryIds).forEach(this.categoryIds::add);
        this.sameCategoriesForAllMovies = sameCategoriesForAllMovies;
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        try (final var statement = connection.prepareStatement(
                "INSERT INTO movie_category (movie_id, category_id) VALUES (?, ?)"
        )) {
            final var categories = nRandomCategories(3);
            for (int movieId : movieIds) {
                final var differentCategories = nRandomCategories(3);
                for (int j = 0; j < categories.length; ++j) {
                    int i = 0;
                    statement.setInt(++i, movieId);
                    if (sameCategoriesForAllMovies) {
                        statement.setInt(++i, categories[j]);
                    } else {
                        statement.setInt(++i, differentCategories[j]);
                    }
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private int[] nRandomCategories(int n) {
        Collections.shuffle(categoryIds);
        final var res = new int[n];
        for (int i = 0; i < n; ++i) {
            res[i] = categoryIds.get(i);
        }

        return res;
    }

}
