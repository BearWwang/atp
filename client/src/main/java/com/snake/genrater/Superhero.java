package com.snake.genrater;

public class Superhero {
    private final Genrater genrater;

    protected Superhero(Genrater genrater) {
        this.genrater = genrater;
    }

    public String name() {
        return genrater.fakeValuesService().resolve("superhero.name", this, genrater);
    }

    public String prefix() {
        return genrater.fakeValuesService().resolve("superhero.prefix", this, genrater);
    }

    public String suffix() {
        return genrater.fakeValuesService().resolve("superhero.suffix", this, genrater);
    }

    public String power() {
        return genrater.fakeValuesService().resolve("superhero.power", this, genrater);
    }

    public String descriptor() {
        return genrater.fakeValuesService().resolve("superhero.descriptor", this, genrater);
    }
}
