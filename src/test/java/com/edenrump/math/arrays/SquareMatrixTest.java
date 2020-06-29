package com.edenrump.math.arrays;

import org.lwjgl.system.MemoryStack;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.FloatBuffer;

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

public class SquareMatrixTest {
    final SquareMatrix squareMatrix_n2_1 = new SquareMatrix(new float[]{1, 2, 3, 4});
    final SquareMatrix squareMatrix_n2_2 = new SquareMatrix(new float[]{2, 4, 6, 8});
    final SquareMatrix squareMatrix_n3_1 = new SquareMatrix(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
    final SquareMatrix squareMatrix_n3_2 = new SquareMatrix(new float[]{2, 4, 6, 8, 10, 12, 14, 16, 18});

    @Test
    public void constructorTest() {
        Assert.assertEquals(squareMatrix_n2_1,
                new SquareMatrix(new ColumnVector(1, 2), new ColumnVector(3, 4)));

        Assert.assertEquals(new SquareMatrix(new float[]{1, 0, 0, 1}), new SquareMatrix(2));

        Assert.assertThrows(IllegalArgumentException.class, () -> new SquareMatrix(new float[]{1, 2, 3}));
    }

    @Test
    public void identityTest() {
        Assert.assertEquals(SquareMatrix.getIdentityMatrix(2), new SquareMatrix(new float[]{1, 0, 0, 1}));
        Assert.assertEquals(SquareMatrix.getIdentityMatrix(3), new SquareMatrix(new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1}));
    }

    @Test
    public void addTest() {
        Assert.assertEquals(squareMatrix_n2_2.add(squareMatrix_n2_1), new SquareMatrix(new float[]{3, 6, 9, 12}));
        Assert.assertEquals(squareMatrix_n3_1.add(squareMatrix_n3_1), squareMatrix_n3_2);

        Assert.assertThrows(IllegalArgumentException.class, () -> squareMatrix_n2_1.add(squareMatrix_n3_1));
        Assert.assertThrows(IllegalArgumentException.class, () -> squareMatrix_n3_1.add(squareMatrix_n2_1));
    }

    @Test
    public void negateTest() {
        Assert.assertEquals(squareMatrix_n2_1.negate(), new SquareMatrix(new float[]{-1, -2, -3, -4}));
        Assert.assertEquals(squareMatrix_n3_1.negate(),
                new SquareMatrix(new float[]{-1, -2, -3, -4, -5, -6, -7, -8, -9}));
    }

    @Test
    public void subtractTest() {
        Assert.assertEquals(squareMatrix_n2_1.subtract(squareMatrix_n2_1), new SquareMatrix(new float[]{0, 0, 0, 0}));
        Assert.assertEquals(squareMatrix_n2_2.subtract(squareMatrix_n2_1), new SquareMatrix(new float[]{1, 2, 3, 4}));
        Assert.assertEquals(squareMatrix_n3_2.subtract(squareMatrix_n3_1), squareMatrix_n3_1);
    }

    @Test
    public void multiplyTest() {
        Assert.assertEquals(squareMatrix_n2_1.multiply(2), squareMatrix_n2_2);
        Assert.assertEquals(squareMatrix_n2_2.multiply(new ColumnVector(2, 2)), new ColumnVector(16, 24));
        Assert.assertEquals(squareMatrix_n2_1.multiply(squareMatrix_n2_2), new SquareMatrix(new float[]{14, 20, 30, 44}));

        Assert.assertEquals(squareMatrix_n3_1.multiply(2), squareMatrix_n3_2);
        Assert.assertEquals(squareMatrix_n3_1.multiply(new ColumnVector(2, 2, 2)), new ColumnVector(24, 30, 36));
        Assert.assertEquals(squareMatrix_n3_1.multiply(squareMatrix_n3_2),
                new SquareMatrix(new float[]{60, 72, 84, 132, 162, 192, 204, 252, 300}));

        Assert.assertThrows(IllegalArgumentException.class, () -> squareMatrix_n2_1.multiply(new ColumnVector(1)));
        Assert.assertThrows(IllegalArgumentException.class, () -> squareMatrix_n3_1.multiply(new ColumnVector(2)));
        Assert.assertThrows(IllegalArgumentException.class, () -> squareMatrix_n3_1.multiply(squareMatrix_n2_1));
    }

    @Test
    public void transposeTest() {
        Assert.assertEquals(squareMatrix_n2_1.transpose(), new SquareMatrix(new float[]{1, 3, 2, 4}));
        Assert.assertEquals(squareMatrix_n3_1.transpose(), new SquareMatrix(new float[]{1, 4, 7, 2, 5, 8, 3, 6, 9}));
    }

    @Test
    public void storeInBufferTest() {
        FloatBuffer n2_manuallyCalculatedFloatBuffer;
        FloatBuffer n3_manuallyCalculatedFloatBuffer;
        FloatBuffer n2_testFloatBuffer;
        FloatBuffer n3_testFloatBuffer;
        FloatBuffer tooFewDimensions;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            n2_manuallyCalculatedFloatBuffer = stack.mallocFloat(4);
            n3_manuallyCalculatedFloatBuffer = stack.mallocFloat(9);
            n2_testFloatBuffer = stack.mallocFloat(4);
            n3_testFloatBuffer = stack.mallocFloat(9);

            tooFewDimensions = stack.mallocFloat(3);

            n2_manuallyCalculatedFloatBuffer.put(1).put(2).put(3).put(4);

            n3_manuallyCalculatedFloatBuffer.put(1).put(2).put(3);
            n3_manuallyCalculatedFloatBuffer.put(4).put(5).put(6);
            n3_manuallyCalculatedFloatBuffer.put(7).put(8).put(9);
        }
        squareMatrix_n2_1.storeMatrixInBuffer(n2_testFloatBuffer);
        Assert.assertEquals(n2_testFloatBuffer, n2_manuallyCalculatedFloatBuffer);

        squareMatrix_n3_1.storeMatrixInBuffer(n3_testFloatBuffer);
        Assert.assertEquals(n3_testFloatBuffer, n3_manuallyCalculatedFloatBuffer);

        Assert.assertThrows(IllegalArgumentException.class, () -> squareMatrix_n2_2.storeMatrixInBuffer(null));
        Assert.assertThrows(IllegalArgumentException.class, () -> squareMatrix_n2_2.storeMatrixInBuffer(tooFewDimensions));
    }


}
