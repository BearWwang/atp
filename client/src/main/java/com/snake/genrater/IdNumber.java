package com.snake.genrater;

import com.snake.genrater.idnumbers.EnIdNumber;
import com.snake.genrater.idnumbers.SvSEIdNumber;

public class IdNumber {
    private final Genrater genrater;

    protected IdNumber(Genrater genrater) {
        this.genrater = genrater;
    }

    public String valid() {
        return genrater.fakeValuesService().resolve("id_number.valid", this, genrater);
    }

    public String invalid() {
        return genrater.numerify(genrater.fakeValuesService().resolve("id_number.invalid", this, genrater));
    }

    public String ssnValid() {
        EnIdNumber enIdNumber = new EnIdNumber();
        return enIdNumber.getValidSsn(genrater);
    }

    /**
     * Specified as #{IDNumber.valid_sv_se_ssn} in sv-SE.yml
     */
    public String validSvSeSsn() {
        SvSEIdNumber svSEIdNumber = new SvSEIdNumber();
        return svSEIdNumber.getValidSsn(genrater);
    }

    /**
     * Specified as #{IDNumber.invalid_sv_se_ssn} in sv-SE.yml
     */
    public String invalidSvSeSsn() {
        SvSEIdNumber svSEIdNumber = new SvSEIdNumber();
        return svSEIdNumber.getInvalidSsn(genrater);
    }
}
