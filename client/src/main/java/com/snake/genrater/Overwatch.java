package com.snake.genrater;

public class Overwatch {
    private final Genrater genrater;

    protected Overwatch(Genrater genrater) {
        this.genrater = genrater;
    }

    public String hero() {
        return genrater.fakeValuesService().resolve("overwatch.heroes", this, genrater);
    }

    public String location() {
        return genrater.fakeValuesService().resolve("overwatch.locations", this, genrater);
    }

    public String quote() {
        return genrater.fakeValuesService().resolve("overwatch.quotes", this, genrater);
    }
}
