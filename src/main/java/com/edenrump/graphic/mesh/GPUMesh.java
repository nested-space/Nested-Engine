package com.edenrump.graphic.mesh;

import com.edenrump.graphic.gpu.Attribute;
import com.edenrump.graphic.gpu.VertexArrayObject;
import com.edenrump.graphic.gpu.VertexBufferObject;
import com.edenrump.math.util.Buffers;

import java.util.HashMap;
import java.util.Map;

import static com.edenrump.graphic.gpu.Attribute.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class GPUMesh {

    protected final VertexArrayObject vao;
    protected VertexBufferObject indexBuffer;

    protected final Map<String, Attribute> attributes = new HashMap<>();
    private final int dimensionsPerVertex;
    private int numberOfElements;
    private int glDrawType = GL_TRIANGLES;

    public GPUMesh(int dimensionsPerVertex) {
        this.dimensionsPerVertex = dimensionsPerVertex;
        vao = new VertexArrayObject();
    }

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

    public int getVAO_ID() {
        return vao.getID();
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public int getDrawType() {
        return this.glDrawType;
    }

    protected Attribute[] getAttributes() {
        return attributes.values().toArray(Attribute[]::new);
    }

    public void setDrawType(int glDrawType) {
        this.glDrawType = glDrawType;
    }

    public void setIndices(int[] indices) {
        indexBuffer = new VertexBufferObject();
        indexBuffer.bind(GL_ELEMENT_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ELEMENT_ARRAY_BUFFER, Buffers.storeDataInBuffer(indices), GL_STATIC_DRAW);
    }

    public void bindVAO() {
        vao.bind();
    }

    public void addAttribute(int location, String name, float[] values) {
        VertexBufferObject vbo = new VertexBufferObject();
        vbo.bind(GL_ARRAY_BUFFER);
        VertexBufferObject.uploadData(
                GL_ARRAY_BUFFER,
                Buffers.storeDataInBuffer(values),
                GL_STATIC_DRAW
        );
        Attribute attribute = new Attribute(
                location,
                name,
                dimensionsPerVertex,
                vbo.getID());
        attributes.put(attribute.getName(), attribute);
    }

    public void setPositions(float[] positions, int[] indices) {
        numberOfElements = indices.length;
        vao.bind();

        addAttribute(POSITION_ATTRIB, POSITIONS_ATTRIB_NAME, positions);
        setIndices(indices);

        this.unbind();
    }

    void unbind() {
        glBindVertexArray(0);
    }

}
