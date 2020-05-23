package com.edenrump.graphic.math;

import com.edenrump.math.util.ByteUtils;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

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
