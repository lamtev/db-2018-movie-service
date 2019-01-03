package com.lamtev.movie_service.datagen.generator.category;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class CategoryTranslationTableGenerator implements TableGenerator {

    @NotNull
    private final int[] categoryIds;

    public CategoryTranslationTableGenerator(final @NotNull int[] categoryIds) {
        this.categoryIds = categoryIds;
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        try (final var statement = connection.prepareStatement(
                "INSERT INTO category_translation (category_id, language_id, translation) VALUES (?, ?, ?)"
        )) {
            for (final var categoryId : categoryIds) {
                for (int languageId = 1; languageId <= 2; ++languageId) {
                    int i = 0;
                    statement.setInt(++i, categoryId);
                    statement.setInt(++i, languageId);
                    statement.setString(++i, FAKER.lorem().word());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
