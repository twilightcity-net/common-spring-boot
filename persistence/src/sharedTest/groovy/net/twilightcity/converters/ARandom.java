package net.twilightcity.converters;

import lombok.experimental.Delegate;
import net.twilightcity.testsupport.RandomGenerator;

class ARandom {

    public static final ARandom aRandom = new ARandom();

    @Delegate
    private RandomGenerator randomGenerator;

    public ARandom() {
        randomGenerator = new RandomGenerator();
    }

}
