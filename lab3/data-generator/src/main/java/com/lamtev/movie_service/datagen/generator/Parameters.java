package com.lamtev.movie_service.datagen.generator;

import org.jetbrains.annotations.NotNull;

public class Parameters {

    @NotNull
    private final EndpointInfo endpoint;
    private final int usersCount;
    private final int moviesCount;
    private final int seriesCount;

    private Parameters(final @NotNull EndpointInfo endpoint,
                       int usersCount,
                       int moviesCount,
                       int seriesCount) {
        this.endpoint = endpoint;
        this.usersCount = usersCount;
        this.moviesCount = moviesCount;
        this.seriesCount = seriesCount;
    }

    @NotNull
    public EndpointInfo getEndpoint() {
        return endpoint;
    }

    public int usersCount() {
        return usersCount;
    }

    public int moviesCount() {
        return moviesCount;
    }

    public int seriesCount() {
        return seriesCount;
    }

    public static class Builder {

        @NotNull
        private EndpointInfo endpoint;
        private int usersCount = 1_000_000;
        private int moviesCount = 10_000;
        private int seriesCount = 1_000;

        public Parameters build() {
            return new Parameters(endpoint, usersCount, moviesCount, seriesCount);
        }

        public Builder setEndpoint(final @NotNull EndpointInfo endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder setUsersCount(int usersCount) {
            this.usersCount = usersCount;
            return this;
        }

        public Builder setMoviesCount(int moviesCount) {
            this.moviesCount = moviesCount;
            return this;
        }

        public Builder setSeriesCount(int seriesCount) {
            this.seriesCount = seriesCount;
            return this;
        }

    }

    public static class EndpointInfo {

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

}
