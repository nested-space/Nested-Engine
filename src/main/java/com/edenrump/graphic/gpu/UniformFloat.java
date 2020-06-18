package com.edenrump.graphic.gpu;

import static org.lwjgl.opengl.GL20C.*;

public class UniformFloat extends Uniform {

    public UniformFloat(int shaderProgramID, int location, CharSequence name) {
        super(shaderProgramID, location, name);
    }

    public void update(float value) {
        engageShader();
        glUniform1f(getLocation(), value);
    }

    public void update2values(float v1, float v2) {
        engageShader();
        glUniform2f(getLocation(), v1, v2);
    }

    public void update3values(float v1, float v2, float v3) {
        engageShader();
        glUniform3f(getLocation(), v1, v2, v3);
    }

    public void update4values(float v1, float v2, float v3, float v4) {
        engageShader();
        glUniform4f(getLocation(), v1, v2, v3, v4);
    }
}
