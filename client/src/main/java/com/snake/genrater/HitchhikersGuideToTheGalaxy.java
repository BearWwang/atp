package com.snake.genrater;

public class HitchhikersGuideToTheGalaxy {
    private final Genrater genrater;

    protected HitchhikersGuideToTheGalaxy(Genrater genrater) {
        this.genrater = genrater;
    }

    public String character() {
        return genrater.fakeValuesService().resolve("hitchhikers_guide_to_the_galaxy.characters", this, genrater);
    }

    public String location() {
        return genrater.fakeValuesService().resolve("hitchhikers_guide_to_the_galaxy.locations", this, genrater);
    }

    public String marvinQuote() {
        return genrater.fakeValuesService().resolve("hitchhikers_guide_to_the_galaxy.marvin_quote", this, genrater);
    }

    public String planet() {
        return genrater.fakeValuesService().resolve("hitchhikers_guide_to_the_galaxy.planets", this, genrater);
    }

    public String quote() {
        return genrater.fakeValuesService().resolve("hitchhikers_guide_to_the_galaxy.quotes", this, genrater);
    }

    public String specie() {
        return genrater.fakeValuesService().resolve("hitchhikers_guide_to_the_galaxy.species", this, genrater);
    }

    public String starship() {
        return genrater.fakeValuesService().resolve("hitchhikers_guide_to_the_galaxy.starships", this, genrater);
    }
}
