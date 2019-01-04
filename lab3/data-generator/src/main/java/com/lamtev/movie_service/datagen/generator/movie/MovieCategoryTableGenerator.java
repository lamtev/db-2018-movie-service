package com.lamtev.movie_service.datagen.generator.movie;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public final class MovieCategoryTableGenerator implements TableGenerator {

    @NotNull
    private final int[] movieIds;
    @NotNull
    private final TIntList categoryIds;
    private final boolean sameCategoriesForAllMovies;

    public MovieCategoryTableGenerator(final @NotNull int[] movieIds, final @NotNull int[] categoryIds, boolean sameCategoriesForAllMovies) {
        this.movieIds = movieIds;
        this.categoryIds = new TIntArrayList(categoryIds.length);
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
        categoryIds.shuffle(RANDOM);
        final var res = new int[n];
        for (int i = 0; i < n; ++i) {
            res[i] = categoryIds.get(i);
        }

        return res;
    }

}
