package com.edenrump.gpu;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

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
 * This class represents a uniform buffer object in OpenGL.
 *
 * @author Ed Eden-Rump
 */
public class UniformBlockBuffer {

    private final int id;

    public int getId() {
        return id;
    }

    /**
     * Constructor creates buffer in OpenGL and saves id handle as id
     */
    public UniformBlockBuffer() {
        id = glGenBuffers();
    }

    /**
     * Method to add data to the allocated buffer in the form of a int array.
     *
     * @param buffer data to be transferred to the GPU
     */
    public void reallocateAndUpload(IntBuffer buffer) {
        glBindBuffer(GL_UNIFORM_BUFFER, getId());
        glBufferData(GL_UNIFORM_BUFFER, buffer, GL_DYNAMIC_DRAW);
    }

    public void blockBind(int bufferBlockBinding) {
        glBindBuffer(GL_UNIFORM_BUFFER, getId());
        glBindBufferBase(GL_UNIFORM_BUFFER, bufferBlockBinding, getId());
        glBufferData(GL_UNIFORM_BUFFER, 80, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    /**
     * Method to bind the uniform buffer to the binding point <tt>GL_UNIFORM_BUFFER</tt>
     */
    public void bind() {
        glBindBuffer(GL_UNIFORM_BUFFER, getId());
    }

    /**
     * Method to bind the binding point <tt>GL_UNIFORM_BUFFER</tt> to uniform buffer location zero, therefore
     * invalidating any non-persistent mapped buffers (reducing impact of accidental memory writes)
     */
    public void unbind() {
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    /**
     * Method to update data already present in the buffer
     *
     * @param buffer data to be transferred to the GPU
     */
    public void updateBuffer(FloatBuffer buffer) {
        glBindBuffer(GL_UNIFORM_BUFFER, id);
        ByteBuffer mappedBuffer = glMapBuffer(GL_UNIFORM_BUFFER, GL_READ_WRITE, buffer.limit() * Float.BYTES, null);
        Objects.requireNonNull(mappedBuffer).clear();
        mappedBuffer.asFloatBuffer().put(buffer);
        mappedBuffer.flip();
        glUnmapBuffer(GL_UNIFORM_BUFFER);
    }

    /**
     * Method to update data already present in the buffer
     *
     * @param buffer data to be transferred to the GPU
     */
    public void updateBuffer(ByteBuffer buffer) {
        glBindBuffer(GL_UNIFORM_BUFFER, id);
        ByteBuffer mappedBuffer = glMapBuffer(GL_UNIFORM_BUFFER, GL_READ_WRITE, buffer.limit() * Float.BYTES, null);
        Objects.requireNonNull(mappedBuffer).clear();
        mappedBuffer.put(buffer);
        mappedBuffer.flip();
        glUnmapBuffer(GL_UNIFORM_BUFFER);
    }

    /**
     * Method to update data already present in the buffer
     *
     * @param buffer data to be transferred to the GPU
     */
    public void updateBuffer(IntBuffer buffer) {
        glBindBuffer(GL_UNIFORM_BUFFER, id);
        ByteBuffer mappedBuffer = glMapBuffer(GL_UNIFORM_BUFFER, GL_READ_WRITE, buffer.remaining() * Integer.BYTES, null);
        Objects.requireNonNull(mappedBuffer).clear();
        mappedBuffer.asIntBuffer().put(buffer);
        mappedBuffer.flip();
        glUnmapBuffer(GL_UNIFORM_BUFFER);
    }

    /**
     * Method to allocate data on the GPU in the buffer created at init
     *
     * @param bytes number of bytes to be allocated
     */
    public void allocate(int bytes) {
        glBindBuffer(GL_UNIFORM_BUFFER, id);
        glBufferData(GL_UNIFORM_BUFFER, bytes, GL_DYNAMIC_DRAW);
    }

    /**
     * Method to add data to the allocated buffer in the form of a float array.
     *
     * @param buffer data to be transferred to the GPU
     */
    public void reallocateAndUpload(FloatBuffer buffer) {
        glBindBuffer(GL_UNIFORM_BUFFER, id);
        glBufferData(GL_UNIFORM_BUFFER, buffer, GL_DYNAMIC_DRAW);
    }
}
