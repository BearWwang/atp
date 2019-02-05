package com.snake.genrater;

public class Esports {
    private final Genrater genrater;

    protected Esports(final Genrater genrater) {
        this.genrater = genrater;
    }

    public String player() {
        return genrater.resolve("esports.players");
    }

    public String team() {
        return genrater.resolve("esports.teams");
    }

    public String event() {
        return genrater.resolve("esports.events");
    }

    public String league() {
        return genrater.resolve("esports.leagues");
    }

    public String game() {
        return genrater.resolve("esports.games");
    }
}
