package com.edenrump.graphic.math;

import org.lwjgl.system.MemoryStack;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.FloatBuffer;

public class glColumnVectorTest {

    Std140Compatible v2 = new glColumnVector(1, 0);
    Std140Compatible v3 = new glColumnVector(1, 2, 3);
    Std140Compatible v4 = new glColumnVector(1, 2, 3, 4);

    @Test
    public void constructorTest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new glColumnVector(1));
        Assert.assertThrows(IllegalArgumentException.class, () -> new glColumnVector(5));
        Assert.assertThrows(IllegalArgumentException.class, glColumnVector::new);
        Assert.assertThrows(IllegalArgumentException.class, () -> new glColumnVector(1));
        Assert.assertThrows(IllegalArgumentException.class, () -> new glColumnVector(1, 2, 3, 4, 5));
    }

    @Test
    private void getStd140SizeTest() {
        Assert.assertEquals(v2.getStd140Size(), 2);
        Assert.assertEquals(v3.getStd140Size(), 4);
        Assert.assertEquals(v4.getStd140Size(), 4);
    }

    @Test
    private void getStd140BytesTest() {
        Assert.assertEquals(v2.getStd140Bytes(), 2 * Float.BYTES);
        Assert.assertEquals(v3.getStd140Bytes(), 4 * Float.BYTES);
        Assert.assertEquals(v4.getStd140Bytes(), 4 * Float.BYTES);
    }

    @Test
    public void getStd140DataTest() {
        Assert.assertEquals(v2.getStd140Data(), new float[]{1, 0});
        Assert.assertEquals(v3.getStd140Data(), new float[]{1, 2, 3, 0});
        Assert.assertEquals(v4.getStd140Data(), new float[]{1, 2, 3, 4});
    }

    @Test
    private void storeStd140DataInBufferTest() {
        FloatBuffer v2_manuallyCalculatedFloatBuffer;
        FloatBuffer v3_manuallyCalculatedFloatBuffer;
        FloatBuffer v4_manuallyCalculatedFloatBuffer;
        FloatBuffer v2_testFloatBuffer;
        FloatBuffer v3_testFloatBuffer;
        FloatBuffer v4_testFloatBuffer;
        FloatBuffer tooFewDimensions;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            v2_manuallyCalculatedFloatBuffer = stack.mallocFloat(2);
            v2_testFloatBuffer = stack.mallocFloat(2);
            v3_manuallyCalculatedFloatBuffer = stack.mallocFloat(4);
            v3_testFloatBuffer = stack.mallocFloat(4);
            v4_manuallyCalculatedFloatBuffer = stack.mallocFloat(4);
            v4_testFloatBuffer = stack.mallocFloat(4);
            tooFewDimensions = stack.mallocFloat(3);

            v2_manuallyCalculatedFloatBuffer.put(new float[]{1, 0});
            v3_manuallyCalculatedFloatBuffer.put(new float[]{1, 2, 3, 0});
            v4_manuallyCalculatedFloatBuffer.put(new float[]{1, 2, 3, 4});
        }

        v2.storeStd140DataInBuffer(v2_testFloatBuffer);
        Assert.assertEquals(v2_testFloatBuffer, v2_manuallyCalculatedFloatBuffer);

        v3.storeStd140DataInBuffer(v3_testFloatBuffer);
        Assert.assertEquals(v3_testFloatBuffer, v3_manuallyCalculatedFloatBuffer);

        v4.storeStd140DataInBuffer(v4_testFloatBuffer);
        Assert.assertEquals(v4_testFloatBuffer, v4_manuallyCalculatedFloatBuffer);

        Assert.assertThrows(IllegalArgumentException.class, () -> v2.storeStd140DataInBuffer(null));
        Assert.assertThrows(IllegalArgumentException.class, () -> v3.storeStd140DataInBuffer(tooFewDimensions));
    }

}
