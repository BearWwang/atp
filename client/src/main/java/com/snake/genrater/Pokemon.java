package com.snake.genrater;

public class Pokemon {

    private final Genrater genrater;

    protected Pokemon(Genrater genrater) {
        this.genrater = genrater;
    }

    public String name() {
        return genrater.resolve("pokemon.names");
    }

    public String location() {
        return genrater.resolve("pokemon.locations");
    }
}
