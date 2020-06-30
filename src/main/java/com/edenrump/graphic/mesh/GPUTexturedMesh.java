/*
 * Copyright (c) 2020 Ed Eden-Rump
 *
 * This file is part of Nested Engine.
 *
 * Nested Engine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nested Engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Nested Engine.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.edenrump.graphic.mesh;

import com.edenrump.gpu.objects.Attribute;
import com.edenrump.gpu.objects.Texture;
import com.edenrump.gpu.objects.VertexBufferObject;
import com.edenrump.math.util.Buffers;

import static org.lwjgl.opengl.GL15.*;

/**
 * This class represents how a mesh should be created and stored in the application
 */
public class GPUTexturedMesh extends GPUMesh {

    private Texture texture;

    public GPUTexturedMesh(int dimensionsPerVertex) {
        super(dimensionsPerVertex);
    }

    @Override
    public void enableAttributes() {
        for (Attribute attribute : getAttributes()) {
            attribute.enableVertexAttribute();
        }

        glActiveTexture(GL_TEXTURE);
        glBindTexture(GL_TEXTURE_2D, getTexture().getId());

    }

    @Override
    public void disableAttributes() {
        for (Attribute attribute : getAttributes()) {
            attribute.disableVertexAttribute();
        }
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void setTexture(float[] textureCoords, Texture texture) {
        bindVAO();

        VertexBufferObject textureVBO = new VertexBufferObject();
        textureVBO.bind(GL_ARRAY_BUFFER);
        VertexBufferObject.uploadData(GL_ARRAY_BUFFER, Buffers.storeDataInBuffer(textureCoords), GL_STATIC_DRAW);
        Attribute textureAttrib = Attribute.getDefaultTextureCoordsAttribute(textureVBO.getID());
        textureAttrib.enableVertexAttribute();
        attributes.put(textureAttrib.getName(), textureAttrib);
        this.texture = texture;

        this.unbind();
    }

    public Texture getTexture() {
        return texture;
    }
}
