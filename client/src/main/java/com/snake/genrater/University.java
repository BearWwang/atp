package com.snake.genrater;

public class University {
    private final Genrater genrater;

    protected University(Genrater genrater) {
        this.genrater = genrater;
    }

    public String name() {
        return genrater.fakeValuesService().resolve("university.name", this, genrater);
    }

    public String prefix() {
        return genrater.fakeValuesService().resolve("university.prefix", this, genrater);
    }

    public String suffix() {
        return genrater.fakeValuesService().resolve("university.suffix", this, genrater);
    }
}
