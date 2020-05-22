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
import com.edenrump.graphic.openGL_gpu.VertexBufferObject;
import com.edenrump.math.util.DataUtils;

import static org.lwjgl.opengl.GL15.*;

/**
 * This class provides methods for creating simple objects in OpenGL.
 */
public class MeshUtils {

    /**
     * Method to create a simple textured indexed mesh without any complex things like multiple-scale-models or mip-mapping
     *
     * @param positions     the positions of the vertices as a float array
     * @param indices       the indices of the vertices as an int array
     * @param textureCoords the texture coordinates of the mesh as a float array
     * @param textureFile   the file location of the texture to be used for this mesh
     * @return a new TextureIndexMesh created from the inputs.
     */
    public static Mesh loadTexturedIndexMesh3D(float[] positions, int[] indices, float[] textureCoords, String textureFile) {
        Mesh mesh = new Mesh(positions.length / 3);
        mesh.setDrawType(GL_TRIANGLES);
        mesh.getVao().bind();

        //TODO: support VBOs in the textured index mesh
        VertexBufferObject positionVBO = new VertexBufferObject();
        positionVBO.bind(GL_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ARRAY_BUFFER, DataUtils.storeDataInBuffer(positions), GL_STATIC_DRAW);
        Attribute positionsAttrib = Attribute.getDefault3DPositionsAttribute(positionVBO.getID());
        positionsAttrib.enableVertexAttribute();
        mesh.associateAttribute(positionsAttrib);

        VertexBufferObject indexVBO = new VertexBufferObject();
        indexVBO.bind(GL_ELEMENT_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ELEMENT_ARRAY_BUFFER, DataUtils.storeDataInBuffer(indices), GL_STATIC_DRAW);

        VertexBufferObject textureVBO = new VertexBufferObject();
        textureVBO.bind(GL_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ARRAY_BUFFER, DataUtils.storeDataInBuffer(textureCoords), GL_STATIC_DRAW);
        Attribute textureAttrib = Attribute.getDefaultTextureCoordsAttribute(textureVBO.getID());
        textureAttrib.enableVertexAttribute();
        mesh.associateAttribute(textureAttrib);

        mesh.setTexture(Texture.loadTexture(textureFile));

        return mesh;
    }

    public static Mesh loadTexturedMesh2D(float[] positions, int[] indices, String textureFile) {
        Mesh mesh = new Mesh(indices.length);
        mesh.setDrawType(GL_TRIANGLES);
        mesh.getVao().bind();

        VertexBufferObject positionVBO = new VertexBufferObject();
        positionVBO.bind(GL_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ARRAY_BUFFER, DataUtils.storeDataInBuffer(positions), GL_STATIC_DRAW);
        Attribute positionsAttribute = Attribute.getDefault2DPositionsAttribute(positionVBO.getID());
        positionsAttribute.enableVertexAttribute();
        mesh.associateAttribute(positionsAttribute);

        VertexBufferObject indexVBO = new VertexBufferObject();
        indexVBO.bind(GL_ELEMENT_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ELEMENT_ARRAY_BUFFER, DataUtils.storeDataInBuffer(indices), GL_STATIC_DRAW);

        VertexBufferObject textureVBO = new VertexBufferObject();
        textureVBO.bind(GL_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ARRAY_BUFFER, DataUtils.storeDataInBuffer(new float[]{0, 0, 0, 1, 1, 1, 1, 0}), GL_STATIC_DRAW);
        Attribute textureAttrib = Attribute.getDefaultTextureCoordsAttribute(textureVBO.getID());
        textureAttrib.enableVertexAttribute();
        mesh.associateAttribute(textureAttrib);
        mesh.setTexture(Texture.loadTexture(textureFile));

        return mesh;
    }

    public static Mesh loadMesh2D(float[] positions, int[] indices) {
        Mesh mesh = new Mesh(indices.length);
        mesh.setDrawType(GL_TRIANGLES);
        mesh.getVao().bind();

        VertexBufferObject positionVBO = new VertexBufferObject();
        positionVBO.bind(GL_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ARRAY_BUFFER, DataUtils.storeDataInBuffer(positions), GL_STATIC_DRAW);
        Attribute positionsAttribute = Attribute.getDefault2DPositionsAttribute(positionVBO.getID());
        positionsAttribute.enableVertexAttribute();
        mesh.associateAttribute(positionsAttribute);

        VertexBufferObject indexVBO = new VertexBufferObject();
        indexVBO.bind(GL_ELEMENT_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ELEMENT_ARRAY_BUFFER, DataUtils.storeDataInBuffer(indices), GL_STATIC_DRAW);

        return mesh;
    }
}
