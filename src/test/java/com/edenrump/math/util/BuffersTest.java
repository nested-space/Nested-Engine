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
