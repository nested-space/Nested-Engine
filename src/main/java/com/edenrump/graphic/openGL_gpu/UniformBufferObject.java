package com.edenrump.graphic.openGL_gpu;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

public class UniformBufferObject extends UniformBuffer {

    public UniformBufferObject() {
        super();
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
