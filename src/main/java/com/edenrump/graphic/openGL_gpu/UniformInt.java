package com.edenrump.graphic.openGL_gpu;

import static org.lwjgl.opengl.GL20C.*;

public class UniformInt extends Uniform {

    public UniformInt(int shaderProgramID, int location, CharSequence name) {
        super(shaderProgramID, location, name);
    }

    public void update(int v1) {
        engageShader();
        glUniform1i(getLocation(), v1);
    }

    public void update2values(int v1, int v2) {
        engageShader();
        glUniform2i(getLocation(), v1, v2);
    }

    public void update3values(int v1, int v2, int v3) {
        engageShader();
        glUniform3i(getLocation(), v1, v2, v3);
    }

    public void update4values(int v1, int v2, int v3, int v4) {
        engageShader();
        glUniform4i(getLocation(), v1, v2, v3, v4);
    }
}
