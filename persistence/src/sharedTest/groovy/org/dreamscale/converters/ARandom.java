package org.dreamscale.converters;

import lombok.experimental.Delegate;
import org.dreamscale.testsupport.RandomGenerator;

class ARandom {

    public static final ARandom aRandom = new ARandom();

    @Delegate
    private RandomGenerator randomGenerator;

    public ARandom() {
        randomGenerator = new RandomGenerator();
    }

}
