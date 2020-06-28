package com.edenrump.math.util;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * This class provides access to common data transfer and storage methods
 *
 * @author Ed Eden-Rump
 */
public class Buffers {

    /**
     * Return a FloatBuffer of the same length and contents as data
     * @param data the data to be stored
     * @return a FloatBuffer
     */
    public static FloatBuffer storeDataInBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    /**
     * Return a IntBuffer of the same length and contents as data
     * @param data the data to be stored
     * @return a IntBuffer
     */
    public static IntBuffer storeDataInBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
