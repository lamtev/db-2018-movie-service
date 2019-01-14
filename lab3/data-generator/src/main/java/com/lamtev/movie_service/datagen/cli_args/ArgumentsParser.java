package com.lamtev.movie_service.datagen.cli_args;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ArgumentsParser {

    @NotNull
    private final String[] args;
    @NotNull
    private final Gson gson;

    public ArgumentsParser(final @NotNull String[] args) {
        this.args = args;
        this.gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(EndpointInfo.class, new Deserializer<EndpointInfo>())
                .registerTypeAdapter(Parameters.class, new Deserializer<Parameters>())
                .create();
    }

    @Nullable
    public EndpointInfo endpoint() {
        try (final var fileReader = new FileReader(args[0])) {
            return gson.fromJson(fileReader, EndpointInfo.class);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public Parameters parameters() {
        try (final var fileReader = new FileReader(args[1])) {
            final var params = gson.fromJson(fileReader, Parameters.class);
            if (args.length == 3) {
                switch (args[2]) {
                    case "onlyMovies":
                        params.setGenMoviesOnly(true);
                        params.setGenAll(false);
                        break;
                    case "onlySeries":
                        params.setGenSeriesOnly(true);
                        params.setGenAll(false);
                        break;
                    case "onlyUsers":
                        params.setGenUsersOnly(true);
                        params.setGenAll(false);
                        break;
                    case "onlyMovieSubscriptions":
                        params.setGenSubscriptionsToMoviesOnly(true);
                        params.setGenAll(false);
                        break;
                    case "onlySeriesSubscriptions":
                        params.setGenSubscriptionsToSeriesOnly(true);
                        params.setGenAll(false);
                        break;
                    default:
                        params.setGenAll(true);
                        break;
                }
            } else {
                params.setGenAll(true);
            }
            return params;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    class Deserializer<T> implements JsonDeserializer<T> {

        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final T obj = new Gson().fromJson(json, typeOfT);

            final var badField = Arrays.stream(obj.getClass().getDeclaredFields())
                    .filter(field -> {
                        try {
                            field.setAccessible(true);
                            return field.get(obj) == null;
                        } catch (IllegalAccessError | IllegalAccessException ignored) {
                            return false;
                        }
                    })
                    .findFirst();

            if (badField.isPresent()) {
                throw new JsonParseException("Missing field: " + badField.get().getName());
            }

            return obj;
        }
    }

}
