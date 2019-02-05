package com.snake.genrater;

public class Food {

    private final Genrater genrater;

    protected Food(Genrater genrater) {
        this.genrater = genrater;
    }

    public String ingredient() {
        return genrater.fakeValuesService().resolve("food.ingredients", this, genrater);
    }

    public String spice() {
        return genrater.fakeValuesService().resolve("food.spices", this, genrater);
    }

    public String measurement() {
        return genrater.fakeValuesService().resolve("food.measurement_sizes", this, genrater) +
            " " + genrater.fakeValuesService().resolve("food.measurements", this, genrater);
    }
}
