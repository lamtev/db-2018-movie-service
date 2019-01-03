package com.lamtev.movie_service.datagen.generator.category;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class CategoryTableGenerator implements TableGenerator {

    private static final Map<String, String> CATEGORY_TO_SUPERCATEGORY = new LinkedHashMap<>() {{
        put("genre", "");
        put("comedy", "genre");
        put("drama", "genre");
        put("thriller", "genre");
        put("new", "");
        put("horror", "genre");
        put("action", "genre");
        put("crime", "genre");
        put("western", "genre");
        put("popular", "");
        put("mystery", "genre");
        put("adventure", "genre");
        put("classic", "");
        put("romance", "genre");
        put("science-fiction", "genre");
        put("soviet", "");
        put("hollywood", "");
    }};

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        try (final var statement = connection.createStatement()) {
            final var categoryIds = new int[CATEGORY_TO_SUPERCATEGORY.size()];
            int i = 0;
            for (final var entry : CATEGORY_TO_SUPERCATEGORY.entrySet()) {
                final var category = entry.getKey();
                final var supercategory = entry.getValue();

                final var query = String.format(
                        "INSERT INTO category (name, supercategory_id) " +
                                "SELECT '%s', (SELECT id FROM category WHERE name = '%s' LIMIT 1)", category, supercategory
                );
                try {
                    statement.executeUpdate(query, RETURN_GENERATED_KEYS);
                    final var generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        categoryIds[i] = generatedKeys.getInt(1);
                    }
                    i++;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            final var categoryTranslation = new CategoryTranslationTableGenerator(categoryIds);
            categoryTranslation.updateTableUsing(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
