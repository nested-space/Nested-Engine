package com.edenrump.math.observable;

/*
 * Copyright (c) 2020 Ed Eden-Rump
 *     This file is part of Nested Engine.
 *
 *     Nested Engine is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Nested Engine is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Nested Engine.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     @Author Ed Eden-Rump
 *
 */

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

