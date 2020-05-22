package com.edenrump.graphic.math;

import java.nio.FloatBuffer;

public interface Std140Compatible {

    int getStd140Size();

    int getStd140Bytes();

    float[] getStd140Data();

    void storeStd140DataInBuffer(FloatBuffer buffer);
}
