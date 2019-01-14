package com.lamtev.movie_service.datagen.cli_args;

import org.jetbrains.annotations.NotNull;

public final class Parameters {

    private final int usersCount;
    private final int femalePercentage;
    private final int moviesCount;
    /**
     * {{1000, 3, 15}, ...} - 1000 series, each consists of 3 seasons with 15 episodes
     */
    @NotNull
    private final int[][] seriesCountSeasonsEpisodes;
    private final int percentageOfUsersWhoBoughtMovies;
    private final int minMoviesPerUser;
    private final int maxMoviesPerUser;
    private final int percentageOfUsersWhoBoughtSeries;
    private final int minSeriesPerUser;
    private final int maxSeriesPerUser;
    private final int minSubscriptionsPerUser;
    private final int maxSubscriptionsPerUser;
    /**
     * {{duration in days, price in USD, number of movies, number of series seasons}, ... }
     */
    @NotNull
    private final int[][] durationPriceNMoviesMSeasons;
    private final int moviesSubscriptionsPercentage;
    private final int yearsSinceFirstSubscription;

    private boolean genMoviesOnly = false;
    private boolean genSeriesOnly = false;
    private boolean genUsersOnly = false;
    private boolean genSubscriptionsToMoviesOnly = false;
    private boolean genSubscriptionsToSeriesOnly = false;
    private boolean genAll = true;

    public Parameters(int usersCount,
                      int femalePercentage,
                      int moviesCount,
                      final @NotNull int[][] seriesCountSeasonsEpisodes,
                      int percentageOfUsersWhoBoughtMovies,
                      int minMoviesPerUser,
                      int maxMoviesPerUser,
                      int percentageOfUsersWhoBoughtSeries,
                      int minSeriesPerUser,
                      int maxSeriesPerUser,
                      int yearsSinceFirstSubscription,
                      int minSubscriptionsPerUser,
                      int maxSubscriptionsPerUser,
                      @NotNull int[][] durationPriceNMoviesMSeasons,
                      int moviesSubscriptionsPercentage) {
        this.usersCount = usersCount;
        this.femalePercentage = femalePercentage;
        this.moviesCount = moviesCount;
        this.seriesCountSeasonsEpisodes = seriesCountSeasonsEpisodes;
        this.percentageOfUsersWhoBoughtMovies = percentageOfUsersWhoBoughtMovies;
        this.minMoviesPerUser = minMoviesPerUser;
        this.maxMoviesPerUser = maxMoviesPerUser;
        this.percentageOfUsersWhoBoughtSeries = percentageOfUsersWhoBoughtSeries;
        this.minSeriesPerUser = minSeriesPerUser;
        this.maxSeriesPerUser = maxSeriesPerUser;
        this.yearsSinceFirstSubscription = yearsSinceFirstSubscription;
        this.minSubscriptionsPerUser = minSubscriptionsPerUser;
        this.maxSubscriptionsPerUser = maxSubscriptionsPerUser;
        this.durationPriceNMoviesMSeasons = durationPriceNMoviesMSeasons;
        this.moviesSubscriptionsPercentage = moviesSubscriptionsPercentage;
    }

    public int femalePercentage() {
        return femalePercentage;
    }

    public int percentageOfUsersWhoBoughtMovies() {
        return percentageOfUsersWhoBoughtMovies;
    }

    public int minMoviesPerUser() {
        return minMoviesPerUser;
    }

    public int maxMoviesPerUser() {
        return maxMoviesPerUser;
    }

    public int percentageOfUsersWhoBoughtSeries() {
        return percentageOfUsersWhoBoughtSeries;
    }

    public int minSeriesPerUser() {
        return minSeriesPerUser;
    }

    public int maxSeriesPerUser() {
        return maxSeriesPerUser;
    }

    public int minSubscriptionsPerUser() {
        return minSubscriptionsPerUser;
    }

    public int maxSubscriptionsPerUser() {
        return maxSubscriptionsPerUser;
    }

    @NotNull
    public int[][] durationPriceNMoviesMSeasons() {
        return durationPriceNMoviesMSeasons;
    }

    public int moviesSubscriptionsPercentage() {
        return moviesSubscriptionsPercentage;
    }

    public int usersCount() {
        return usersCount;
    }

    public int moviesCount() {
        return moviesCount;
    }

    @NotNull
    public int[][] seriesCountSeasonsEpisodes() {
        return seriesCountSeasonsEpisodes;
    }

    public int yearsSinceFirstSubscription() {
        return yearsSinceFirstSubscription;
    }

    public boolean isGenMoviesOnly() {
        return genMoviesOnly;
    }

    public void setGenMoviesOnly(boolean genMoviesOnly) {
        this.genMoviesOnly = genMoviesOnly;
    }

    public boolean isGenSeriesOnly() {
        return genSeriesOnly;
    }

    public void setGenSeriesOnly(boolean genSeriesOnly) {
        this.genSeriesOnly = genSeriesOnly;
    }

    public boolean isGenUsersOnly() {
        return genUsersOnly;
    }

    public void setGenUsersOnly(boolean genUsersOnly) {
        this.genUsersOnly = genUsersOnly;
    }

    public boolean isGenSubscriptionsToMoviesOnly() {
        return genSubscriptionsToMoviesOnly;
    }

    public void setGenSubscriptionsToMoviesOnly(boolean genSubscriptionsToMoviesOnly) {
        this.genSubscriptionsToMoviesOnly = genSubscriptionsToMoviesOnly;
    }

    public boolean isGenSubscriptionsToSeriesOnly() {
        return genSubscriptionsToSeriesOnly;
    }

    public void setGenSubscriptionsToSeriesOnly(boolean genSubscriptionsToSeriesOnly) {
        this.genSubscriptionsToSeriesOnly = genSubscriptionsToSeriesOnly;
    }

    public boolean isGenAll() {
        return genAll;
    }

    public void setGenAll(boolean genAll) {
        this.genAll = genAll;
    }

}
