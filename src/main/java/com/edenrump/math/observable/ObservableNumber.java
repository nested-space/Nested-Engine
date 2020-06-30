/*
 * Copyright (c) 2020 Ed Eden-Rump
 *
 * This file is part of Nested Engine.
 *
 * Nested Engine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nested Engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Nested Engine.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.edenrump.math.observable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * An ObservableValue is an entity that wraps a value and allows to observe the value for changes.
 * In general this interface should not be implemented directly but one of its sub-interfaces (ObservableFloat etc.).
 *
 * @param <N> generic value
 * @author Ed Eden-Rump
 */
public abstract class ObservableNumber<N> {

    public static final String PROPERTY_NAME = "Number";

    private N value;

    private final PropertyChangeSupport support;

    public ObservableNumber() {
        support = new PropertyChangeSupport(this);
    }

    public void addListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public N getValue() {
        return value;
    }

    /**
     * This method sets the variable "value".
     * <p>
     * It fires the PCS with an appropriate name, and then set the Number to
     * the <code>value</code>
     *
     * @param value the new value to be set
     */
    public void setValue(N value) {
        support.firePropertyChange(PROPERTY_NAME, this.value, value);
        this.value = value;
    }
}
