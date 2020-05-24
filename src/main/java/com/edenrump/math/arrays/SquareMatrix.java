package com.edenrump.math.arrays;

import java.nio.FloatBuffer;

public class SquareMatrix {

    private int dimensions;
    private float[] values;

    public SquareMatrix(int dimensions) {
        this.dimensions = dimensions;
        values = getIdentityMatrix(dimensions).getValues();
    }

    public SquareMatrix(float[] initialValues) {
        if (!isSquareNumber(initialValues.length))
            throw new IllegalArgumentException("Cannot create square matrix from " + initialValues.length + " values");

        dimensions = (int) Math.sqrt(initialValues.length);
        values = initialValues;
    }

    public SquareMatrix(ColumnVector... columns) {
        for (ColumnVector vector : columns) {
            if (columns.length != vector.getDimensions())
                throw new IllegalArgumentException("Cannot create square matrix from " + columns.length
                        + " column vectors with length " + vector.getDimensions());
        }

        dimensions = columns.length;
        values = new float[dimensions * dimensions];
        for (int i = 0; i < columns.length; i++) {
            System.arraycopy(columns[i].getValues(), 0, values, i * dimensions, dimensions);
        }
    }

    /**
     * Sets this matrix to the identity matrix.
     */
    public static SquareMatrix getIdentityMatrix(int dimensions) {
        float[] values = new float[dimensions * dimensions];
        for (int column = 0; column < dimensions; column++) {
            for (int row = 0; row < dimensions; row++) {
                if (column == row) {
                    values[column * dimensions + row] = 1;
                } else {
                    values[column * dimensions + row] = 0;
                }
            }
        }
        return new SquareMatrix(values);
    }

    private static boolean isSquareNumber(float number) {
        double sr = Math.sqrt(number);
        return ((sr - Math.floor(sr)) == 0);
    }

    public int getDimensions() {
        return dimensions;
    }

    public float[] getValues() {
        return values;
    }

    /**
     * Adds this matrix to another matrix.
     *
     * @param other The other matrix
     * @return Sum of this + other
     */
    public SquareMatrix add(SquareMatrix other) {
        if (other.getDimensions() != getDimensions())
            throw new IllegalArgumentException("Cannot add two matrices of different shapes");

        float[] addedValues = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            addedValues[i] = this.values[i] + other.getValues()[i];
        }
        return new SquareMatrix(addedValues);
    }

    /**
     * Negates this matrix.
     *
     * @return Negated matrix
     */
    public SquareMatrix negate() {
        return multiply(-1f);
    }

    /**
     * Subtracts this matrix from another matrix.
     *
     * @param other The other matrix
     * @return Difference of this - other
     */
    public SquareMatrix subtract(SquareMatrix other) {
        return this.add(other.negate());
    }

    /**
     * Multiplies this matrix with a scalar.
     *
     * @param scalar The scalar
     * @return Scalar product of this * scalar
     */
    public SquareMatrix multiply(float scalar) {
        float[] multipliedValues = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            multipliedValues[i] = values[i] * scalar;
        }
        return new SquareMatrix(multipliedValues);
    }

    /**
     * Multiplies this matrix to a vector.
     *
     * @param vector The vector
     * @return Vector product of this * other
     */
    public ColumnVector multiply(ColumnVector vector) {
        if (dimensions != vector.getDimensions())
            throw new IllegalArgumentException("Cannot multiple mismatched matrix/vector pairs");

        float[] result = new float[dimensions];
        for (int row = 0; row < dimensions; row++) {
            for (int column = 0; column < vector.getDimensions(); column++) {
                result[row] += values[column * dimensions + row] * vector.getValues()[column];
            }
        }
        return new ColumnVector(result);
    }

    /**
     * Multiplies this matrix to another matrix.
     *
     * @param other The other matrix
     * @return Matrix product of this * other
     */
    public SquareMatrix multiply(SquareMatrix other) {
        if (other.getDimensions() != this.getDimensions())
            throw new IllegalArgumentException("Cannot multiply together two matrices of different size");

        float[] result = new float[dimensions * dimensions];

        for (int row1 = 0; row1 < dimensions; row1++) {
            for (int columnOther = 0; columnOther < dimensions; columnOther++) {
                for (int column1 = 0; column1 < dimensions; column1++) {
                    result[row1 * dimensions + columnOther] +=
                            values[row1 * dimensions + column1] * other.values[column1 * dimensions + columnOther];
                }
            }
        }

        return new SquareMatrix(result);
    }

    /**
     * Transposes this matrix.
     *
     * @return Transposed matrix
     */
    public SquareMatrix transpose() {
        float[] result = new float[dimensions * dimensions];
        for (int column = 0; column < dimensions; column++) {
            for (int row = 0; row < dimensions; row++) {
                result[row * dimensions + column] = values[column * dimensions + row];
            }
        }
        return new SquareMatrix(result);
    }

    /**
     * Stores the matrix in a given Buffer.
     *
     * @param buffer The buffer to store the matrix data
     */
    public void storeMatrixInBuffer(FloatBuffer buffer) {
        if (buffer == null)
            throw new IllegalArgumentException("Buffer storage not possible when buffer is null. Aborting.");
        if (buffer.capacity() - buffer.position() < values.length)
            throw new IllegalArgumentException("Buffer does not have sufficient space for this operation. Aborted.");

        buffer.put(values);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("Matrix: \n(");
        for (int row = 0; row < dimensions; row++) {
            for (int col = 0; col < dimensions; col++) {
                output.append(values[col * dimensions + row]);
                if (col == dimensions - 1 && row == dimensions - 1) {
                    output.append(")");
                } else if (col == dimensions - 1) {
                    output.append(")\n(");
                } else {
                    output.append(", ");
                }
            }
        }
        return output.toString();
    }

    @Override
    public int hashCode() {
        int hash = dimensions * 100003;
        for (int i = 0; i < values.length; i++) {
            hash += (int) values[i] * 13;
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SquareMatrix) {
            SquareMatrix other = (SquareMatrix) obj;

            if (values.length != other.getValues().length) return false;

            for (int i = 0; i < values.length; i++) {
                if (values[i] != other.getValues()[i]) return false;
            }
            return true;
        }
        return super.equals(obj);
    }


}
