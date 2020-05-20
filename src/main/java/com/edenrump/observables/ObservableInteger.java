package com.edenrump.observables;

/**
 * This class represents an observable float
 * <p>
 * It extends the ObservableNumber class and locks the generic value parameter to Integer.
 *
 * @author Ed Eden-Rump
 */

public class ObservableInteger extends ObservableNumber<Integer> {
    /**
     * Parameterised constructor sets the initial value.
     *
     * @param value initial value
     */
    public ObservableInteger(int value) {
        this.setValue(value);
    }


    /**
     * Unparameterised constructor leaves initial value as null.
     */
    public ObservableInteger() {
    }


}
