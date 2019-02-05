package com.snake.genrater;

public class Address {
    private final Genrater genrater;

    protected Address(Genrater genrater) {
        this.genrater = genrater;
    }

    public String streetName() {
        return genrater.fakeValuesService().resolve("address.street_name", this, genrater);
    }

    public String streetAddressNumber() {
        return String.valueOf(genrater.random().nextInt(1000));
    }

    public String streetAddress() {
        return genrater.fakeValuesService().resolve("address.street_address", this, genrater);
    }

    public String streetAddress(boolean includeSecondary) {
        String streetAddress = genrater.fakeValuesService().resolve("address.street_address", this, genrater);
        if (includeSecondary) {
            streetAddress = streetAddress + " " + secondaryAddress();
        }
        return streetAddress;
    }

    public String secondaryAddress() {
        return genrater.numerify(genrater.fakeValuesService().resolve("address.secondary_address", this, genrater));
    }

    public String zipCode() {
        return genrater.bothify(genrater.fakeValuesService().resolve("address.postcode", this, genrater));
    }

    public String zipCodeByState(String stateAbbr) { return genrater.fakeValuesService().resolve("address.postcode_by_state." + stateAbbr, this, genrater); }

    public String streetSuffix() {
        return genrater.fakeValuesService().resolve("address.street_suffix", this, genrater);
    }

    public String streetPrefix() {
        return genrater.fakeValuesService().resolve("address.street_prefix", this, genrater);
    }

    public String citySuffix() {
        return genrater.fakeValuesService().resolve("address.city_suffix", this, genrater);
    }

    public String cityPrefix() {
        return genrater.fakeValuesService().resolve("address.city_prefix", this, genrater);
    }

    public String city() {
        return genrater.fakeValuesService().resolve("address.city", this, genrater);
    }

    public String cityName() {
        return genrater.fakeValuesService().resolve("address.city_name", this, genrater);
    }

    public String state() {
        return genrater.fakeValuesService().resolve("address.state", this, genrater);
    }

    public String stateAbbr() {
        return genrater.fakeValuesService().resolve("address.state_abbr", this, genrater);
    }

    public String firstName() {
        return genrater.name().firstName();
    }

    public String lastName() {
        return genrater.name().lastName();
    }

    public String latitude() {
        return String.format("%.8g", (genrater.random().nextDouble() * 180) - 90);
    }

    public String longitude() {
        return String.format("%.8g", (genrater.random().nextDouble() * 360) - 180);
    }

    public String timeZone() {
        return genrater.fakeValuesService().resolve("address.time_zone", this, genrater);
    }

    public String country() {
        return genrater.fakeValuesService().resolve("address.country", this, genrater);
    }

    public String countryCode() {
        return genrater.fakeValuesService().resolve("address.country_code", this, genrater);
    }

    public String buildingNumber() {
        return genrater.numerify(genrater.fakeValuesService().resolve("address.building_number", this, genrater));
    }

    public String fullAddress() {
        return genrater.fakeValuesService().resolve("address.full_address", this, genrater);
    }
}
