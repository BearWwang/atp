package com.snake.genrater;

public class Demographic {

    private final Genrater genrater;

    protected Demographic(Genrater genrater) {
        this.genrater = genrater;
    }

    public String race() {
        return genrater.fakeValuesService().fetchString("demographic.race");
    }

    public String educationalAttainment() {
        return genrater.fakeValuesService().fetchString("demographic.educational_attainment");
    }

    public String demonym() {
        return genrater.fakeValuesService().fetchString("demographic.demonym");
    }

    public String sex() {
        return genrater.fakeValuesService().fetchString("demographic.sex");
    }

    public String maritalStatus() {
        return genrater.fakeValuesService().fetchString("demographic.marital_status");
    }
}
