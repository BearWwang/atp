package com.snake.genrater;

public class Lebowski {
    private final Genrater genrater;

    public Lebowski(final Genrater genrater) {
        this.genrater = genrater;
    }

    public String actor() {
        return genrater.fakeValuesService().resolve("lebowski.actors", this, genrater);
    }

    public String character() {
        return genrater.fakeValuesService().resolve("lebowski.characters", this, genrater);
    }

    public String quote() {
        return genrater.fakeValuesService().resolve("lebowski.quotes", this, genrater);
    }
}
