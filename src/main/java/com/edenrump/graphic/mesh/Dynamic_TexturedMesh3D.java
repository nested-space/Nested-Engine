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

import com.edenrump.graphic.mesh.contracts.Drawable;
import com.edenrump.graphic.mesh.contracts.Updatable;
import com.edenrump.graphic.openGL_gpu.Attribute;
import com.edenrump.graphic.openGL_gpu.Texture;
import com.edenrump.graphic.openGL_gpu.VertexBufferObject;
import com.edenrump.math.util.DataUtils;

import static com.edenrump.graphic.openGL_gpu.Attribute.POSITIONS_ATTRIB_NAME;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;

/**
 * This class represents how a mesh should be created and stored in the application
 */
public class Dynamic_TexturedMesh3D extends Mesh implements Drawable, Updatable {

    private VertexBufferObject indexBuffer;
    private int elements;
    private int glDrawType = GL_TRIANGLES;

    private Texture texture;

    public Dynamic_TexturedMesh3D() {
        super();
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
    private void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Method to enable all attributes linked to this mesh
     */
    @Override
    public void enableAttributes() {
        for (Attribute attribute : getAttributes()) {
            attribute.enableVertexAttribute();
        }

        glActiveTexture(GL_TEXTURE);
        glBindTexture(GL_TEXTURE_2D, getTexture().getId());

    }

    /**
     * Method to disable all attributes linked to this mesh
     */
    @Override
    public void disableAttributes() {
        for (Attribute attribute : getAttributes()) {
            attribute.disableVertexAttribute();
        }
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public int getElements() {
        return elements;
    }

    @Override
    public int getDrawType() {
        return this.glDrawType;
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

        setIndices(indices);

        Drawable.unbind();
    }

    private void setIndices(int[] indices) {
        indexBuffer = new VertexBufferObject();
        indexBuffer.bind(GL_ELEMENT_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ELEMENT_ARRAY_BUFFER, DataUtils.storeDataInBuffer(indices), GL_STATIC_DRAW);

    }

    public void setTexture(float[] textureCoords, String textureFile) {
        bindVAO();

        VertexBufferObject textureVBO = new VertexBufferObject();
        textureVBO.bind(GL_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ARRAY_BUFFER, DataUtils.storeDataInBuffer(textureCoords), GL_STATIC_DRAW);
        Attribute textureAttrib = Attribute.getDefaultTextureCoordsAttribute(textureVBO.getID());
        textureAttrib.enableVertexAttribute();
        this.associateAttribute(textureAttrib);
        setTexture(Texture.loadTexture(textureFile));

        Drawable.unbind();
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
    public void updatePositions(float[] positions) {
        int positionVBO = getAttribute(POSITIONS_ATTRIB_NAME).getVBOId();
        VertexBufferObject.bind(positionVBO, GL_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ARRAY_BUFFER, DataUtils.storeDataInBuffer(positions), GL_STATIC_DRAW);
    }

    @Override
    public void updateIndices(int[] indices) {
        if (indexBuffer == null) {
            setIndices(indices);
        } else {
            indexBuffer.bind(GL_ELEMENT_ARRAY_BUFFER);
            VertexBufferObject.uploadData(GL_ELEMENT_ARRAY_BUFFER, DataUtils.storeDataInBuffer(indices), GL_STATIC_DRAW);
        }
    }
}
