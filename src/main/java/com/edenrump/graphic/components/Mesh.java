package com.edenrump.graphic.components;

import com.edenrump.graphic.openGL_gpu.Attribute;
import com.edenrump.graphic.openGL_gpu.VertexArrayObject;
import com.edenrump.graphic.openGL_gpu.VertexBufferObject;
import com.edenrump.math.util.DataUtils;

import java.util.ArrayList;
import java.util.List;

import static com.edenrump.graphic.openGL_gpu.Attribute.POSITIONS_ATTRIB_NAME;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Mesh {

    int elements;
    int dimensionsPerVertex = 3;
    private int glDrawType = GL_TRIANGLES;

    protected final VertexArrayObject vao;
    protected VertexBufferObject indexBuffer;
    private final List<Attribute> attributes = new ArrayList<>();

    public void enableAttributes() {
        for (Attribute attribute : getAttributes()) {
            attribute.enableVertexAttribute();
        }
        glActiveTexture(GL_TEXTURE);
    }

    public void disableAttributes() {
        for (Attribute attribute : getAttributes()) {
            attribute.disableVertexAttribute();
        }
    }

    public int getElements() {
        return elements;
    }

    public int getVAO_ID() {
        return vao.getID();
    }

    public void bindVAO() {
        vao.bind();
    }

    public int getDrawType() {
        return this.glDrawType;
    }

    public void setDrawType(int glDrawType) {
        this.glDrawType = glDrawType;
    }

    public Mesh() {
        vao = new VertexArrayObject();
    }

    void setIndices(int[] indices) {
        indexBuffer = new VertexBufferObject();
        indexBuffer.bind(GL_ELEMENT_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ELEMENT_ARRAY_BUFFER, DataUtils.storeDataInBuffer(indices), GL_STATIC_DRAW);
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

    void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    public void setPositions(float[] positions, int[] indices){
        elements = indices.length;
        System.out.println("Dimensions: " + dimensionsPerVertex);
        vao.bind();

        VertexBufferObject positionVBO = new VertexBufferObject();
        positionVBO.bind(GL_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ARRAY_BUFFER, DataUtils.storeDataInBuffer(positions), GL_STATIC_DRAW);
        Attribute positionsAttrib = Attribute.getDefaultPositionsAttribute(positionVBO.getID(), dimensionsPerVertex);
        this.addAttribute(positionsAttrib);

        indexBuffer = new VertexBufferObject();
        indexBuffer.bind(GL_ELEMENT_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ELEMENT_ARRAY_BUFFER, DataUtils.storeDataInBuffer(indices), GL_STATIC_DRAW);

        this.unbind();
    }

    public void updatePositions(float[] positions) {
        int positionVBO = this.getAttribute(POSITIONS_ATTRIB_NAME).getVBOId();
        VertexBufferObject.bind(positionVBO, GL_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ARRAY_BUFFER, DataUtils.storeDataInBuffer(positions), GL_STATIC_DRAW);
        //TODO: improve with map buffer;
    }

    public void updateIndices(int[] indices) {
        if (indexBuffer == null) {
            setIndices(indices);
        } else {
            indexBuffer.bind(GL_ELEMENT_ARRAY_BUFFER);
            VertexBufferObject.uploadData(GL_ELEMENT_ARRAY_BUFFER, DataUtils.storeDataInBuffer(indices), GL_STATIC_DRAW);
        }
    }

    void unbind() {
        glBindVertexArray(0);
    }
}
