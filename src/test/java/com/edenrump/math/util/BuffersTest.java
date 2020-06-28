package com.edenrump.math.util;

import org.lwjgl.BufferUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * This class provides access to common data transfer and storage methods
 *
 * @author Ed Eden-Rump
 */
public class BuffersTest {

    static float[] floats = new float[]{
            0.5f, 1.5f, 2.5f, 3.5f, 4.5f, 5.5f, 6.5f, 7.5f, 8.5f, 9.5f
    };

    static int[] ints = new int[]{
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9
    };

    @Test
    public static void storeFloatDataInBuffer() {
        FloatBuffer manualBuffer = BufferUtils.createFloatBuffer(floats.length);
        manualBuffer.put(floats);
        manualBuffer.flip();

        FloatBuffer testBuffer = Buffers.storeDataInBuffer(floats);
        Assert.assertEquals(manualBuffer, testBuffer);
    }

    @Test
    public static void storeIntDataInBuffer() {
        IntBuffer manualBuffer = BufferUtils.createIntBuffer(ints.length);
        manualBuffer.put(ints);
        manualBuffer.flip();

        IntBuffer testBuffer = Buffers.storeDataInBuffer(ints);
        Assert.assertEquals(manualBuffer, testBuffer);
    }
}
