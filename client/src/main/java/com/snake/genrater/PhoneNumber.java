package com.snake.genrater;

public class PhoneNumber {
    private final Genrater genrater;

    protected PhoneNumber(Genrater genrater) {
        this.genrater = genrater;
    }

    public String cellPhone() {
        return genrater.numerify(genrater.fakeValuesService().resolve("cell_phone.formats", this, genrater));
    }

    public String phoneNumber() {
        return genrater.numerify(genrater.fakeValuesService().resolve("phone_number.formats", this, genrater));
    }
}
