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
