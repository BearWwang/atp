package com.snake.genrater;

public class FunnyName {
    private final Genrater genrater;

    protected FunnyName(Genrater genrater) {
        this.genrater = genrater;
    }

    public String name() {
        return genrater.fakeValuesService().resolve("funny_name.name", this, genrater);
    }
}
