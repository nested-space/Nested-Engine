package com.edenrump.graphic.data;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
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

public interface Std140Compatible {

    int getStd140Size();

    int getStd140Bytes();

    float[] getStd140Data();

    void storeStd140DataInBuffer(FloatBuffer buffer);

    static ByteBuffer putAllInBuffer(Std140Compatible... data) {
        int size = 0;
        for (Std140Compatible datum : data) {
            size += datum.getStd140Bytes();
        }

        ByteBuffer buffer = BufferUtils.createByteBuffer(size);
        for (Std140Compatible datum : data) {
            float[] fltArray = datum.getStd140Data();
            for (float flt : fltArray) {
                buffer.putFloat(flt);
            }
        }
        buffer.flip();
        return buffer;
    }
}
