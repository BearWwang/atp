package com.snake.genrater;

public class Music {

    private static final String[] KEYS = new String[] { "C", "D", "E", "F", "G", "A", "B" };
    private static final String[] KEY_VARIANTS = new String[] { "b", "#", "" };
    private static final String[] CHORD_TYPES = new String[] { "", "maj", "6", "maj7", "m", "m7", "-7", "7", "dom7", "dim", "dim7", "m7b5"};

    private final Genrater genrater;

    protected Music(Genrater genrater) {
        this.genrater = genrater;
    }

    public String instrument() {
        return genrater.resolve("music.instruments");
    }

    public String key() {
        return genrater.options().option(KEYS) + genrater.options().option(KEY_VARIANTS);
    }

    public String chord() {
        return key() + genrater.options().option(CHORD_TYPES);
    }

    public String genre() {
        return genrater.resolve("music.genre");
    }
}
