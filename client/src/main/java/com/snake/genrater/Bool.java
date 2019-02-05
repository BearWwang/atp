package com.snake.genrater;

public class Bool {
    private final Genrater genrater;

    protected Bool(Genrater genrater) {
        this.genrater = genrater;
    }

    public boolean bool() {
        return genrater.random().nextBoolean();
    }
}
