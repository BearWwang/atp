package com.snake.genrater;

public class Ancient {

    private final Genrater genrater;

    protected Ancient(Genrater genrater) {
        this.genrater = genrater;
    }

    public String god() {
        return genrater.resolve("ancient.god");
    }

    public String primordial() {
        return genrater.resolve("ancient.primordial");
    }

    public String titan() {
        return genrater.resolve("ancient.titan");
    }

    public String hero() {
        return genrater.resolve("ancient.hero");
    }
}
