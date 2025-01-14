package com.snake.genrater;

import java.math.BigDecimal;

public class Number {
    private final Genrater genrater;

    protected Number(Genrater genrater) {
        this.genrater = genrater;
    }

    /**
     * Returns a random number from 0-9 (both inclusive)
     */
    public int randomDigit() {
        return decimalBetween(0,10).intValue();
    }

    /**
     * Returns a random number from 1-9 (both inclusive)
     */
    public int randomDigitNotZero() {
        return decimalBetween(1,10).intValue();
    }

    /**
     * @see Number#numberBetween(long, long) 
     */
    public int numberBetween(int min, int max) {
        return decimalBetween(min,max).setScale(0, BigDecimal.ROUND_HALF_DOWN).intValue();
    }

    /**
     * Return a number between <em>min</em> and <em>max</em>.  If 
     * min == max, then min is returned. So numberBetween(2,2) will yield 2 even though
     * it doesn't make sense.
     *
     * @param min inclusive
     * @param max exclusive (unless min == max)
     */
    public long numberBetween(long min, long max) {
        return decimalBetween(min,max).longValue();
    }
    
    /**
     * @param numberOfDigits the number of digits the generated value should have
     * @param strict         whether or not the generated value should have exactly <code>numberOfDigits</code>
     */
    public long randomNumber(int numberOfDigits, boolean strict) {
        long max = (long) Math.pow(10, numberOfDigits);
        if (strict) {
            long min = (long) Math.pow(10, numberOfDigits - 1);
            return genrater.random().nextLong(max - min) + min;
        }

        return genrater.random().nextLong(max);
    }

    /**
     * Returns a random number
     */
    public long randomNumber() {
        int numberOfDigits = decimalBetween(1,10).intValue();
        return randomNumber(numberOfDigits, false);
    }

    public double randomDouble(int maxNumberOfDecimals, int min, int max) {
        return randomDouble(maxNumberOfDecimals,(long) min, (long) max);
    }
    /**
     * Returns a random double
     *
     * @param maxNumberOfDecimals maximum number of places
     * @param min                 minimum value
     * @param max                 maximum value
     */
    public double randomDouble(int maxNumberOfDecimals, long min, long max) {
        return decimalBetween(min,max)
                .setScale(maxNumberOfDecimals, BigDecimal.ROUND_HALF_DOWN)
                .doubleValue();
    }

    /**
     * @param min inclusive
     * @param max exclusive
     * @return
     */
    private BigDecimal decimalBetween(long min, long max) {
        if (min == max) {
            return new BigDecimal(min);
        }
        final long trueMin = Math.min(min, max);
        final long trueMax = Math.max(min, max);

        final double range = (double) trueMax - (double) trueMin;
        
        final double chunkCount = Math.sqrt(Math.abs(range));
        final double chunkSize = chunkCount;
        final long randomChunk = genrater.random().nextLong((long) chunkCount);

        final double chunkStart = trueMin + randomChunk * chunkSize;
        final double adj = chunkSize * genrater.random().nextDouble();
        return new BigDecimal(chunkStart + adj);
    }

    public String digits(int count) {
        final StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < count; i++) {
            tmp.append(randomDigit());
        }
        return tmp.toString();
    }

    public String digit() {
        return digits(1);
    }
}
