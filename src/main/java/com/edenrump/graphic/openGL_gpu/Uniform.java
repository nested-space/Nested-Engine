package com.edenrump.graphic.openGL_gpu;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Uniform {

    int location;
    int shaderProgramID;
    CharSequence name;

    public Uniform(int shaderProgramID, int location, CharSequence name) {
        this.shaderProgramID = shaderProgramID;
        this.location = location;
        this.name = name;
    }

    public int getShaderProgramID() {
        return shaderProgramID;
    }

    int getLocation() {
        return location;
    }

    void engageShader() {
        glUseProgram(shaderProgramID);
    }

    void disengageShader() {
        glUseProgram(0);
    }

    public UniformFloat asUniformFloat() {
        return new UniformFloat(shaderProgramID, location, name);
    }

    public UniformInt asUniformInt() {
        return new UniformInt(shaderProgramID, location, name);
    }

    UniformMatrixFloat asUniformMatrix() {
        return new UniformMatrixFloat(shaderProgramID, location, name);
    }
}
