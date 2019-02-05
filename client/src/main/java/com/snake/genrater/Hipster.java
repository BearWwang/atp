package com.snake.genrater;

public class Hipster {
    private final Genrater genrater;

    protected Hipster(final Genrater genrater) {
        this.genrater = genrater;
    }

    public String word() {
        return genrater.resolve("hipster.words");
    }
}
