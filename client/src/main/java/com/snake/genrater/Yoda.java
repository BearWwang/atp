package com.snake.genrater;

public class Yoda {
    private final Genrater genrater;

    protected Yoda(final Genrater genrater) {
        this.genrater = genrater;
    }

    public String quote() {
        return genrater.resolve("yoda.quotes");
    }
}
