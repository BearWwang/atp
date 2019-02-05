package com.snake.genrater;

public class Hacker {
    private final Genrater genrater;

    protected Hacker(Genrater genrater) {
        this.genrater = genrater;
    }

    public String abbreviation() {
        return genrater.fakeValuesService().resolve("hacker.abbreviation", this, genrater);
    }

    public String adjective() {
        return genrater.fakeValuesService().resolve("hacker.adjective", this, genrater);
    }

    public String noun() {
        return genrater.fakeValuesService().resolve("hacker.noun", this, genrater);
    }

    public String verb() {
        return genrater.fakeValuesService().resolve("hacker.verb", this, genrater);
    }

    public String ingverb() {
        return genrater.fakeValuesService().resolve("hacker.ingverb", this, genrater);
    }
}
