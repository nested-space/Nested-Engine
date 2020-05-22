package com.edenrump.math.arrays;

import java.nio.FloatBuffer;

public class ColumnVector {

    private int dimensions;
    private float[] values;

    public ColumnVector(int dimensions) {
        this.dimensions = dimensions;
        this.values = new float[dimensions];
    }

    public ColumnVector(float... values) {
        this.dimensions = values.length;
        this.values = values;
    }

    /**
     * Return a normalised vector of length 1.
     *
     * @return Normalized vector
     */
    public ColumnVector normalize() {
        return divide(length());
    }

    /**
     * Negate this vector.
     *
     * @return Negated vector
     */
    public ColumnVector negate() {
        return scale(-1f);
    }

    /**
     * Calculate the length of this vector.
     *
     * @return Length of this vector
     */
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    /**
     * Calculates the squared length of the vector.
     *
     * @return Squared length of this vector
     */
    public float lengthSquared() {
        float lengthSquared = 0;
        for (int i = 0; i < dimensions; i++) {
            lengthSquared += (values[i] * values[i]);
        }
        return lengthSquared;
    }

    /**
     * Adds this vector to another vector.
     *
     * @param other The other vector
     * @return Sum of this + other
     */
    public ColumnVector add(ColumnVector other) {
        if (other == null) return this;

        float[] addedValues = new float[other.dimensions];
        for (int i = 0; i < dimensions; i++) {
            addedValues[i] = (values[i] + other.values[i]);
        }
        return new ColumnVector(addedValues);
    }

    /**
     * Subtracts this vector from another vector.
     *
     * @param other The other vector
     * @return Difference of this - other
     */
    public ColumnVector subtract(ColumnVector other) {
        if (other == null) return this;

        return this.add(other.negate());
    }

    /**
     * Divides a vector by a scalar.
     *
     * @param scalar Scalar to multiply
     * @return Scalar quotient of this / scalar
     */
    public ColumnVector divide(float scalar) {
        if (scalar == 0) throw new ArithmeticException("Cannot properly divide by 0");
        return scale(1f / scalar);
    }

    /**
     * Multiplies a vector by a scalar.
     *
     * @param scalar Scalar to multiply
     * @return Scalar product of this * scalar
     */
    public ColumnVector scale(float scalar) {
        if (scalar == Float.POSITIVE_INFINITY || scalar == Float.NEGATIVE_INFINITY) throw new IllegalArgumentException(
                "Positive and negative infinity values cannot and should not be realistically handled with this program");


        float[] scaledValues = new float[dimensions];
        for (int i = 0; i < dimensions; i++) {
            scaledValues[i] = (values[i] * scalar);
        }
        return new ColumnVector(scaledValues);
    }

    /**
     * Calculates the dot product of this vector with another vector.
     *
     * @param other The other vector
     * @return Dot product of this * other
     */
    public float dot(ColumnVector other) {
        if (other.getDimensions() != dimensions)
            throw new IllegalArgumentException("Cannot calculate dot product for vectors of different length");

        float dotProduct = 0;
        for (int i = 0; i < dimensions; i++) {
            dotProduct += values[i] * other.getValues()[i];
        }
        return dotProduct;
    }

    /**
     * Calculates a linear interpolation between this vector with another
     * vector.
     *
     * @param other The other vector
     * @param alpha The alpha value, must be between 0.0 and 1.0
     * @return Linear interpolated vector
     */
    public ColumnVector lerp(ColumnVector other, float alpha) {
        if (other.containsInfinity() ||
                this.containsInfinity()) throw new IllegalArgumentException(
                "Cannot interpolate half way to infinity");
        return this.scale(1f - alpha).add(other.scale(alpha));
    }

    private boolean containsInfinity() {
        for (int i = 0; i < dimensions; i++) {
            if (values[i] == Float.POSITIVE_INFINITY || values[i] == Float.NEGATIVE_INFINITY) return true;
        }
        return false;
    }

    public float[] getValues() {
        return values;
    }

    /**
     * Stores the vector in a given Buffer.
     *
     * @param buffer The buffer to store the vector data
     */
    public void storeCoordinatesInBuffer(FloatBuffer buffer) {
        if (buffer == null)
            throw new IllegalArgumentException("Buffer storage not possible when buffer is null. Aborting.");
        if (buffer.remaining() < dimensions)
            throw new IllegalArgumentException("Buffer does not have sufficient space for this operation. Aborted.");

        for (int i = 0; i < dimensions; i++) {
            buffer.put(values[i]);
        }
    }

    public float getSquareDistanceToOther(ColumnVector other) {
        if (other.getDimensions() != dimensions)
            throw new IllegalArgumentException("Cannot calculate distance to vector with different number of dimensions");

        float squareDistance = 0;
        for (int i = 0; i < dimensions; i++) {
            squareDistance += Math.pow(values[i] - other.getValues()[i], 2);
        }
        return squareDistance;
    }

    public int getDimensions() {
        return dimensions;
    }

    public float getDistanceToOther(ColumnVector other) {
        return (float) Math.sqrt(getSquareDistanceToOther(other));
    }

    /**
     * Calculates the cross product of this vector with another vector.
     *
     * @param other The other vector
     * @return Cross product of this x other
     */
    public ColumnVector cross(ColumnVector other) {
        if (dimensions != 3 || other.getDimensions() != 3)
            throw new IllegalArgumentException("Cannot calculate cross product for non-3d vectors");

        float a = this.getValues()[1] * other.getValues()[2] - this.getValues()[2] * other.getValues()[1];
        float b = this.getValues()[2] * other.getValues()[0] - this.getValues()[0] * other.getValues()[2];
        float c = this.getValues()[0] * other.getValues()[1] - this.getValues()[1] * other.getValues()[0];

        return new ColumnVector(a, b, c);
    }


    @Override
    public int hashCode() {
        int hash = dimensions * 100003;
        for (int i = 0; i < dimensions; i++) {
            hash += (int) values[i] * 13;
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ColumnVector) {
            ColumnVector other = (ColumnVector) obj;
            for (int i = 0; i < dimensions; i++) {
                if (values[i] != other.values[i]) return false;
            }
            return true;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("Vector: (");
        for (int i = 0; i < dimensions; i++) {
            output.append(values[i]);
            if (i != dimensions - 1) output.append(", ");
        }

        return output.toString();
    }
}
