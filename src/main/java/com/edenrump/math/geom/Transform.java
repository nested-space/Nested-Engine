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

package com.edenrump.math.geom;

import com.edenrump.math.arrays.ColumnVector;
import com.edenrump.math.arrays.SquareMatrix;
import com.edenrump.math.util.Volume;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class Transform {

    private ColumnVector translation;
    private ColumnVector rotation;
    private ColumnVector scale;

    public Transform() {
        translation = new ColumnVector(4);
        rotation = new ColumnVector(4);
        scale = new ColumnVector(1, 1, 1, 0);
    }

    public float[] getTranslation() {
        return translation.getValues();
    }

    public float[] getRotation() {
        return rotation.getValues();
    }

    public float[] getScale() {
        return scale.getValues();
    }

    public void rotate(float x, float y, float z) {
        rotation = rotation.add(new ColumnVector(x, y, z, 0));
    }

    public void scale(float x, float y, float z) {
        float[] oldScale = scale.getValues();
        scale = new ColumnVector(
                x * oldScale[0],
                y * oldScale[1],
                z * oldScale[2],
                1);
    }

    public void translate(float x, float y, float z) {
        translation = translation.add(new ColumnVector(x, y, z, 0));
    }

    public FloatBuffer getTransformationMatrix() {
        SquareMatrix transformationMatrix = new SquareMatrix(4);

        SquareMatrix txyz = Volume.createTranslationMatrix(translation);
        SquareMatrix rx = Volume.createRotationMatrix(rotation.getValues()[0], 1, 0, 0);
        SquareMatrix ry = Volume.createRotationMatrix(rotation.getValues()[1], 0, 1, 0);
        SquareMatrix rz = Volume.createRotationMatrix(rotation.getValues()[2], 0, 0, 1);
        SquareMatrix s = Volume.createScaleMatrix(scale.getValues()[0], scale.getValues()[1], scale.getValues()[2]);
        transformationMatrix = transformationMatrix.multiply(s).multiply(rx).multiply(ry).multiply(rz).multiply(txyz);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        transformationMatrix.storeMatrixInBuffer(buffer);
        buffer.flip();
        return buffer;
    }
}
