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

package com.edenrump.gpu.data;

import com.edenrump.math.arrays.ColumnVector;

import java.nio.FloatBuffer;

public class std140ColumnVector extends ColumnVector implements Std140Compatible {

    private std140ColumnVector() {
    }

    public std140ColumnVector(int size) {
        super(size);
        if (size < 2 || size > 4) {
            throw new IllegalArgumentException("Vectors of " + getDimensions() + " dimensions are not compatible with" +
                    " GLSL datatypes of vec2, vec3 and vec4");
        }
    }

    public std140ColumnVector(float... values) {
        super(values);
        if (values.length < 2 || values.length > 4) {
            throw new IllegalArgumentException("Square matrices of " + getDimensions() + " dimensions are not compatible with" +
                    " GLSL datatypes of vec2, vec3 and vec4");
        }
    }

    @Override
    public int getStd140Size() {
        if (getDimensions() == 4 || getDimensions() == 2) {
            return getDimensions();
        } else if (getDimensions() == 3) {
            return 4;
        } else {
            throw new IllegalStateException("Vectors of " + getDimensions() + " dimensions are not compatible with" +
                    " GLSL datatypes of vec2, vec3 and vec4");
        }
    }

    @Override
    public int getStd140Bytes() {
        return getStd140Size() * Float.BYTES;
    }

    @Override
    public float[] getStd140Data() {
        float[] data = new float[getStd140Size()];
        System.arraycopy(getValues(), 0, data, 0, getValues().length);
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
