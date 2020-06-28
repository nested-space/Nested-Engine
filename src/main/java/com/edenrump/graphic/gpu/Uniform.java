package com.edenrump.graphic.gpu;

import static org.lwjgl.opengl.GL20.glUseProgram;

public class Uniform {

    final int location;
    final int shaderProgramID;
    final CharSequence name;

    public Uniform(int shaderProgramID, int location, CharSequence name) {
        this.shaderProgramID = shaderProgramID;
        this.location = location;
        this.name = name;
    }

    public CharSequence getName() {
        return name;
    }

    public int getShaderProgramID() {
        return shaderProgramID;
    }

    public int getLocation() {
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

    public UniformMatrixFloat asUniformMatrix() {
        return new UniformMatrixFloat(shaderProgramID, location, name);
    }

}
