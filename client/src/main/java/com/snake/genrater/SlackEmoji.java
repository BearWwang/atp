package com.snake.genrater;

public class SlackEmoji {

    private final Genrater genrater;

    protected SlackEmoji(Genrater genrater) {
        this.genrater = genrater;
    }

    public String people() {
        return genrater.resolve("slack_emoji.people");
    }

    public String nature() {
        return genrater.resolve("slack_emoji.nature");
    }

    public String foodAndDrink() {
        return genrater.resolve("slack_emoji.food_and_drink");
    }

    public String celebration() {
        return genrater.resolve("slack_emoji.celebration");
    }

    public String activity() {
        return genrater.resolve("slack_emoji.activity");
    }

    public String travelAndPlaces() {
        return genrater.resolve("slack_emoji.travel_and_places");
    }

    public String objectsAndSymbols() {
        return genrater.resolve("slack_emoji.objects_and_symbols");
    }

    public String custom() {
        return genrater.resolve("slack_emoji.custom");
    }

    public String emoji() {
        return genrater.fakeValuesService().resolve("slack_emoji.emoji", this, genrater);
    }
}
