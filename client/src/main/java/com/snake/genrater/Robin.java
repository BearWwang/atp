package com.snake.genrater;

public class Robin {
    private final Genrater genrater;

    protected Robin(Genrater genrater) {
        this.genrater = genrater;
    }

    public String quote() {
        return genrater.fakeValuesService().resolve("robin.quotes", this, genrater);
    }
}
