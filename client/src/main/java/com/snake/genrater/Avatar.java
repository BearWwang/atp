package com.snake.genrater;

public class Avatar {
    private final Genrater genrater;
    private final String baseUrl;

    protected Avatar(Genrater genrater) {
        this.genrater = genrater;
        this.baseUrl = "https://s3.amazonaws.com/uifaces/faces/twitter/";
    }

    public String image() {
        return baseUrl + genrater.fakeValuesService().resolve("internet.avatar", this, genrater);
    }
}
