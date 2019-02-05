package com.snake.genrater;

public class RickAndMorty  {
    private final Genrater genrater;

    protected RickAndMorty(final Genrater genrater) {
        this.genrater = genrater;
    }

    public String character() {
        return genrater.resolve("rick_and_morty.characters");
    }

    public String location() {
        return genrater.resolve("rick_and_morty.locations");
    }

    public String quote() {
        return genrater.resolve("rick_and_morty.quotes");
    }
}
