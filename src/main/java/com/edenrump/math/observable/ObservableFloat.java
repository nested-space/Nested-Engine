package com.edenrump.math.observable;

/**
 * This class represents an observable float
 * <p>
 * It extends the ObservableNumber class and locks the generic value parameter to Float.
 *
 * @author Ed Eden-Rump
 */
public class ObservableFloat extends ObservableNumber<Float> {

    /**
     * Parameterised constructor sets the initial value.
     *
     * @param value initial value
     */
    public ObservableFloat(float value) {
        this.setValue(value);
    }


    /**
     * Unparameterised constructor leaves initial value as null.
     */
    public ObservableFloat() {
    }
}

