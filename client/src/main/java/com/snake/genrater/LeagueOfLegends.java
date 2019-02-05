package com.snake.genrater;

public class LeagueOfLegends {
    private final Genrater genrater;

    protected LeagueOfLegends(Genrater genrater) {
        this.genrater = genrater;
    }

    public String champion() {
        return genrater.fakeValuesService().resolve("league_of_legends.champion", this, genrater);
    }

    public String location() {
        return genrater.fakeValuesService().resolve("league_of_legends.location", this, genrater);
    }

    public String quote() {
        return genrater.fakeValuesService().resolve("league_of_legends.quote", this, genrater);
    }

    public String summonerSpell() {
        return genrater.fakeValuesService().resolve("league_of_legends.summoner_spell", this, genrater);
    }

    public String masteries() {
        return genrater.fakeValuesService().resolve("league_of_legends.masteries", this, genrater);
    }

    public String rank() {
        return genrater.fakeValuesService().resolve("league_of_legends.rank", this, genrater);
    }
}
