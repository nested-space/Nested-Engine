package com.edenrump.gpu;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

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

/**
 * This class represents a Vertex Buffer Object (VertexBufferObject).
 *
 * @author Heiko Brumme
 */
public class VertexBufferObject {

    /**
     * Stores the handle of the VertexBufferObject.
     */
    private final int id;

    /**
     * Creates a Vertex Buffer Object (VertexBufferObject).
     */
    public VertexBufferObject() {
        id = glGenBuffers();
    }

    public static void bind(int id, int target) {
        glBindBuffer(target, id);
    }

    /**
     * Binds OpenGL buffers to position 0, meaning to meaningful changes can be made by accident.
     */
    public static void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    /**
     * Upload vertex data to this VertexBufferObject with specified target, data and usage. The
     * target in the tutorial should be <code>GL_ARRAY_BUFFER</code> and usage
     * should be <code>GL_STATIC_DRAW</code> most of the time.
     *
     * @param target Target to upload
     * @param data   Buffer with the data to upload
     * @param usage  Usage of the data
     */
    public static void uploadData(int target, FloatBuffer data, int usage) {
        glBufferData(target, data, usage);
    }

    /**
     * Upload null data to this VertexBufferObject with specified target, size and usage. The
     * target in the tutorial should be <code>GL_ARRAY_BUFFER</code> and usage
     * should be <code>GL_STATIC_DRAW</code> most of the time.
     *
     * @param target Target to upload
     * @param size   Size in bytes of the VertexBufferObject data store
     * @param usage  Usage of the data
     */
    public static void uploadData(int target, long size, int usage) {
        glBufferData(target, size, usage);
    }

    /**
     * Upload sub data to this VertexBufferObject with specified target, offset and data. The
     * target in the tutorial should be <code>GL_ARRAY_BUFFER</code> most of the
     * time.
     *
     * @param target Target to upload
     * @param offset Offset where the data should go in bytes
     * @param data   Buffer with the data to upload
     */
    public static void uploadSubData(int target, long offset, FloatBuffer data) {
        glBufferSubData(target, offset, data);
    }

    /**
     * Upload sub data to this VertexBufferObject with specified target, offset and data. The
     * target in the tutorial should be <code>GL_ARRAY_BUFFER</code> most of the
     * time.
     *
     * @param target Target to upload
     * @param offset Offset where the data should go in bytes
     * @param data   Buffer with the data to upload
     */
    public static void uploadSubData(int target, long offset, IntBuffer data) {
        glBufferSubData(target, offset, data);
    }

    /**
     * Upload element data to this EBO with specified target, data and usage.
     * The target in the tutorial should be <code>GL_ELEMENT_ARRAY_BUFFER</code>
     * and usage should be <code>GL_STATIC_DRAW</code> most of the time.
     *
     * @param target Target to upload
     * @param data   Buffer with the data to upload
     * @param usage  Usage of the data
     */
    public static void uploadData(int target, IntBuffer data, int usage) {
        glBufferData(target, data, usage);
    }

    /**
     * Binds this VertexBufferObject with specified target.
     *
     * @param target Target to bind
     */
    public void bind(int target) {
        glBindBuffer(target, id);
    }

    /**
     * Deletes this VertexBufferObject.
     */
    public void delete() {
        glDeleteBuffers(id);
    }

    /**
     * Getter for the Vertex Buffer Object ID.
     *
     * @return Handle of the VertexBufferObject
     */
    public int getID() {
        return id;
    }

}
