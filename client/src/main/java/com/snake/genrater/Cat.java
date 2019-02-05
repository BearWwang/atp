package com.snake.genrater;

public class Cat {

    private final Genrater genrater;

    protected Cat(Genrater genrater) {
        this.genrater = genrater;
    }

    public String name() {
        return genrater.fakeValuesService().resolve("cat.name", this, genrater);
    }

    public String breed() {
        return genrater.fakeValuesService().resolve("cat.breed", this, genrater);
    }

    public String registry() {
        return genrater.fakeValuesService().resolve("cat.registry", this, genrater);
    }
}
