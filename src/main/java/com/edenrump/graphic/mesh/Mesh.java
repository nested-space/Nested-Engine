package com.edenrump.graphic.mesh;

import com.edenrump.graphic.mesh.contracts.Drawable;
import com.edenrump.graphic.openGL_gpu.Attribute;
import com.edenrump.graphic.openGL_gpu.VertexArrayObject;
import com.edenrump.graphic.openGL_gpu.VertexBufferObject;
import com.edenrump.math.util.DataUtils;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;

public abstract class Mesh {

    private final VertexArrayObject vao;
    private final List<Attribute> attributes = new ArrayList<>();
    int elements;
    VertexBufferObject indexBuffer;

    Mesh() {
        vao = new VertexArrayObject();
    }

    VertexArrayObject getVao() {
        return vao;
    }

    List<Attribute> getAttributes() {
        return attributes;
    }

    Attribute getAttribute(String attributeName) {
        for (Attribute attribute : attributes) {
            if (attribute.getName().equals(attributeName)) return attribute;
        }
        return null;
    }

    void associateAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    public void setPositions(float[] positions, int[] indices){
        elements = indices.length;
        vao.bind();

        VertexBufferObject positionVBO = new VertexBufferObject();
        positionVBO.bind(GL_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ARRAY_BUFFER, DataUtils.storeDataInBuffer(positions), GL_STATIC_DRAW);
        Attribute positionsAttrib = Attribute.getDefault2DPositionsAttribute(positionVBO.getID());
        this.associateAttribute(positionsAttrib);

        indexBuffer = new VertexBufferObject();
        indexBuffer.bind(GL_ELEMENT_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ELEMENT_ARRAY_BUFFER, DataUtils.storeDataInBuffer(indices), GL_STATIC_DRAW);

        Drawable.unbind();
    }
}
