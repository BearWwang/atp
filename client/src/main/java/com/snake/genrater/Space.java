package com.snake.genrater;

public class Space {

    private final Genrater genrater;

    protected Space(Genrater genrater) {
        this.genrater = genrater;
    }

    public String planet() {
        return genrater.resolve("space.planet");
    }

    public String moon() {
        return genrater.resolve("space.moon");
    }

    public String galaxy() {
        return genrater.resolve("space.galaxy");
    }

    public String nebula() {
        return genrater.resolve("space.nebula");
    }

    public String starCluster() {
        return genrater.resolve("space.star_cluster");
    }

    public String constellation() {
        return genrater.resolve("space.constellation");
    }

    public String star() {
        return genrater.resolve("space.star");
    }

    public String agency() {
        return genrater.resolve("space.agency");
    }

    public String agencyAbbreviation() {
        return genrater.resolve("space.agency_abv");
    }

    public String nasaSpaceCraft() {
        return genrater.resolve("space.nasa_space_craft");
    }

    public String company() {
        return genrater.resolve("space.company");
    }

    public String distanceMeasurement() {
        return genrater.number().numberBetween(10, 100) + ' ' + genrater.resolve("space.distance_measurement");
    }

    public String meteorite() {
        return genrater.resolve("space.meteorite");
    }
}
