package com.lamtev.movie_service.datagen.cli_args;

import com.google.gson.*;
import com.lamtev.movie_service.datagen.generator.EndpointInfo;
import com.lamtev.movie_service.datagen.generator.Parameters;
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
            return gson.fromJson(fileReader, Parameters.class);
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
