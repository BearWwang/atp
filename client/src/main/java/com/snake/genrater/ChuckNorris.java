package com.snake.genrater;

public class ChuckNorris {
    private final Genrater genrater;

    protected ChuckNorris(Genrater genrater) {
        this.genrater = genrater;
    }

    public String fact() {
        return genrater.fakeValuesService().resolve("chuck_norris.fact", this, genrater);
    }
}
