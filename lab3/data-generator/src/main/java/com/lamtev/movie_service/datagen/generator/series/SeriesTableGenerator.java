package com.lamtev.movie_service.datagen.generator.series;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import com.lamtev.movie_service.datagen.generator.series.season.SeriesSeasonTableGenerator;
import org.jetbrains.annotations.NotNull;
import org.postgresql.util.PGmoney;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SeriesTableGenerator implements TableGenerator {

    private static final short[] SERIES_PRICES_IN_USD = new short[]{10, 10, 10, 10, 17, 17, 17, 25, 25, 35};

    /**
     * [[1000, 3, 15], ...] - 1000 series, each consists of 3 seasons with 15 episodes
     */
    @NotNull
    private final int[][] countSeasonsEpisodesArray;

    public SeriesTableGenerator(final @NotNull int[][] countSeasonsEpisodes) {
        this.countSeasonsEpisodesArray = countSeasonsEpisodes;
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        try (final var statement = connection.prepareStatement(
                "INSERT INTO series (seasons, price) VALUES (?, ?)",
                RETURN_GENERATED_KEYS
        )) {
            for (final var countSeasonsEpisodes : countSeasonsEpisodesArray) {
                final int seriesCount = countSeasonsEpisodes[0];
                final short seasons = (short) countSeasonsEpisodes[1];
                final var seriesPrices = new int[seriesCount];
                for (int seriesIdx = 0; seriesIdx < seriesCount; ++seriesIdx) {
                    final int seriesPrice = SERIES_PRICES_IN_USD[RANDOM.nextInt(SERIES_PRICES_IN_USD.length)];
                    seriesPrices[seriesIdx] = seriesPrice;
                    int i = 0;
                    statement.setShort(++i, seasons);
                    statement.setObject(++i, new PGmoney("$" + seriesPrice));
                    statement.addBatch();
                }
                statement.executeBatch();

                final var seriesIds = TableGenerator.getIdsOfRowsInsertedWith(statement, seriesCount);

                final var seriesTranslation = new SeriesTranslationTableGenerator(seriesIds);
                seriesTranslation.updateTableUsing(connection);

                final int episodes = countSeasonsEpisodes[2];
                final var seriesSeason = new SeriesSeasonTableGenerator(seriesIds, seriesPrices, seasons, episodes);
                seriesSeason.updateTableUsing(connection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
