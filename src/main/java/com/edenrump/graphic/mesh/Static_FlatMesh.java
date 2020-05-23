package com.edenrump.graphic.mesh;

import com.edenrump.graphic.mesh.contracts.Drawable;
import com.edenrump.graphic.openGL_gpu.Attribute;
import com.edenrump.graphic.openGL_gpu.VertexBufferObject;
import com.edenrump.math.util.DataUtils;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.*;

public class Static_FlatMesh extends Mesh implements Drawable {

    int glDrawType = GL_TRIANGLES;
    int elements;

    public Static_FlatMesh() {
        super();
    }

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

    @Override
    public void setPositions(float[] positions, int[] indices) {
        elements = indices.length;
        bindVAO();

        VertexBufferObject positionVBO = new VertexBufferObject();
        positionVBO.bind(GL_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ARRAY_BUFFER, DataUtils.storeDataInBuffer(positions), GL_STATIC_DRAW);
        Attribute positionsAttrib = Attribute.getDefault2DPositionsAttribute(positionVBO.getID());
        this.associateAttribute(positionsAttrib);

        VertexBufferObject indexBuffer = new VertexBufferObject();
        indexBuffer.bind(GL_ELEMENT_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ELEMENT_ARRAY_BUFFER, DataUtils.storeDataInBuffer(indices), GL_STATIC_DRAW);

        Drawable.unbind();
    }
}
