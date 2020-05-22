/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2019 Ed Eden-Rump
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */

package com.edenrump.graphic.mesh;

import com.edenrump.graphic.openGL_gpu.Attribute;
import com.edenrump.graphic.openGL_gpu.Texture;
import com.edenrump.graphic.openGL_gpu.VertexArrayObject;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 * This class represents how a mesh should be created and stored in the application
 */
public class Mesh implements Drawable {

    /**
     * A vertex array object, representing the handle OpenGL uses for this entity.
     */
    private final VertexArrayObject vao;

    private final List<Attribute> attributes = new ArrayList<>();

    /**
     * Parameter that stores the texture ID of the texture
     * TODO: to manage more than one texture
     */
    private Texture texture;

    /**
     * Parameter to define whether mesh is textured
     */
    private boolean isTextured;

    private int vertexCount;
    private int glDrawType;

    public Mesh(int vertexCount) {
        this.vertexCount = vertexCount;
        vao = new VertexArrayObject();
    }

    /**
     * Method to get the VAO that stores the information for this mesh in OpenGL
     *
     * @return the VAO for this mesh
     */
    public VertexArrayObject getVao() {
        return vao;
    }

    /**
     * Method to link an attribute to this mesh
     *
     * @param attribute attribute to link to this mesh
     */
    @Override
    public void associateAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    @Override
    public int getVAO_ID() {
        return getVao().getID();
    }

    @Override
    public void bindVAO() {
        getVao().bind();
    }

    public Attribute getAttribute(String attributeName) {
        for (Attribute attribute : attributes) {
            if (attribute.getName().equals(attributeName)) return attribute;
        }
        return null;
    }

    /**
     * Method to get whether mesh is textured or not
     *
     * @return isTextured
     */
    public boolean isTextured() {
        return isTextured;
    }

    /**
     * Method to set whether mesh is textured or not
     *
     * @param textured whether mesh is textured or not
     */
    public void setTextured(boolean textured) {
        isTextured = textured;
    }

    /**
     * Method to get the texture of this texturedIndexMesh
     *
     * @return the texture
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Method to set the texture of the this mesh
     *
     * @param texture the texture to be used to model this mesh
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
        setTextured(true);
    }

    /**
     * Method to enable all attributes linked to this mesh
     */
    @Override
    public void enableAttributes() {
        for (Attribute attribute : attributes) {
            attribute.enableVertexAttribute();
        }

        if (isTextured) {
            glActiveTexture(GL_TEXTURE);
            glBindTexture(GL_TEXTURE_2D, getTexture().getId());
        }

    }

    /**
     * Method to disable all attributes linked to this mesh
     */
    @Override
    public void disableAttributes() {
        for (Attribute attribute : attributes) {
            attribute.disableVertexAttribute();
        }
    }

    @Override
    public int getVertexCount() {
        return vertexCount;
    }

    @Override
    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }

    @Override
    public int getDrawType() {
        return this.glDrawType;
    }

    @Override
    public void setDrawType(int glDrawType) {
        this.glDrawType = glDrawType;
    }
}
