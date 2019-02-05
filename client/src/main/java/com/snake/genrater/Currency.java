package com.snake.genrater;

public class Currency {

    private final Genrater genrater;

    public Currency(Genrater genrater) {
        this.genrater = genrater;
    }

    public String name() {
        return genrater.fakeValuesService().resolve("currency.name", this, genrater);
    }

    public String code() {
        return genrater.fakeValuesService().resolve("currency.code", this, genrater);
    }
}
