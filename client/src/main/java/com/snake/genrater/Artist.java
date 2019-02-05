package com.snake.genrater;

public class Artist {

    private final Genrater genrater;

    protected Artist(Genrater genrater) {
        this.genrater = genrater;
    }

    public String name() {
        return genrater.fakeValuesService().fetchString("artist.names");
    }
}
