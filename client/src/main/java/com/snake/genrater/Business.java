package com.snake.genrater;

public class Business {
    
    private final Genrater genrater;

    protected Business(Genrater genrater) {
        this.genrater = genrater;
    }

    public String creditCardNumber() {
        return genrater.fakeValuesService().resolve("business.credit_card_numbers", this, genrater);
    }

    public String creditCardType() {
        return genrater.fakeValuesService().resolve("business.credit_card_types", this, genrater);
    }

    public String creditCardExpiry() {
        return genrater.fakeValuesService().resolve("business.credit_card_expiry_dates", this, genrater);
    }
}
