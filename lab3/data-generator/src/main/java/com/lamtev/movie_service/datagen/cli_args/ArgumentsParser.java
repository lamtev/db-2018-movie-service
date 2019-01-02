package com.lamtev.movie_service.datagen.cli_args;

import com.google.gson.Gson;
import com.lamtev.movie_service.datagen.generator.Parameters;
import com.lamtev.movie_service.datagen.generator.Parameters.EndpointInfo;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.IOException;

public class ArgumentsParser {
    @NotNull
    public Parameters parseArguments(final @NotNull String[] args) {
        try (final var fileReader = new FileReader("/Users/anton.lamtev/university/movie-service/lab3/data-generator/config.json")) {
            EndpointInfo endpoint = new Gson().fromJson(fileReader, EndpointInfo.class);
            return new Parameters.Builder().setEndpoint(endpoint).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
