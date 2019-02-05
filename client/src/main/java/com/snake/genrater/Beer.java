package com.snake.genrater;

public class Beer {
    private final Genrater genrater;

    protected Beer(Genrater genrater) {
        this.genrater = genrater;
    }

    public String name() {
        return genrater.fakeValuesService().resolve("beer.name", this, genrater);
    }

    public String style() {
        return genrater.fakeValuesService().resolve("beer.style", this, genrater);
    }

    public String hop() {
        return genrater.fakeValuesService().resolve("beer.hop", this, genrater);
    }

    public String yeast() {
        return genrater.fakeValuesService().resolve("beer.yeast", this, genrater);
    }

    public String malt() {
        return genrater.fakeValuesService().resolve("beer.malt", this, genrater);
    }
}
