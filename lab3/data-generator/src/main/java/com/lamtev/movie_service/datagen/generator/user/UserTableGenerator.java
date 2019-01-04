package com.lamtev.movie_service.datagen.generator.user;

import com.lamtev.movie_service.datagen.generator.TableGenerator;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public final class UserTableGenerator implements TableGenerator {

    private static byte[] buf = null;
    private final long count;
    private final int femalePercent;

    public UserTableGenerator(long count, int femalePercentage) {
        this.count = count;
        this.femalePercent = femalePercentage;
    }

    @Override
    public void updateTableUsing(final @NotNull Connection connection) {
        try (final var statement = connection.prepareStatement(
                "INSERT INTO \"user\" (login, password_hash, email, birthday, sex, first_name, last_name) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                RETURN_GENERATED_KEYS
        )) {
            for (int i = 0; i < count; ++i) {
                int j = 0;
                final var firstName = FAKER.name().firstName();
                final var lastName = FAKER.name().lastName();
                final var username = firstName + "." + lastName + RANDOM.nextInt((int) count);
                statement.setString(++j, username);
                statement.setString(++j, Long.toHexString(FAKER.number().randomNumber()));
                statement.setString(++j, username + "@email.com");
                statement.setDate(++j, UTILS.randomDate(100));
                statement.setString(++j, randomSex());
                statement.setString(++j, firstName);
                statement.setString(++j, lastName);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String randomSex() {
        if (buf == null) {
            buf = new byte[100];
            for (int i = 0; i < buf.length; ++i) {
                if (i < femalePercent) {
                    buf[i] = 0;
                } else {
                    buf[i] = 1;
                }
            }
        }

        return Byte.toString(buf[RANDOM.nextInt(100)]);
    }

}
