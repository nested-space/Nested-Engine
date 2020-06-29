package com.edenrump.graphic.mesh;

import com.edenrump.gpu.Attribute;
import com.edenrump.gpu.VertexArrayObject;
import com.edenrump.gpu.VertexBufferObject;
import com.edenrump.math.util.Buffers;

import java.util.HashMap;
import java.util.Map;

import static com.edenrump.gpu.Attribute.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/*
 * Copyright (c) 2020 Ed Eden-Rump
 *     This file is part of Nested Engine.
 *
 *     Nested Engine is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Nested Engine is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Nested Engine.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     @Author Ed Eden-Rump
 *
 */

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
        vao.bind();
        indexBuffer = new VertexBufferObject();
        indexBuffer.bind(GL_ELEMENT_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ELEMENT_ARRAY_BUFFER, Buffers.storeDataInBuffer(indices), GL_STATIC_DRAW);
        this.unbind();
    }

    public void bindVAO() {
        vao.bind();
    }

    public void addAttribute(int location, String name, float[] values) {
        VertexBufferObject vbo = new VertexBufferObject();
        vao.bind();
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
        this.unbind();
    }

    public void setPositions(float[] positions, int[] indices) {
        numberOfElements = indices.length;
        addAttribute(POSITION_ATTRIB, POSITIONS_ATTRIB_NAME, positions);
        setIndices(indices);
    }

    void unbind() {
        glBindVertexArray(0);
    }

}
