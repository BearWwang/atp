package com.snake.genrater;

public class HowIMetYourMother {
    private final Genrater genrater;

    protected HowIMetYourMother(Genrater genrater) {
        this.genrater = genrater;
    }

    public String character() {
        return genrater.fakeValuesService().resolve("how_i_met_your_mother.character", this, genrater);
    }

    public String catchPhrase() {
        return genrater.fakeValuesService().resolve("how_i_met_your_mother.catch_phrase", this, genrater);
    }

    public String highFive() {
        return genrater.fakeValuesService().resolve("how_i_met_your_mother.high_five", this, genrater);
    }

    public String quote() {
        return genrater.fakeValuesService().resolve("how_i_met_your_mother.quote", this, genrater);
    }
}
