package com.snake.genrater;

public class Educator {
    private final Genrater genrater;

    protected Educator(Genrater genrater) {
        this.genrater = genrater;
    }

    // TODO - move these all out to en.yml by default. 
    public String university() {
        return genrater.fakeValuesService().resolve("educator.name", this, genrater)
                + " " 
                + genrater.fakeValuesService().resolve("educator.tertiary.type", this, genrater);
    }

    public String course() {
        return genrater.fakeValuesService().resolve("educator.tertiary.course.type", this, genrater)
                + " "
                + genrater.fakeValuesService().resolve("educator.tertiary.course.subject", this, genrater);
    }

    public String secondarySchool() {
        return genrater.fakeValuesService().resolve("educator.name", this, genrater)
                + " "
                + genrater.fakeValuesService().resolve("educator.secondary", this, genrater);
    }

    public String campus() {
        return genrater.fakeValuesService().resolve("educator.name", this, genrater) + " Campus";
    }

}
