package com.snake.genrater;

public class Matz {
    private final Genrater genrater;

    protected Matz(final Genrater genrater) {
        this.genrater = genrater;
    }

    public String quote() {
        return genrater.resolve("matz.quotes");
    }
}
