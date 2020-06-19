package com.edenrump.graphic.mesh;

import com.edenrump.graphic.gpu.Attribute;
import com.edenrump.graphic.gpu.VertexArrayObject;
import com.edenrump.graphic.gpu.VertexBufferObject;
import com.edenrump.math.util.DataUtils;

import java.util.HashMap;
import java.util.Map;

import static com.edenrump.graphic.gpu.Attribute.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class GPUMesh {

    protected final VertexArrayObject vao;
    private final Map<String, Attribute> attributes = new HashMap<>();
    private final int dimensionsPerVertex;

    protected VertexBufferObject indexBuffer;

    private int elements;
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

    void setIndices(int[] indices) {
        indexBuffer = new VertexBufferObject();
        indexBuffer.bind(GL_ELEMENT_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ELEMENT_ARRAY_BUFFER, DataUtils.storeDataInBuffer(indices), GL_STATIC_DRAW);
    }

    protected Attribute[] getAttributes() {
        return attributes.values().toArray(Attribute[]::new);
    }

    protected void addAttribute(Attribute attribute) {
        attributes.put(attribute.getName(), attribute);
    }

    protected void addAttribute(int location, String name, float[] values) {
        VertexBufferObject vbo = new VertexBufferObject();
        vbo.bind(GL_ARRAY_BUFFER);
        VertexBufferObject.uploadData(
                GL_ARRAY_BUFFER,
                DataUtils.storeDataInBuffer(values),
                GL_STATIC_DRAW
        );
        Attribute attribute = new Attribute(
                location,
                name,
                dimensionsPerVertex,
                vbo.getID());
        this.addAttribute(attribute);
    }

    protected void setNormals(float[] values) {
        vao.bind();
        addAttribute(NORMALS_ATTRIB, NORMALS_ATTRIB_NAME, values);
        this.unbind();
    }

    public void setPositions(float[] positions, int[] indices) {
        elements = indices.length;
        vao.bind();

        addAttribute(POSITION_ATTRIB, POSITIONS_ATTRIB_NAME, positions);
        setIndices(indices);

        this.unbind();
    }

    public void updatePositions(float[] positions) {
        int positionVBO = attributes.get(POSITIONS_ATTRIB_NAME).getVBOId();
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
