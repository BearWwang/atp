package com.snake.genrater;

public class App {
    private final Genrater genrater;

    protected App(Genrater genrater) {
        this.genrater = genrater;
    }

    public String name() {
        return genrater.fakeValuesService().resolve("app.name", this, genrater);
    }

    public String version() {
        return genrater.numerify(genrater.fakeValuesService().resolve("app.version", this, genrater));
    }

    public String author() {
        return genrater.fakeValuesService().resolve("app.author", this, genrater);
    }
}
