package com.edenrump.graphic.math;

import com.edenrump.math.arrays.SquareMatrix;

import java.nio.FloatBuffer;

public class glSquareMatrix extends SquareMatrix implements Std140Compatible {

    public glSquareMatrix(int dimensions) {
        super(dimensions);
        if (dimensions < 2 || dimensions > 4) {
            throw new IllegalArgumentException("Vectors of " + getDimensions() + " dimensions are not compatible with" +
                    " GLSL datatypes of mat2, mat3 and mat4");
        }
    }

    public glSquareMatrix(glColumnVector... columns) {
        super(columns);
        if (columns.length < 2 || columns.length > 4) {
            throw new IllegalArgumentException("Square matrices of " + getDimensions() + " dimensions are not compatible with" +
                    " GLSL datatypes of mat2, mat3 and mat4");
        }
    }

    private int getStd140Dimensions() {
        if (getDimensions() == 4 || getDimensions() == 3 || getDimensions() == 2) {
            return getDimensions();
        } else {
            throw new IllegalStateException("Vectors of " + getDimensions() + " dimensions are not compatible with" +
                    " GLSL datatypes of mat2, mat3 and mat4");
        }
    }

    @Override
    public int getStd140Size() {
        return getStd140Dimensions() * 4; //in GLSL all elements rounded to the size of a vec[4] so 4 elements per dimension
    }

    @Override
    public int getStd140Bytes() {
        return getStd140Size() * Float.BYTES;
    }

    @Override
    public float[] getStd140Data() {
        int valuesPerColumn = getStd140Dimensions();
        int std140padding = 4 - getStd140Dimensions();

        float[] data = new float[getStd140Size()];

        int transcriptionCaret = 0;
        for (int i = 0; i < getDimensions(); i++) {
            for (int j = 0; j < valuesPerColumn; j++) {
                data[i * 4 + j] = getValues()[transcriptionCaret];
                transcriptionCaret++;
            }
            for (int j = 0; j < std140padding; j++) {
                data[i * 4 + j + valuesPerColumn] = 0;
            }
        }
        return data;
    }

    @Override
    public void storeStd140DataInBuffer(FloatBuffer buffer) {
        if (buffer == null)
            throw new IllegalArgumentException("Buffer storage not possible when buffer is null. Aborting.");
        if (buffer.remaining() < getStd140Size())
            throw new IllegalArgumentException("Buffer does not have sufficient space for this operation. Aborted.");

        buffer.put(getStd140Data());
    }
}
