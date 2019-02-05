package com.snake.genrater;

public class Color {
    private final Genrater genrater;

    protected Color(Genrater genrater) {
        this.genrater = genrater;
    }

    public String name() {
        return genrater.fakeValuesService().resolve("color.name", this, genrater);
    }
}
