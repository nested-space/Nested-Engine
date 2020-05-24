package com.edenrump.graphic.entities;

import static org.lwjgl.opengl.GL30.glBindVertexArray;

public interface Renderable {

    int getID();

    void prepare();

    void update();

    void finish();

    int getDrawType();

    int getElements();

    static void unbind() {
        glBindVertexArray(0);
    }

}
