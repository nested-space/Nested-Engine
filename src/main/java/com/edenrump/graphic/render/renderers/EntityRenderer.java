package com.edenrump.graphic.render.renderers;

import com.edenrump.graphic.data.VertexArrayObject;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;

public class EntityRenderer implements GenericRenderer {

    public void prepare() {
        GL11.glClear(GL_COLOR_BUFFER_BIT);
        GL11.glClearColor(1, 0, 0, 1);
    }

    @Override
    public void render() {

    }

    public void cleanUp() {

    }

    public void render(VertexArrayObject vao) {
        //render
    }


}
