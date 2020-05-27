package com.edenrump.graphic.math;

import org.lwjgl.system.MemoryStack;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.FloatBuffer;

public class std140SquareMatrixTest {

    Std140Compatible m2 = new std140SquareMatrix(2);
    Std140Compatible m3 = new std140SquareMatrix(3);
    Std140Compatible m4 = new std140SquareMatrix(4);

    Std140Compatible m4Square = new std140SquareMatrix(
            new std140ColumnVector(1, 0, 0),
            new std140ColumnVector(0, 1, 0),
            new std140ColumnVector(0, 0, 1)
    );

    @Test
    public void constructorTest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new std140SquareMatrix(1));
        Assert.assertThrows(IllegalArgumentException.class, () -> new std140SquareMatrix(5));
        Assert.assertThrows(IllegalArgumentException.class, std140SquareMatrix::new);

        Assert.assertEquals(m4Square, std140SquareMatrix.getIdentityMatrix(3));
    }

    @Test
    public void getStd140SizeTest() {
        Assert.assertEquals(m2.getStd140Size(), 8);
        Assert.assertEquals(m3.getStd140Size(), 12);
        Assert.assertEquals(m4.getStd140Size(), 16);
    }

    @Test
    public void getStd140BytesTest() {
        Assert.assertEquals(m2.getStd140Bytes(), 8 * Float.BYTES);
        Assert.assertEquals(m3.getStd140Bytes(), 12 * Float.BYTES);
        Assert.assertEquals(m4.getStd140Bytes(), 16 * Float.BYTES);

    }

    @Test
    public void getStd140DataTest() {
        Assert.assertEquals(m2.getStd140Data(), new float[]{1, 0, 0, 0, 0, 1, 0, 0});
        Assert.assertEquals(m3.getStd140Data(), new float[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0});
        Assert.assertEquals(m4.getStd140Data(), new float[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1});
    }

    @Test
    public void storeStd140DataInBufferTest() {
        FloatBuffer n2_manuallyCalculatedFloatBuffer;
        FloatBuffer n3_manuallyCalculatedFloatBuffer;
        FloatBuffer n4_manuallyCalculatedFloatBuffer;
        FloatBuffer n2_testFloatBuffer;
        FloatBuffer n3_testFloatBuffer;
        FloatBuffer n4_testFloatBuffer;
        FloatBuffer tooFewDimensions;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            n2_manuallyCalculatedFloatBuffer = stack.mallocFloat(8);
            n3_manuallyCalculatedFloatBuffer = stack.mallocFloat(12);
            n4_manuallyCalculatedFloatBuffer = stack.mallocFloat(16);
            n2_testFloatBuffer = stack.mallocFloat(8);
            n3_testFloatBuffer = stack.mallocFloat(12);
            n4_testFloatBuffer = stack.mallocFloat(16);

            tooFewDimensions = stack.mallocFloat(7);

            n2_manuallyCalculatedFloatBuffer.put(new float[]{1, 0, 0, 0, 0, 1, 0, 0});
            n3_manuallyCalculatedFloatBuffer.put(new float[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0});
            n4_manuallyCalculatedFloatBuffer.put(new float[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1});
        }
        m2.storeStd140DataInBuffer(n2_testFloatBuffer);
        Assert.assertEquals(n2_testFloatBuffer, n2_manuallyCalculatedFloatBuffer);

        m3.storeStd140DataInBuffer(n3_testFloatBuffer);
        Assert.assertEquals(n3_testFloatBuffer, n3_manuallyCalculatedFloatBuffer);

        m4.storeStd140DataInBuffer(n4_testFloatBuffer);
        Assert.assertEquals(n4_testFloatBuffer, n4_manuallyCalculatedFloatBuffer);

        Assert.assertThrows(IllegalArgumentException.class, () -> m2.storeStd140DataInBuffer(null));
        Assert.assertThrows(IllegalArgumentException.class, () -> m3.storeStd140DataInBuffer(tooFewDimensions));

    }

}
