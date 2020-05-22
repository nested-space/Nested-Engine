package com.edenrump.graphic.openGL_gpu;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glUnmapBuffer;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

public abstract class UniformBuffer {

    private int id;

    UniformBuffer(){
        id = glGenBuffers();
    }

    /**
     * Method to update data already present in the buffer
     *
     * @param buffer data to be transferred to the GPU
     */
    public void updateData(FloatBuffer buffer) {
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
     * @param length number of bytes - get this with Integer.BYTES * number of ints
     */
    public void updateData(IntBuffer buffer, int length) {
        glBindBuffer(GL_UNIFORM_BUFFER, id);
        ByteBuffer mappedBuffer = glMapBuffer(GL_UNIFORM_BUFFER, GL_READ_WRITE, length, null);
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

    int getId() {
        return id;
    }
}
