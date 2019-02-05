package com.snake.genrater;

public class StarTrek {
    private final Genrater genrater;

    protected StarTrek(Genrater genrater) {
        this.genrater = genrater;
    }

    public String character() {
        return genrater.fakeValuesService().resolve("star_trek.character", this, genrater);
    }

    public String location() {
        return genrater.fakeValuesService().resolve("star_trek.location", this, genrater);
    }

    public String specie() {
        return genrater.fakeValuesService().resolve("star_trek.specie", this, genrater);
    }

    public String villain() {
        return genrater.fakeValuesService().resolve("star_trek.villain", this, genrater);
    }
}
