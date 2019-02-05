package com.snake.genrater;

public class Friends {
    private final Genrater genrater;

    protected Friends(Genrater genrater) {
        this.genrater = genrater;
    }

    public String character() {
        return genrater.resolve("friends.characters");
    }

    public String location() {
        return genrater.resolve("friends.locations");
    }

    public String quote() {
        return genrater.resolve("friends.quotes");
    }
}