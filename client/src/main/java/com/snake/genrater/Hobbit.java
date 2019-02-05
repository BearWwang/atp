package com.snake.genrater;

public class Hobbit {
    private final Genrater genrater;

    protected Hobbit(Genrater genrater) {
        this.genrater = genrater;
    }

    public String character() {
        return genrater.fakeValuesService().resolve("hobbit.character", this, genrater);
    }

    public String thorinsCompany() {
        return genrater.fakeValuesService().resolve("hobbit.thorins_company", this, genrater);
    }

    public String quote() {
        return genrater.fakeValuesService().resolve("hobbit.quote", this, genrater);
    }

    public String location() {
        return genrater.fakeValuesService().resolve("hobbit.location", this, genrater);
    }
}
