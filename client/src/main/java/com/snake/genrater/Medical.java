package com.snake.genrater;

public class Medical {

    private final Genrater genrater;

    protected Medical(Genrater genrater) {
        this.genrater = genrater;
    }

    public String medicineName() {
        return genrater.fakeValuesService().resolve("medical.medicine_name", this, genrater);
    }

    public String diseaseName() {
        return genrater.fakeValuesService().resolve("medical.disease_name", this, genrater);
    }

    public String hospitalName() {
        return genrater.fakeValuesService().resolve("medical.hospital_name", this, genrater);
    }

    public String symptoms() {
        return genrater.fakeValuesService().resolve("medical.symptoms", this, genrater);
    }
}
