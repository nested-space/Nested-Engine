package com.edenrump.graphic.openGL_gpu;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

/**
 * This class represents a uniform buffer object in OpenGL.
 *
 * @author Ed Eden-Rump
 */
public class UniformBlockBuffer extends UniformBuffer {

    /**
     * Constructor creates buffer in OpenGL and saves id handle as id
     */
    public UniformBlockBuffer() {
        super();
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


}
