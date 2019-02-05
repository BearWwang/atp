package com.snake.genrater;

public class Team {
    private final Genrater genrater;

    protected Team(Genrater genrater) {
        this.genrater = genrater;
    }

    public String name() {
        return genrater.fakeValuesService().resolve("team.name", this, genrater);
    }

    public String creature() {
        return genrater.fakeValuesService().resolve("team.creature", this, genrater);
    }

    public String state() {
        return genrater.fakeValuesService().resolve("address.state", this, genrater);
    }

    public String sport() {
        return genrater.fakeValuesService().resolve("team.sport", this, genrater);
    }
}
