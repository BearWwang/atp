package com.snake.genrater;

public class RockBand {

    private final Genrater genrater;

    protected RockBand(Genrater genrater) {
        this.genrater = genrater;
    }

    public String name() {
        return genrater.resolve("rock_band.name");
    }
}
