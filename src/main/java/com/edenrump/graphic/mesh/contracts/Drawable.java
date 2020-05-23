package com.edenrump.graphic.mesh.contracts;

import static org.lwjgl.opengl.GL30.glBindVertexArray;

public interface Drawable {

    static void unbind() {
        glBindVertexArray(0);
    }

    int getVAO_ID();

    void bindVAO();

    void enableAttributes();

    void disableAttributes();

    int getElements();

    int getDrawType();

    void setDrawType(int glDrawType);
}
