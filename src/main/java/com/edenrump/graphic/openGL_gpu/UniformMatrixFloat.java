package com.edenrump.graphic.openGL_gpu;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20C.*;

public class UniformMatrixFloat extends Uniform {

    public UniformMatrixFloat(int shaderProgramID, int location, CharSequence name) {
        super(shaderProgramID, location, name);
    }

    public void update_2x2(FloatBuffer buffer){
        if(buffer.array().length != 4)
            throw new RuntimeException("Buffer of incorrect size to update Uniform Matrix of 2x2");
        engageShader();
        glUniformMatrix2fv(getLocation(), false, buffer);
    }

    public void update_3x3(FloatBuffer buffer){
        if(buffer.array().length != 9)
            throw new RuntimeException("Buffer of incorrect size to update Uniform Matrix of 3x3");
        engageShader();
        glUniformMatrix3fv(getLocation(), false, buffer);
    }

    public void update_4x4(FloatBuffer buffer){
        if(buffer.array().length != 16)
            throw new RuntimeException("Buffer of incorrect size to update Uniform Matrix of 4x4");
        engageShader();
        glUniformMatrix4fv(getLocation(), false, buffer);
    }
}
