package com.edenrump.math.arrays;

import org.lwjgl.system.MemoryStack;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.FloatBuffer;

public class SquareMatrixTest {
    SquareMatrix squareMatrix_n2_1 = new SquareMatrix(new float[]{1, 2, 3, 4});
    SquareMatrix squareMatrix_n2_2 = new SquareMatrix(new float[]{2, 4, 6, 8});
    SquareMatrix squareMatrix_n3_1 = new SquareMatrix(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
    SquareMatrix squareMatrix_n3_2 = new SquareMatrix(new float[]{2, 4, 6, 8, 10, 12, 14, 16, 18});

    @Test
    public void constructorTest() {
        Assert.assertEquals(squareMatrix_n2_1,
                new SquareMatrix(new ColumnVector(1, 2), new ColumnVector(3, 4)));

        Assert.assertEquals(new SquareMatrix(new float[]{0, 0, 0, 0}), new SquareMatrix(2));

        Assert.assertThrows(IllegalArgumentException.class, () -> new SquareMatrix(new float[]{1, 2, 3}));
    }

    @Test
    public void identityTest(){
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
        Assert.assertEquals(squareMatrix_n2_1.subtract(squareMatrix_n2_1), new SquareMatrix(2));
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
            n2_manuallyCalculatedFloatBuffer.flip();

            n3_manuallyCalculatedFloatBuffer.put(1).put(2).put(3);
            n3_manuallyCalculatedFloatBuffer.put(4).put(5).put(6);
            n3_manuallyCalculatedFloatBuffer.put(7).put(8).put(9);
            n3_manuallyCalculatedFloatBuffer.flip();
        }
        squareMatrix_n2_1.storeMatrixInBuffer(n2_testFloatBuffer);
        Assert.assertEquals(n2_testFloatBuffer, n2_manuallyCalculatedFloatBuffer);

        squareMatrix_n3_1.storeMatrixInBuffer(n3_testFloatBuffer);
        Assert.assertEquals(n3_testFloatBuffer, n3_manuallyCalculatedFloatBuffer);

        Assert.assertThrows(IllegalArgumentException.class, () -> squareMatrix_n2_2.storeMatrixInBuffer(null));
        Assert.assertThrows(IllegalArgumentException.class, () -> squareMatrix_n2_2.storeMatrixInBuffer(tooFewDimensions));
    }


}
