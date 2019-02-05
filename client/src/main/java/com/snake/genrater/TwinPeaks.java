package com.snake.genrater;

public class TwinPeaks {
    private final Genrater genrater;

    protected TwinPeaks(final Genrater genrater) {
        this.genrater = genrater;
    }

    public String character() {
        return genrater.resolve("twin_peaks.characters");
    }

    public String location() {
        return genrater.resolve("twin_peaks.locations");
    }

    public String quote() {
        return genrater.resolve("twin_peaks.quotes");
    }
}
