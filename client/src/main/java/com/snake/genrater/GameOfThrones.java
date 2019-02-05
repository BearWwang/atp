package com.snake.genrater;

public class GameOfThrones {

    private final Genrater genrater;

    protected GameOfThrones(Genrater genrater) {
        this.genrater = genrater;
    }

    public String character() {
        return genrater.resolve("game_of_thrones.characters");
    }

    public String house() {
        return genrater.resolve("game_of_thrones.houses");
    }

    public String city() {
        return genrater.resolve("game_of_thrones.cities");
    }

    public String dragon() {
        return genrater.resolve("game_of_thrones.dragons");
    }

    public String quote() {
      return genrater.resolve("game_of_thrones.quotes");
    }
}
