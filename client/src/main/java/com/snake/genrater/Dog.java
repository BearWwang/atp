package com.snake.genrater;

public class Dog {

    private final Genrater genrater;

    protected Dog(Genrater genrater) {
        this.genrater = genrater;
    }

    public String name() {
        return genrater.fakeValuesService().resolve("dog.name", this, genrater);
    }

    public String breed() {
        return genrater.fakeValuesService().resolve("dog.breed", this, genrater);
    }

    public String sound() {
        return genrater.fakeValuesService().resolve("dog.sound", this, genrater);
    }

    public String memePhrase() {
        return genrater.fakeValuesService().resolve("dog.meme_phrase", this, genrater);
    }

    public String age() {
        return genrater.fakeValuesService().resolve("dog.age", this, genrater);
    }

    public String coatLength() {
        return genrater.fakeValuesService().resolve("dog.coat_length", this, genrater);
    }

    public String gender() {
        return genrater.fakeValuesService().resolve("dog.gender", this, genrater);
    }

    public String size() {
        return genrater.fakeValuesService().resolve("dog.size", this, genrater);
    }
}
