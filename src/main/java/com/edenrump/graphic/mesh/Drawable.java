package com.edenrump.graphic.mesh;

import com.edenrump.graphic.openGL_gpu.Attribute;

import static org.lwjgl.opengl.GL30.glBindVertexArray;

public interface Drawable {

    void associateAttribute(Attribute attribute);

    int getVAO_ID();

    void bindVAO();

    static void unbind(){
        glBindVertexArray(0);
    };

    void enableAttributes();

    void disableAttributes();

    int getVertexCount();

    void setVertexCount(int vertexCount);

    int getDrawType();

    void setDrawType(int glDrawType);
}
