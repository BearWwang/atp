package com.snake.genrater;

public class Book {
    private final Genrater genrater;

    protected Book(Genrater genrater) {
        this.genrater = genrater;
    }

    public String author() {
        return genrater.fakeValuesService().resolve("book.author", this, genrater);
    }

    public String title() {
        return genrater.fakeValuesService().resolve("book.title", this, genrater);
    }

    public String publisher() {
        return genrater.fakeValuesService().resolve("book.publisher", this, genrater);
    }

    public String genre() {
        return genrater.fakeValuesService().resolve("book.genre", this, genrater);
    }
}
