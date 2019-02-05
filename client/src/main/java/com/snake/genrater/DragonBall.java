package com.snake.genrater;

public class DragonBall {
    private final Genrater genrater;

    protected DragonBall(Genrater genrater) {
        this.genrater = genrater;
    }

    public String character() {
        return genrater.fakeValuesService().resolve("dragon_ball.characters", this, genrater);
    }
}
