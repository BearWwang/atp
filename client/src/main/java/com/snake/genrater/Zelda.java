package com.snake.genrater;

public class Zelda {
    private final Genrater genrater;

    protected Zelda(final Genrater genrater) {
        this.genrater = genrater;
    }

    public String game() {
        return genrater.resolve("zelda.games");
    }

    public String character() {
        return genrater.resolve("zelda.characters");
    }
}
