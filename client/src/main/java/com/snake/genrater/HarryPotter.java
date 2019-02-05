package com.snake.genrater;

public class HarryPotter {
    private final Genrater genrater;

    protected HarryPotter(final Genrater genrater) {
        this.genrater = genrater;
    }

    public String character() {
        return genrater.resolve("harry_potter.characters");
    }

    public String location() {
        return genrater.resolve("harry_potter.locations");
    }

    public String quote() {
        return genrater.resolve("harry_potter.quotes");
    }

    public String book() {
        return genrater.resolve("harry_potter.books");
    }
}
