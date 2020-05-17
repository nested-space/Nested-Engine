package com.edenrump.graphic.data;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

/**
 * This class represents a uniform buffer object in OpenGL.
 *
 * @author Ed Eden-Rump
 */
public class UniformBufferObject {

    /**
     * The index of the uniform buffer object in OpenGL memory
     */
    private final int id;

    /**
     * Constructor creates buffer in OpenGL and saves id handle as id
     */
    public UniformBufferObject() {
        id = glGenBuffers();
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

    /**
     * Method to add data to the allocated buffer in the form of a int array.
     *
     * @param buffer data to be transferred to the GPU
     */
    public void reallocateAndUpload(IntBuffer buffer) {
        glBindBuffer(GL_UNIFORM_BUFFER, id);
        glBufferData(GL_UNIFORM_BUFFER, buffer, GL_DYNAMIC_DRAW);
    }

    /**
     * Method to update data already present in the buffer
     *
     * @param buffer data to be transferred to the GPU
     * @param length number of bytes - get this with Float.BYTES * number of floats
     */
    public void updateData(FloatBuffer buffer, int length) {
        glBindBuffer(GL_UNIFORM_BUFFER, id);
        ByteBuffer mappedBuffer = glMapBuffer(GL_UNIFORM_BUFFER, GL_READ_WRITE, length, null);
        Objects.requireNonNull(mappedBuffer).clear();
        mappedBuffer.asFloatBuffer().put(buffer);
//        for (int i = 0; i < length / Float.BYTES; i++) {
//            mappedBuffer.putFloat(buffer.get(i));
//        }
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
//        for (int i = 0; i < length / Float.BYTES; i++) {
//            mappedBuffer.putFloat(buffer.get(i));
//        }
        mappedBuffer.flip();
        glUnmapBuffer(GL_UNIFORM_BUFFER);
    }

    /**
     * Method to bind the uniform buffer to the binding point <tt>GL_UNIFORM_BUFFER</tt>
     */
    public void bind() {
        glBindBuffer(GL_UNIFORM_BUFFER, id);
    }

    /**
     * Method to bind the binding point <tt>GL_UNIFORM_BUFFER</tt> to uniform buffer location zero, therefore
     * invalidating any non-persistent mapped buffers (reducing impact of accidental memory writes)
     */
    public void unbind() {
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }


}