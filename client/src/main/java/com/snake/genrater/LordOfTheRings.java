package com.snake.genrater;

public class LordOfTheRings {
    private final Genrater genrater;

    protected LordOfTheRings(final Genrater genrater) {
        this.genrater = genrater;
    }

    public String character() {
        return genrater.resolve("lord_of_the_rings.characters");
    }

    public String location() {
        return genrater.resolve("lord_of_the_rings.locations");
    }
}
