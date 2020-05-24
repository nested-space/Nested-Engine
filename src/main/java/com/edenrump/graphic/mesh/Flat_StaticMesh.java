package com.edenrump.graphic.mesh;

import com.edenrump.graphic.mesh.contracts.Drawable;
import com.edenrump.graphic.openGL_gpu.Attribute;
import com.edenrump.graphic.openGL_gpu.VertexBufferObject;
import com.edenrump.math.util.DataUtils;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.*;

public class Flat_StaticMesh extends Mesh implements Drawable {

    int glDrawType = GL_TRIANGLES;

    @Override
    public int getVAO_ID() {
        return getVao().getID();
    }

    @Override
    public void bindVAO() {
        getVao().bind();
    }

    @Override
    public void enableAttributes() {
        for (Attribute attribute : getAttributes()) {
            attribute.enableVertexAttribute();
        }
    }

    @Override
    public void disableAttributes() {
        for (Attribute attribute : getAttributes()) {
            attribute.disableVertexAttribute();
        }
    }

    @Override
    public int getElements() {
        return elements;
    }

    @Override
    public int getDrawType() {
        return glDrawType;
    }

    @Override
    public void setDrawType(int glDrawType) {
        this.glDrawType = glDrawType;
    }
}
