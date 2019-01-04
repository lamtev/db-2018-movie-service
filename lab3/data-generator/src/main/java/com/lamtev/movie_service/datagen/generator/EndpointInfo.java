package com.lamtev.movie_service.datagen.generator;

import org.jetbrains.annotations.NotNull;

public class EndpointInfo {

    @NotNull
    private final String url;
    @NotNull
    private final String user;
    @NotNull
    private final String password;

    public EndpointInfo(@NotNull final String url,
                        @NotNull final String user,
                        @NotNull final String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @NotNull
    public String url() {
        return url;
    }

    @NotNull
    public String user() {
        return user;
    }

    @NotNull
    public String password() {
        return password;
    }

}
